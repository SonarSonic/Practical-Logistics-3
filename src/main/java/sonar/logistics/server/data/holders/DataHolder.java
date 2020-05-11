package sonar.logistics.server.data.holders;

import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataWatcher;
import sonar.logistics.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

/** the data holder, does what the name suggests and stores the output of a method
 * it also stores the time until the data will be updated and who / what is using / watching the data*/
public class DataHolder<D extends IData> {

    public final List<IDataWatcher> dataWatchers;
    public D data;
    public int ticks;
    public int tickRate;
    public boolean hasWatchers = false;
    public boolean forceUpdate = false;
    public boolean sourceIncompatible = false;

    public DataHolder(int tickRate){
        this.dataWatchers = new ArrayList<>();
        this.tickRate = tickRate;
        this.ticks = MathUtils.randInt(0, tickRate); ///attempting to distribute updates more evenly
    }

    public boolean canUpdateData() {
        return !sourceIncompatible && (forceUpdate || !dataWatchers.isEmpty() && ticks == tickRate);
    }

    public void onWatchersChanged() {
        if(dataWatchers.isEmpty()){
            hasWatchers = false;
            ///DataManager.instance().removeDataHolder(this); //FIXME
            return;
        }

        for (IDataWatcher watcher : dataWatchers) {
            if (watcher.isWatcherActive()) {
                hasWatchers = true;
                return;
            }
        }
        hasWatchers = false;
    }

    public void tick() {
        if(!hasWatchers || dataWatchers.isEmpty()){
            return;
        }
        if(ticks>=tickRate) {
            ticks = 0;
            doTick();
        }else{
            ticks++;
        }
    }

    public void doTick(){}

    public void addWatcher(IDataWatcher watcher){
        dataWatchers.add(watcher);
        onWatchersChanged();
    }

    public void removeWatcher(IDataWatcher watcher){
        dataWatchers.remove(watcher);
        onWatchersChanged();
    }

    public void onDataChanged(){
        dataWatchers.forEach(w -> w.onDataChanged(this));
    }

    public void onHolderDestroyed(){
        dataWatchers.forEach(w -> w.removeDataHolder(this));
    }
}
