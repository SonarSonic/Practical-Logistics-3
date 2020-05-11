package sonar.logistics.client.gsi.properties;

import net.minecraft.util.math.Vec3d;

/**convenience class, a simple float value which can be sized by percentage or be an absolute size*/
public class BorderProperty {

    public double value;
    public boolean percentage;

    public BorderProperty(double value, boolean percentage){
        this.value = value;
        this.percentage = percentage;
    }

    public double getRenderSize(double maxSize){
        if(!percentage){
            return value;
        }
        return maxSize * value;
    }

}