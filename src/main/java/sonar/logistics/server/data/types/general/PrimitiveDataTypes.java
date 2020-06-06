package sonar.logistics.server.data.types.general;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.api.IDataFactory;

public class PrimitiveDataTypes {


    public static class BooleanData extends GeneralData<Boolean> implements IData {

        public BooleanData(boolean data){
            super(data);
        }

        @Override
        public boolean hasUpdated(Boolean newData, Boolean currentData){
            return newData.booleanValue() != currentData.booleanValue();
        }

    }

    public static class BooleanDataFactory implements IDataFactory<BooleanData> {

        @Override
        public BooleanData create() {
            return new BooleanData(false);
        }

        @Override
        public void save(BooleanData data, String key, CompoundNBT tag) {
            tag.putBoolean(key, data.data);
        }

        @Override
        public void read(BooleanData data, String key, CompoundNBT tag) {
            data.data = tag.getBoolean(key);
        }

        @Override
        public void saveUpdate(BooleanData data, PacketBuffer buf) {
            buf.writeBoolean(data.data);
        }

        @Override
        public void readUpdate(BooleanData data, PacketBuffer buf) {
            data.data = buf.readBoolean();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == Boolean.class;
        }

        @Override
        public void convert(BooleanData data, Object obj){
            if(obj instanceof Boolean){
                data.setData((Boolean) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////

    public static class IntegerData extends GeneralData<Integer> implements IData {

        public IntegerData(int data){
            super(data);
        }

        @Override
        public boolean hasUpdated(Integer newData, Integer currentData){
            return newData.intValue() != currentData.intValue();
        }

    }

    public static class IntegerDataFactory implements IDataFactory<IntegerData> {

        @Override
        public IntegerData create() {
            return new IntegerData(0);
        }

        @Override
        public void save(IntegerData data, String key, CompoundNBT tag) {
            tag.putInt(key, data.data);
        }

        @Override
        public void read(IntegerData data, String key, CompoundNBT tag) {
            data.data = tag.getInt(key);
        }

        @Override
        public void saveUpdate(IntegerData data, PacketBuffer buf) {
            buf.writeInt(data.data);
        }

        @Override
        public void readUpdate(IntegerData data, PacketBuffer buf) {
            data.data = buf.readInt();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == Integer.class || returnType == Short.class || returnType == Byte.class;
        }

        @Override
        public void convert(IntegerData data, Object obj){
            if(obj instanceof Integer || obj instanceof Short || obj instanceof Byte){
                data.setData(((Number) obj).intValue());
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////

    public static class LongData extends GeneralData<Long> implements IData {

        public LongData(long data){
            super(data);
        }

        @Override
        public boolean hasUpdated(Long newData, Long currentData){
            return newData.longValue() != currentData.longValue();
        }

    }

    public static class LongDataFactory implements IDataFactory<LongData> {

        @Override
        public LongData create() {
            return new LongData(0);
        }

        @Override
        public void save(LongData data, String key, CompoundNBT tag) {
            tag.putLong(key, data.data);
        }

        @Override
        public void read(LongData data, String key, CompoundNBT tag) {
            data.data = tag.getLong(key);
        }

        @Override
        public void saveUpdate(LongData data, PacketBuffer buf) {
            buf.writeLong(data.data);
        }

        @Override
        public void readUpdate(LongData data, PacketBuffer buf) {
            data.data = buf.readLong();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == Long.class;
        }

        @Override
        public void convert(LongData data, Object obj){
            if(obj instanceof Long){
                data.setData((Long) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////

    public static class DoubleData extends GeneralData<Double> implements IData {

        public DoubleData(double data){
            super(data);
        }

        @Override
        public boolean hasUpdated(Double newData, Double currentData){
            return newData.doubleValue() != currentData.doubleValue();
        }

    }

    public static class DoubleDataFactory implements IDataFactory<DoubleData> {

        @Override
        public DoubleData create() {
            return new DoubleData(0);
        }

        @Override
        public void save(DoubleData data, String key, CompoundNBT tag) {
            tag.putDouble(key, data.data);
        }

        @Override
        public void read(DoubleData data, String key, CompoundNBT tag) {
            data.data = tag.getDouble(key);
        }

        @Override
        public void saveUpdate(DoubleData data, PacketBuffer buf) {
            buf.writeDouble(data.data);
        }

        @Override
        public void readUpdate(DoubleData data, PacketBuffer buf) {
            data.data = buf.readDouble();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == Double.class;
        }

        @Override
        public void convert(DoubleData data, Object obj){
            if(obj instanceof Double){
                data.setData((Double) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

    ////

    public static class FloatData extends GeneralData<Float> implements IData {

        public FloatData(float data){
            super(data);
        }

        @Override
        public boolean hasUpdated(Float newData, Float currentData){
            return newData.floatValue() != currentData.floatValue();
        }

    }

    public static class FloatDataFactory implements IDataFactory<FloatData> {

        @Override
        public FloatData create() {
            return new FloatData(0);
        }

        @Override
        public void save(FloatData data, String key, CompoundNBT tag) {
            tag.putFloat(key, data.data);
        }

        @Override
        public void read(FloatData data, String key, CompoundNBT tag) {
            data.data = tag.getFloat(key);
        }

        @Override
        public void saveUpdate(FloatData data, PacketBuffer buf) {
            buf.writeFloat(data.data);
        }

        @Override
        public void readUpdate(FloatData data, PacketBuffer buf) {
            data.data = buf.readFloat();
        }

        @Override
        public boolean canConvert(Class returnType){
            return returnType == Float.class;
        }

        @Override
        public void convert(FloatData data, Object obj){
            if(obj instanceof Float){
                data.setData((Float) obj);
                return;
            }
            throw new NullPointerException("INVALID CONVERSION: " + this + " given " + obj);
        }
    }

}
