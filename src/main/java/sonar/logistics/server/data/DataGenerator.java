package sonar.logistics.server.data;

import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataWatcher;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public List<IDataWatcher> dataWatchers = new ArrayList<>();
    public IData data = null;
    public DataRegistry.DataType type = null;
    public int ticks;
    public int tickRate;

    public void tick(){
        if(ticks >= tickRate){
            // update


            ticks = 0;
        }else{
            ticks++;
        }
    }


    //// EVENTS \\\\

}
