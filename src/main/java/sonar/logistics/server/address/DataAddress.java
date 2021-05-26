package sonar.logistics.server.address;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.util.registry.Registries;
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
        source = Registries.getAddressRegistry().read(nbt, "source", EnumSyncType.SAVE);
        method = Registries.getMethodRegistry().read(nbt, "method", EnumSyncType.SAVE);
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        Registries.getAddressRegistry().write(source, nbt, "source", EnumSyncType.SAVE);
        Registries.getMethodRegistry().write(method, nbt, "method", EnumSyncType.SAVE);
        return nbt;
    }

    @Override
    public void read(PacketBuffer buffer) {
        source = Registries.getAddressRegistry().read(buffer);
        method = Registries.getMethodRegistry().read(buffer);
    }

    @Override
    public void write(PacketBuffer buffer) {
        Registries.getAddressRegistry().write(source, buffer);
        Registries.getMethodRegistry().write(method, buffer);
    }

    @Override
    public String getRegistryName() {
        return "data";
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
        return Objects.hash(getRegistryName(), source, method);
    }

    @Override
    public String toString() {
        return String.format("Source: (%s), Method (%s)", source.toString(), method.getRegistryName().toString());
    }

}
