package sonar.logistics.server.data.api;

import sonar.logistics.server.data.api.methods.IMethod;
import sonar.logistics.server.data.holders.DataHolder;

import java.util.List;

/**used for combining multiple bits of data together into a different form*/
public interface IDataMerger<D extends IData> {

    Class<D> getDataType();

    IMethod getDataMethod();

    void generateData(D data, List<DataHolder> validHolders);

    boolean isValidHolder(DataHolder holder);

    //FIXME - WRITE TO NBT / READ FROM NBT

}
