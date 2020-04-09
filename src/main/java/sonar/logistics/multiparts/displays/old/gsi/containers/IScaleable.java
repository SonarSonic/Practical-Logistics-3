package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public interface IScaleable {

    @Nullable
    default IScaleable getHost(){
        return null;
    }

    Vec3d getTranslation();

    Vec3d getSizing();

    void updateScaleable();
}
