package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.util.math.Vec3d;

public interface IRenderableElement {

    boolean canRender();

    void render();

    Vec3d unscaledSizing();

}
