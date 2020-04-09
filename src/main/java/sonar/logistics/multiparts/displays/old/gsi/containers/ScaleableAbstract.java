package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;

import javax.annotation.Nullable;

public abstract class ScaleableAbstract implements IScaleable {

    public final IScaleable host;
    public Vec3d containerTranslationPercentage;
    public Vec3d containerSizingPercentage;

    public Vec3d containerTranslation;
    public Vec3d containerSizing;

    public ScaleableAbstract(IScaleable host){
        this.host = host;
    }

    public void setSizingFromScale(Vec3d scalePercentage, Vec3d translationPercentage){
        this.containerTranslationPercentage = DisplayVectorHelper.percentageFromScale(translationPercentage, host.getSizing());
        this.containerSizingPercentage = DisplayVectorHelper.percentageFromScale(scalePercentage, host.getSizing());
        updateScaleable();
    }

    public void setSizingFromPercentage(Vec3d scalePercentage, Vec3d translationPercentage){
        this.containerTranslationPercentage = scalePercentage;
        this.containerSizingPercentage = translationPercentage;
        updateScaleable();
    }

    @Nullable
    @Override
    public IScaleable getHost() {
        return host;
    }

    @Override
    public Vec3d getTranslation() {
        return containerTranslation;
    }

    @Override
    public Vec3d getSizing() {
        return containerSizing;
    }

}
