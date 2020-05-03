package sonar.logistics.client.gsi.scaleables;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.api.IScaleable;
import sonar.logistics.client.gsi.properties.ScaleableAlignment;

public abstract class AbstractScaleable implements IScaleable {

    public ScaleableAlignment alignment = new ScaleableAlignment();

    public AbstractScaleable() {}

    public void setAlignment(ScaleableAlignment alignment){
        this.alignment = alignment;
    }

    @Override
    public void build(Vec3d alignment, Vec3d maxSizing) {
        this.alignment.build(alignment, maxSizing);
    }

    @Override
    public ScaleableAlignment getAlignment() {
        return alignment;
    }

}
