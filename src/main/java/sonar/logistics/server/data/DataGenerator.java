package sonar.logistics.server.data;

import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.server.data.watchers.DataWatcher;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public List<DataWatcher> dataWatchers = new ArrayList<>();
    public IData data = null;
    public DataType type = null;
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
