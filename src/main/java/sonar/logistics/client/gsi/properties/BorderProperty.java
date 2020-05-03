package sonar.logistics.client.gsi.properties;

import net.minecraft.util.math.Vec3d;

/**convenience class, a simple float value which can be sized by percentage or be an absolute size*/
public class BorderProperty {

    public float value;
    public boolean percentage;

    public BorderProperty(float value, boolean percentage){
        this.value = value;
        this.percentage = percentage;
    }

    public float getRenderSize(float maxSize){
        if(!percentage){
            return value;
        }
        return maxSize * value;
    }

}