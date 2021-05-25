package sonar.logistics.server.cables;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICableListener {

    void onCableAdded(World world, BlockPos pos, EnumCableTypes cableType);


    void onCableRemoved(World world, BlockPos pos, EnumCableTypes cableType);


    /** the global id of the new network.
     * @param globalID the global id of the new created network*/
    void onCableDataCreated(int globalID, EnumCableTypes cableType);


    /** called when there are no longer any cables on the network
     * @param globalID the global id of the network which has been deleted */
    void onCableDataDeleted(int globalID, EnumCableTypes cableType);



    /** called when a cable or cables have been added to the network
     * @param globalID the global id of the network which has grown */
    void onCableDataGrown(int globalID, EnumCableTypes cableType);


    /** called when a cable or cables have been removed from the network
     * @param globalID the global id of the network which has shrunk */
    void onCableDataShrunk(int globalID, EnumCableTypes cableType);


    /** called when two networks have been combined, resulting in one.
     * @param globalID the global id of the network which all merged networks are now connected to
     * @param mergedID the global id of the network which has now been removed */
    void onCableDataMerged(int globalID, int mergedID, EnumCableTypes cableType);

}
