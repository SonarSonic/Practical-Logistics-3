package sonar.logistics.server.data;

import sonar.logistics.server.data.api.*;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.address.Environment;
import sonar.logistics.util.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {

    public final Address sourceAddress;
    public final Environment environment;
    public final Map<DataAddress, IData> dataMap = new HashMap<>();
    public final Map<DataAddress, Map<IDataWatcher, Integer>> watchers = new HashMap<>();
    public List<DataAddress> toUpdate = new ArrayList<>();

    public int ticks;
    public int tickRate;

    /*
    TODO
    public int tickPass;
    public int maxTickPass;
     */

    public DataSource(Address address){
        this.sourceAddress = address;
        this.environment = new Environment(address);
        this.ticks = MathUtils.randInt(0, tickRate); ///distributes updates more evenly
    }


    public void tick(){
        if(ticks>=tickRate) {
            ticks = 0;

            ///check if any data watchers are active and find which methods need updating
            toUpdate.clear();
            loop: for(Map.Entry<DataAddress, Map<IDataWatcher, Integer>> methodWatchers : watchers.entrySet()){
                for(IDataWatcher watcher : methodWatchers.getValue().keySet()){
                    if(watcher.isWatcherActive()){
                        toUpdate.add(methodWatchers.getKey());
                        continue loop;
                    }
                }
            }


            if(!toUpdate.isEmpty()){

                ///update the data
                if(sourceAddress.updateEnvironment(environment)){
                    for(DataAddress address : toUpdate){
                        IData data = dataMap.get(address);

                        data.preUpdate();
                        Object returned = address.method.invoke(environment);
                        address.method.getDataFactory().convert(data, returned);
                        data.postUpdate();

                        if(data.hasUpdated()){
                            data.onUpdated(); //TODO CHECK THIS IS USED AS SHOULD BE?
                            for(IDataWatcher watchers : watchers.get(address).keySet()){
                                watchers.onDataUpdate(address, data);
                            }
                        }
                    }
                }else{
                    ///TODO TELL WATCHERS THAT THE SOURCE IS INVALID
                    ///TODO ENTER LAZY TICK MODULATION
                }
            }


        }else{
            ticks++;
        }
    }

    //// DATA / METHODS \\\\

    @Nullable
    public DataAddress getDataAddress(Method method){
        for(Map.Entry<DataAddress, IData> dataEntry : dataMap.entrySet()){
            if(dataEntry.getKey().method.equals(method)){
                return dataEntry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public IData getData(Method method){
        DataAddress address = getDataAddress(method);
        return address == null ? null : getData(address);
    }

    @Nullable
    public IData getData(DataAddress address){
        return dataMap.get(address);
    }

    public DataAddress addMethod(Method method, IDataWatcher watcher){
        DataAddress address = new DataAddress(sourceAddress, method);
        dataMap.putIfAbsent(address, method.getDataFactory().create());
        watchers.putIfAbsent(address, new HashMap<>());

        Map<IDataWatcher, Integer> methodWatchers = watchers.get(address);
        methodWatchers.compute(watcher, (k, v) -> (v == null) ? 1 : v + 1);
        return address;
    }

    public void removeMethod(Method method, IDataWatcher watcher){
        DataAddress address = new DataAddress(sourceAddress, method);
        Map<IDataWatcher, Integer> methodWatchers = watchers.get(address);
        if(methodWatchers != null){
            int tally = methodWatchers.get(watcher);
            if(tally > 1){
                methodWatchers.putIfAbsent(watcher, tally - 1);
            }else{
                dataMap.remove(address);
                watchers.remove(address);
            }
        }
    }

}