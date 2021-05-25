package sonar.logistics.client.gsi.style.properties;

import imgui.ImColor;

public class ColourProperty {

    public int rgba;
    public transient int argb = -1;

    public ColourProperty(int rgba){
        this.rgba = rgba;
    }

    public ColourProperty(int r, int g, int b){
        this(r, g, b, 255);
    }

    public ColourProperty(int r, int g, int b, int a){
        this.rgba = getRGBA(r, g, b, a);
    }

    public ColourProperty(float[] colour){
        this((int)(colour[0]*255), (int)(colour[1]*255), (int)(colour[2]*255), (int)(colour[3]*255));
    }

    public float[] asFloatArray(){
        return new float[]{getRed()/255F, getGreen()/255F, getBlue()/255F, getAlpha()/255F};
    }

    public int getRed(){
        return getRed(rgba);
    }

    public int getGreen(){
        return getGreen(rgba);
    }

    public int getBlue(){
        return getBlue(rgba);
    }

    public int getAlpha(){
        return getAlpha(rgba);
    }

    public int getRGBA(){
        return rgba;
    }

    public int getARGB(){
            int red = (rgba >> 16) & 0xFF;
            int green = (rgba >> 8) & 0xFF;
            int blue = (rgba >> 0) & 0xFF;
            int alpha = (rgba >> 24) & 0xff;
            argb = ImColor.intToColor(red, green, blue, alpha);

        return argb;
    }

    public static int getRed(int rgba) {
        return (rgba >> 16) & 0xFF;
    }

    public static int getGreen(int rgba) {
        return (rgba >> 8) & 0xFF;
    }

    public static int getBlue(int rgba) {
        return (rgba >> 0) & 0xFF;
    }

    public static int getAlpha(int rgba) {
        return (rgba >> 24) & 0xff;
    }

    public static int getRGBA(int r, int g, int b, int a){
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF) << 0);
    }

    public ColourProperty copy(){
        return new ColourProperty(rgba);
    }

    @Override
    public int hashCode() {
        return rgba;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ColourProperty){
            return ((ColourProperty) obj).rgba == rgba;
        }
        return super.equals(obj);
    }
}
