package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;

public abstract class ScaleableElement extends ScaleableAbstract implements IRenderableElement {

    public Vec3d elementAlignment;
    public Vec3d elementScaling;

    public ScaleableElement(IScaleable host) {
        super(host);
    }

    @Override
    public void updateScaleable() {
        this.containerTranslation = DisplayVectorHelper.scaleFromPercentage(containerTranslationPercentage, host.getSizing());
        this.containerSizing = DisplayVectorHelper.scaleFromPercentage(containerSizingPercentage, host.getSizing());

        this.elementAlignment = DisplayVectorHelper.getHostAlignment(this);
        this.elementScaling = DisplayVectorHelper.scaleFromUnscaledSize(unscaledSizing(), containerSizing, 1);
    }

    @Override
    public Vec3d unscaledSizing(){
        return new Vec3d(1, 1, 1);
    }
}
