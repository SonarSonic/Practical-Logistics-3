package sonar.logistics.server.data.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**used for converting objects into usable data
 * also used for creating the data and saving / loading it*/
public interface IDataFactory<D extends IData>{

        D create();

        void save(D data, String key, CompoundNBT tag);

        void read(D data, String key, CompoundNBT tag);

        void saveUpdate(D data, PacketBuffer buf);

        void readUpdate(D data, PacketBuffer buf);

        default boolean canConvert(Class returnType){
                return false;
        }

        default void convert(D data, Object obj){}
}