package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.client.nodes.AdvancedNodeGraph;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.util.network.EnumSyncType;

import java.util.Objects;

public class PinOutputAddress extends DataAddress {

    public int nodeGraphID;
    public int pinID;

    public PinOutputAddress() {}

    public PinOutputAddress(int nodeGraphID, int pinID) {
        this.nodeGraphID = nodeGraphID;
        this.pinID = pinID;
    }

    public int getPinID() {
        return pinID;
    }

    @Override
    public void read(PacketBuffer buffer) {
        pinID = buffer.readInt();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(pinID);
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        nodeGraphID = nbt.getInt("graphID");
        pinID = nbt.getInt("pinID");
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putLong("graphID", nodeGraphID);
        nbt.putLong("pinID", pinID);
        return nbt;
    }

    @Override
    public String getRegistryName() {
        return "pin";
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        environment.reset();
        AdvancedNodeGraph nodeGraph = DataManager.INSTANCE.nodeGraphMap.get(nodeGraphID);
        environment.data = nodeGraph == null ? null : nodeGraph.getInternalPinData(pinID);
        return environment.data != null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PinOutputAddress){
            return ((PinOutputAddress) obj).pinID == pinID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName(), nodeGraphID, pinID);
    }

    @Override
    public String toString() {
        return String.format("Graph: (%s) Pin: (%s)", nodeGraphID, pinID);
    }
}
