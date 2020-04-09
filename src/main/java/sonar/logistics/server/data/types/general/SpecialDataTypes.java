package sonar.logistics.server.data.types.general;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;

public class SpecialDataTypes {

    ////

    public static class StringData extends GeneralData<String> implements IData {

        public StringData(String data){
            super(data);
        }

        @Override
        public boolean hasUpdated(String newData, String currentData){
            return !currentData.equals(newData);
        }

    }

    public static class StringDataFactory implements IDataFactory<StringData> {

        @Override
        public StringData create() {
            return new StringData("");
        }

        @Override
        public void save(StringData data, String key, CompoundNBT tag) {
            tag.putString(key, data.data);
        }

        @Override
        public void read(StringData data, String key, CompoundNBT tag) {
            data.data = tag.getString(key);
        }

        @Override
        public void saveUpdate(StringData data, PacketBuffer buf) {
            buf.writeString(data.data);
        }

        @Override
        public void readUpdate(StringData data, PacketBuffer buf) {
            data.data = buf.readString();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == String.class;
        }

        @Override
        public void convert(StringData data, Object obj){
            if(obj instanceof String){
                data.setData((String) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////


    public static class ItemStackData extends GeneralData<ItemStack> implements IData {

        public ItemStackData(ItemStack data){
            super(data);
        }

        @Override
        public boolean hasUpdated(ItemStack newData, ItemStack currentData){
            return !ItemStack.areItemStacksEqual(newData, currentData) || !ItemStack.areItemStackTagsEqual(newData, currentData);
        }

    }

    public static class ItemStackDataFactory implements IDataFactory<ItemStackData> {

        @Override
        public ItemStackData create() {
            return new ItemStackData(ItemStack.EMPTY);
        }

        @Override
        public void save(ItemStackData data, String key, CompoundNBT tag) {
            tag.put(key, data.data.write(new CompoundNBT()));
        }

        @Override
        public void read(ItemStackData data, String key, CompoundNBT tag) {
            data.data = ItemStack.read(tag.getCompound(key));
        }

        @Override
        public void saveUpdate(ItemStackData data, PacketBuffer buf) {
            buf.writeItemStack(data.data);
        }

        @Override
        public void readUpdate(ItemStackData data, PacketBuffer buf) {
            data.data = buf.readItemStack();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == ItemStack.class;
        }

        @Override
        public void convert(ItemStackData data, Object obj){
            if(obj instanceof ItemStack){
                data.setData((ItemStack) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////

    public static class NBTData extends GeneralData<CompoundNBT> implements IData {

        public NBTData(CompoundNBT data){
            super(data);
        }

        @Override
        public boolean hasUpdated(CompoundNBT newData, CompoundNBT currentData){
            return !newData.equals(currentData);
        }

    }

    public static class NBTDataFactory implements IDataFactory<NBTData> {

        @Override
        public NBTData create() {
            return new NBTData(new CompoundNBT());
        }

        @Override
        public void save(NBTData data, String key, CompoundNBT tag) {
            tag.put(key, data.data);
        }

        @Override
        public void read(NBTData data, String key, CompoundNBT tag) {
            data.data = tag.getCompound(key);
        }

        @Override
        public void saveUpdate(NBTData data, PacketBuffer buf) {
            buf.writeCompoundTag(data.data);
        }

        @Override
        public void readUpdate(NBTData data, PacketBuffer buf) {
            data.data = buf.readCompoundTag();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == CompoundNBT.class;
        }

        @Override
        public void convert(NBTData data, Object obj){
            if(obj instanceof CompoundNBT){
                data.setData((CompoundNBT) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }
}
