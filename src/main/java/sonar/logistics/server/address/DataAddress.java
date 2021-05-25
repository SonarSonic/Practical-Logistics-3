package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.methods.MethodRegistry;
import sonar.logistics.util.network.EnumSyncType;

import java.util.Objects;

//TODO cool idea, you could have address to other data, which are then used to make more data!!!
public class DataAddress extends Address {

    public Address source;
    public Method method;

    public DataAddress() {}

    public DataAddress(Address source, Method method) {
        this.source = source;
        this.method = method;
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        CompoundNBT subTag = nbt.getCompound("source");
        source = Address.fromNBT(subTag);
        method = MethodRegistry.getMethodFromIdentifier(nbt.getString("method"));
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        CompoundNBT subTag = new CompoundNBT();
        Address.toNBT(source, subTag);
        nbt.put("source", subTag);
        nbt.putString("method", method.getIdentifier());
        return nbt;
    }

    @Override
    public void read(PacketBuffer buffer) {
        source = Address.fromPacketBuffer(buffer);
        method = MethodRegistry.getMethodFromIdentifier(buffer.readString());
    }

    @Override
    public void write(PacketBuffer buffer) {
        Address.toPacketBuffer(source, buffer);
        buffer.writeString(method.getIdentifier());
    }

    @Override
    public int getType() {
        return DATA_ADDRESS;
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        environment.reset();
        environment.data = DataManager.getData(this);
        return environment.data != null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataAddress){
            DataAddress dataAddress = (DataAddress) obj;
            return Objects.equals(dataAddress.source, source) && Objects.equals(dataAddress.method, method);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(DATA_ADDRESS, source, method);
    }

    @Override
    public String toString() {
        return String.format("Source: (%s), Method (%s)", source.toString(), method.getIdentifier().toString());
    }

}
