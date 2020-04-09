package sonar.logistics.server.data.holders;

import sonar.logistics.server.data.api.IEnvironment;
import sonar.logistics.server.data.api.methods.IMethod;
import sonar.logistics.server.data.sources.IDataSource;

import java.util.HashMap;
import java.util.Map;

public class SourceHolder {

    public IDataSource source;
    public IEnvironment environment;
    public Map<IMethod, DataHolder> holders = new HashMap<>();

    public SourceHolder(IDataSource source, IEnvironment environment){
        this.source = source;
        this.environment = environment;
    }

    public void updateData(){
        for(Map.Entry<IMethod, DataHolder> entry : holders.entrySet()){
            entry.getValue().tick(); //FIXME change to the tick bit, this UPDATES EVERY TICK AT THE MO, BAD PERFORMANCE ATM!
            if(/*holder.canUpdateData() && */entry.getKey().canInvoke(environment)){ //FIXME

                entry.getValue().data.preUpdate();
                Object obj =  entry.getKey().invoke(environment);
                entry.getKey().getDataFactory().convert(entry.getValue().data, obj);
                entry.getValue().data.postUpdate();

                if(entry.getValue().forceUpdate ||entry.getValue().data.hasUpdated()){
                    System.out.println(entry.getKey().getIdentifier() + " "  + obj);
                    entry.getValue().onDataChanged();
                    entry.getValue().data.onUpdated();
                    entry.getValue().forceUpdate = false;
                    ///SEND UPDATE!!!!!
                }
            }
        }
    }

}
