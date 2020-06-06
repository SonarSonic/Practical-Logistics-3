package sonar.logistics.client.gsi.style.properties;

public class ColourProperty {

    public int rgba;

    public ColourProperty(int rgba){
        this.rgba = rgba;
    }

    public ColourProperty(int r, int g, int b){
        this(r, g, b, 255);
    }

    public ColourProperty(int r, int g, int b, int a){
        this.rgba = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF) << 0);
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
