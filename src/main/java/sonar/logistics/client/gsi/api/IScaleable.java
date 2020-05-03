package sonar.logistics.client.gsi.api;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.properties.ScaleableAlignment;

public interface IScaleable {

    ScaleableAlignment getAlignment();

    void build(Vec3d alignment, Vec3d maxSizing);

}
