package sonar.logistics.server.data.deleteme.holders;
/*

import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.api.IDataSource;

import java.util.HashMap;
import java.util.Map;

public class SourceHolder {

    public IDataSource source;
    public Map<Method, DataHolder> holders = new HashMap<>();

    public SourceHolder(IDataSource source){
        this.source = source;
    }

    public void updateData(){
        for(Map.Entry<Method, DataHolder> entry : holders.entrySet()){
            entry.getValue().tick(); //FIXME change to the tick bit, this UPDATES EVERY TICK AT THE MO, BAD PERFORMANCE ATM!
            if(/*holder.canUpdateData() && entry.getKey().canInvoke(source)){ //FIXME

                entry.getValue().data.preUpdate();
                Object obj = entry.getKey().invoke(source);
                entry.getKey().getDataType().factory.convert(entry.getValue().data, obj);
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
        */
