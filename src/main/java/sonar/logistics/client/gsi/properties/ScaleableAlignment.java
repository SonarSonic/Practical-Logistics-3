package sonar.logistics.client.gsi.properties;

import net.minecraft.util.math.Vec3d;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;

public class ScaleableAlignment {

    private Vec3d sizingPercentage = ScaleableRenderHelper.DEFAULT_SCALE_PERCENTAGE;
    private Vec3d alignmentPercentage = ScaleableRenderHelper.DEFAULT_TRANSLATION_PERCENTAGE;

    private Vec3d sizing;
    private Vec3d alignment;

    private Vec3d renderSizing;
    private Vec3d renderAlignment;

    public void setSizingFromPercentage(Vec3d sizingPercentage, Vec3d alignmentPercentage){
        this.sizingPercentage = sizingPercentage;
        this.alignmentPercentage = alignmentPercentage;
    }

    /*
    public void setSizingFromScale(Vec3d scalePercentage, Vec3d translationPercentage){
        this.containerTranslationPercentage = DisplayVectorHelper.percentageFromScale(translationPercentage, host.getSizing());
        this.containerSizingPercentage = DisplayVectorHelper.percentageFromScale(scalePercentage, host.getSizing());
    }
     */

    public void build(Vec3d alignment, Vec3d maxSizing){
        this.sizing = DisplayVectorHelper.scaleFromPercentage(sizingPercentage, maxSizing);
        this.alignment = DisplayVectorHelper.scaleFromPercentage(alignmentPercentage, maxSizing).add(alignment);
        this.renderSizing = sizing;
        this.renderAlignment = this.alignment;
    }

    public void build(Vec3d alignment, Vec3d maxSizing, ScaleableStyling properties){
        this.sizing = DisplayVectorHelper.scaleFromPercentage(sizingPercentage, maxSizing);
        this.alignment = DisplayVectorHelper.scaleFromPercentage(alignmentPercentage, maxSizing).add(alignment);
        this.renderSizing = properties.getRenderSizing(this);
        this.renderAlignment = properties.getRenderAlignment(this).add(alignment);
    }



    public Vec3d getSizing(){
        return sizing;
    }

    public Vec3d getAlignment(){
        return alignment;
    }

    public Vec3d getRenderSizing(){
        return renderSizing;
    }

    public Vec3d getRenderAlignment(){
        return renderAlignment;
    }

}