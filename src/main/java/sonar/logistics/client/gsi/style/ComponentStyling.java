package sonar.logistics.client.gsi.style;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.INBTSyncable;
import sonar.logistics.util.network.NBTUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ComponentStyling implements INBTSyncable {

    protected Boolean absolute;

    ///TODO ALIGNMENT? //TODO RATIOS?

    ///positioning
    protected Integer zLayer;
    protected LengthProperty xPos;
    protected LengthProperty yPos;

    ////sizing
    protected LengthProperty width;
    protected LengthProperty height;

    ///offsets
    protected LengthProperty marginWidth;
    protected LengthProperty marginHeight;
    protected LengthProperty paddingWidth;
    protected LengthProperty paddingHeight;
    protected LengthProperty borderWidth;
    protected LengthProperty borderHeight;

    ///component colours
    protected ColourProperty outerBackgroundColour;
    protected ColourProperty innerBackgroundColour;
    protected ColourProperty borderColour;

    ///text colours
    protected ColourProperty enabledTextColour;
    protected ColourProperty hoveredTextColour;
    protected ColourProperty disabledTextColour;

    public ComponentStyling alterStyling(Consumer<ComponentStyling> alter){
        alter.accept(this);
        return this;
    }

    public ComponentStyling setSizing(float xPos, float yPos, float width, float height, Unit type){
        return setSizing(new LengthProperty(type, xPos), new LengthProperty(type, yPos), new LengthProperty(type, width), new LengthProperty(type, height));
    }

    public ComponentStyling setSizing(LengthProperty xPos, LengthProperty yPos, LengthProperty width, LengthProperty height){
        setXPos(xPos);
        setYPos(yPos);
        setWidth(width);
        setHeight(height);
        return this;
    }

    ///

    @Nonnull
    public Boolean isAbsolute() {
        return absolute == null ? DefaultStyling.INSTANCE.isAbsolute() : absolute;
    }

    public void setAbsolute(@Nonnull Boolean absolute) {
        this.absolute = DefaultStyling.INSTANCE.isAbsolute().equals(absolute) ? null : absolute;
    }

    public Integer getZLayer() {
        return zLayer == null ? DefaultStyling.INSTANCE.getZLayer() : zLayer;
    }

    public void setZLayer(Integer zLayer) {
        this.zLayer = DefaultStyling.INSTANCE.getZLayer().equals(zLayer) ? null : zLayer;
    }

    ///

    @Nonnull
    public LengthProperty getXPos() {
        return xPos == null ? DefaultStyling.INSTANCE.getXPos() : xPos;
    }

    public void setXPos(@Nonnull LengthProperty xPos) {
        this.xPos = xPos;
    }

    @Nonnull
    public LengthProperty getYPos() {
        return yPos == null ? DefaultStyling.INSTANCE.getYPos() : yPos;
    }

    public void setYPos(@Nonnull LengthProperty yPos) {
        this.yPos = DefaultStyling.INSTANCE.getYPos().equals(yPos) ? null : yPos;
    }

    ///

    @Nonnull
    public LengthProperty getWidth() {
        return width == null ? DefaultStyling.INSTANCE.getWidth() : width;
    }

    public void setWidth(@Nonnull LengthProperty width) {
        this.width = DefaultStyling.INSTANCE.getWidth().equals(width) ? null : width;
    }

    @Nonnull
    public LengthProperty getHeight() {
        return height == null ? DefaultStyling.INSTANCE.getHeight() : height;
    }

    public void setHeight(@Nonnull LengthProperty height) {
        this.height = DefaultStyling.INSTANCE.getHeight().equals(height) ? null : height;
    }

    ///

    @Nonnull
    public LengthProperty getMarginWidth() {
        return marginWidth == null ? DefaultStyling.INSTANCE.getMarginWidth() : marginWidth;
    }

    public void setMarginWidth(@Nullable LengthProperty marginWidth) {
        this.marginWidth = DefaultStyling.INSTANCE.getMarginWidth().equals(marginWidth) ? null : marginWidth;
    }

    @Nonnull
    public LengthProperty getMarginHeight() {
        return marginHeight == null ? DefaultStyling.INSTANCE.getMarginHeight() : marginHeight;
    }

    public void setMarginHeight(@Nullable LengthProperty marginHeight) {
        this.marginHeight = DefaultStyling.INSTANCE.getMarginHeight().equals(marginHeight) ? null : marginHeight;
    }

    @Nonnull
    public LengthProperty getPaddingWidth() {
        return paddingWidth == null ? DefaultStyling.INSTANCE.getPaddingWidth() : paddingWidth;
    }

    public void setPaddingWidth(@Nullable LengthProperty paddingWidth) {
        this.paddingWidth = DefaultStyling.INSTANCE.getPaddingWidth().equals(paddingWidth) ? null : paddingWidth;
    }

    @Nonnull
    public LengthProperty getPaddingHeight() {
        return paddingHeight == null ? DefaultStyling.INSTANCE.getPaddingHeight() : paddingHeight;
    }

    public void setPaddingHeight(@Nullable LengthProperty paddingHeight) {
        this.paddingHeight = DefaultStyling.INSTANCE.getPaddingHeight().equals(paddingHeight) ? null : paddingHeight;
    }

    @Nonnull
    public LengthProperty getBorderWidth() {
        return borderWidth == null ? DefaultStyling.INSTANCE.getBorderWidth() : borderWidth;
    }

    public void setBorderWidth(@Nullable LengthProperty borderWidth) {
        this.borderWidth = DefaultStyling.INSTANCE.getBorderWidth().equals(borderWidth) ? null : borderWidth;
    }

    @Nonnull
    public LengthProperty getBorderHeight() {
        return borderHeight == null ? DefaultStyling.INSTANCE.getBorderHeight() : borderHeight;
    }

    public void setBorderHeight(@Nullable LengthProperty borderHeight) {
        this.borderHeight = DefaultStyling.INSTANCE.getBorderHeight().equals(borderHeight) ? null : borderHeight;
    }

    ///

    @Nonnull
    public ColourProperty getOuterBackgroundColour() {
        return outerBackgroundColour == null ? DefaultStyling.INSTANCE.getOuterBackgroundColour() : outerBackgroundColour;
    }

    public void setOuterBackgroundColour(@Nullable ColourProperty outerBackgroundColour) {
        this.outerBackgroundColour = DefaultStyling.INSTANCE.getOuterBackgroundColour().equals(outerBackgroundColour) ? null : outerBackgroundColour;
    }

    @Nonnull
    public ColourProperty getInnerBackgroundColour() {
        return innerBackgroundColour == null ? DefaultStyling.INSTANCE.getInnerBackgroundColour() : innerBackgroundColour;
    }

    public void setInnerBackgroundColour(@Nullable ColourProperty innerBackgroundColour) {
        this.innerBackgroundColour = DefaultStyling.INSTANCE.getInnerBackgroundColour().equals(innerBackgroundColour) ? null : innerBackgroundColour;
    }

    @Nonnull
    public ColourProperty getBorderColour() {
        return borderColour == null ? DefaultStyling.INSTANCE.getBorderColour() : borderColour;
    }

    public void setBorderColour(@Nonnull ColourProperty borderColour) {
        this.borderColour = DefaultStyling.INSTANCE.getBorderColour().equals(borderColour) ? null : borderColour;
    }

    ///

    @Nonnull
    public ColourProperty getEnabledTextColour() {
        return enabledTextColour == null ? DefaultStyling.INSTANCE.getEnabledTextColour() : enabledTextColour;
    }

    public void setEnabledTextColour(@Nullable ColourProperty enabledTextColour) {
        this.enabledTextColour = DefaultStyling.INSTANCE.getEnabledTextColour().equals(enabledTextColour) ? null : enabledTextColour;
    }

    @Nonnull
    public ColourProperty getHoveredTextColour() {
        return hoveredTextColour == null ? DefaultStyling.INSTANCE.getHoveredTextColour() : hoveredTextColour;
    }

    public void setHoveredTextColour(@Nullable ColourProperty hoveredTextColour) {
        this.hoveredTextColour = DefaultStyling.INSTANCE.getHoveredTextColour().equals(hoveredTextColour) ? null : hoveredTextColour;
    }

    @Nonnull
    public ColourProperty getDisabledTextColour() {
        return disabledTextColour == null ? DefaultStyling.INSTANCE.getDisabledTextColour() : disabledTextColour;
    }

    public void setDisabledTextColour(@Nullable ColourProperty disabledTextColour) {
        this.disabledTextColour = DefaultStyling.INSTANCE.getDisabledTextColour().equals(disabledTextColour) ? null : disabledTextColour;
    }

    //// SAVING

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        if(nbt.contains("sizing")){
            byte[] sizingTypes = nbt.getByteArray("type1");
            float[] sizingValues = NBTUtils.readFloatArray(nbt, "sizing");
            loadSizingFromArrays(sizingTypes, sizingValues);
        }
        if(nbt.contains("offset")){
            byte[] offsetTypes = nbt.getByteArray("type2");
            float[] offsetValues = NBTUtils.readFloatArray(nbt, "offset");
            loadOffsetFromArrays(offsetTypes, offsetValues);
        }
        if(nbt.contains("colour3")){
            int[] colours = nbt.getIntArray("colour3");
            loadColoursFromIntArray(colours);
        }
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        if(hasCustomSizings()){
            nbt.putByteArray("type1", getSizingTypesAsByteArray());
            NBTUtils.writeFloatArray(nbt, getSizingValuesAsFloatArray(), "sizing");
        }
        if(hasCustomOffsets()){
            nbt.putByteArray("type2", getOffsetTypesAsByteArray());
            NBTUtils.writeFloatArray(nbt, getOffsetValuesAsFloatArray(), "offset");
        }
        if(hasCustomColours()){
            nbt.putIntArray("colour3", getColoursAsIntArray());
        }
        return nbt;
    }

    public boolean hasCustomSizings(){
        return zLayer!= null || xPos != null || yPos != null || width != null || height != null;
    }

    public byte[] getSizingTypesAsByteArray(){
        return new byte[]{
                0,
                (byte) getXPos().unitType.ordinal(),
                (byte) getYPos().unitType.ordinal(),
                (byte) getWidth().unitType.ordinal(),
                (byte) getHeight().unitType.ordinal(),
        };
    }

    public float[] getSizingValuesAsFloatArray(){
        return new float[]{
                getZLayer().floatValue(),
                getXPos().value,
                getYPos().value,
                getWidth().value,
                getHeight().value
        };
    }

    public void loadSizingFromArrays(byte[] types, float[] values){
        if(getZLayer() != values[0]){
            setZLayer((int)values[0]);
        }
        if(getXPos().unitType.ordinal() != types[1] || getXPos().value != values[1]){
            setXPos(new LengthProperty(Unit.values()[types[1]], values[1]));
        }
        if(getYPos().unitType.ordinal() != types[2] || getYPos().value != values[2]){
            setYPos(new LengthProperty(Unit.values()[types[2]], values[2]));
        }
        if(getWidth().unitType.ordinal() != types[3] || getWidth().value != values[3]){
            setWidth(new LengthProperty(Unit.values()[types[3]], values[3]));
        }
        if(getHeight().unitType.ordinal() != types[4] || getHeight().value != values[4]){
            setHeight(new LengthProperty(Unit.values()[types[4]], values[4]));
        }
    }

    public boolean hasCustomOffsets(){
        return marginWidth != null || marginHeight != null || paddingWidth != null || paddingHeight != null || borderWidth != null || borderHeight != null;
    }

    public byte[] getOffsetTypesAsByteArray(){
        return new byte[]{
                (byte) getMarginWidth().unitType.ordinal(),
                (byte) getMarginHeight().unitType.ordinal(),
                (byte) getPaddingWidth().unitType.ordinal(),
                (byte) getPaddingHeight().unitType.ordinal(),
                (byte) getBorderWidth().unitType.ordinal(),
                (byte) getBorderHeight().unitType.ordinal(),
        };
    }

    public float[] getOffsetValuesAsFloatArray(){
        return new float[]{
                getMarginWidth().value,
                getMarginHeight().value,
                getPaddingWidth().value,
                getPaddingHeight().value,
                getBorderWidth().value,
                getBorderHeight().value
        };
    }

    public void loadOffsetFromArrays(byte[] types, float[] values){
        if(getMarginWidth().unitType.ordinal() != types[0] || getMarginWidth().value != values[0]){
            setMarginWidth(new LengthProperty(Unit.values()[types[0]], values[0]));
        }
        if(getMarginHeight().unitType.ordinal() != types[1] || getMarginHeight().value != values[1]){
            setMarginHeight(new LengthProperty(Unit.values()[types[1]], values[1]));
        }
        if(getPaddingWidth().unitType.ordinal() != types[2] || getPaddingWidth().value != values[2]){
            setPaddingWidth(new LengthProperty(Unit.values()[types[2]], values[2]));
        }
        if(getPaddingHeight().unitType.ordinal() != types[3] || getPaddingHeight().value != values[3]){
            setPaddingHeight(new LengthProperty(Unit.values()[types[3]], values[3]));
        }
        if(getBorderWidth().unitType.ordinal() != types[4] || getBorderWidth().value != values[4]){
            setBorderWidth(new LengthProperty(Unit.values()[types[4]], values[4]));
        }
        if(getBorderHeight().unitType.ordinal() != types[5] || getBorderHeight().value != values[4]){
            setBorderHeight(new LengthProperty(Unit.values()[types[5]], values[5]));
        }
    }

    public boolean hasCustomColours(){
        return outerBackgroundColour != null || innerBackgroundColour != null || borderColour != null || enabledTextColour != null || hoveredTextColour != null || disabledTextColour != null;
    }

    public int[] getColoursAsIntArray(){
        return new int[]{
                getOuterBackgroundColour().rgba,
                getInnerBackgroundColour().rgba,
                getBorderColour().rgba,
                getEnabledTextColour().rgba,
                getHoveredTextColour().rgba,
                getDisabledTextColour().rgba,
        };
    }

    public void loadColoursFromIntArray(int[] array){
        if(array[0] != getOuterBackgroundColour().rgba){
            setOuterBackgroundColour(new ColourProperty(array[0]));
        }
        if(array[1] != getInnerBackgroundColour().rgba){
            setInnerBackgroundColour(new ColourProperty(array[1]));
        }
        if(array[2] != getBorderColour().rgba){
            setBorderColour(new ColourProperty(array[2]));
        }
        if(array[3] != getEnabledTextColour().rgba){
            setEnabledTextColour(new ColourProperty(array[3]));
        }
        if(array[4] != getHoveredTextColour().rgba){
            setHoveredTextColour(new ColourProperty(array[4]));
        }
        if(array[5] != getDisabledTextColour().rgba){
            setDisabledTextColour(new ColourProperty(array[5]));
        }
    }
    ///

}