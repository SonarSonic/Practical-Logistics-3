package sonar.logistics.client.gsi.properties;

import net.minecraft.util.math.Vec3d;
import org.joml.Rectangled;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

public class ScaleableAlignment {

    private Vec3d sizingPercentage = ScaleableRenderHelper.DEFAULT_SIZING_PERCENTAGE;
    private Vec3d alignmentPercentage = ScaleableRenderHelper.DEFAULT_ALIGNMENT_PERCENTAGE;

    private Vec3d sizing;
    private Vec3d alignment;

    private Vec3d renderSizing;
    private Vec3d renderAlignment;

    public void setAlignmentPercentages(Vec3d alignmentPercentage, Vec3d sizingPercentage){
        this.alignmentPercentage = alignmentPercentage;
        this.sizingPercentage = sizingPercentage;
    }

    public void build(Vec3d alignment, Vec3d maxSizing){
        this.sizing = maxSizing.mul(sizingPercentage);
        this.alignment = maxSizing.mul(alignmentPercentage).add(alignment);
        this.renderSizing = sizing;
        this.renderAlignment = this.alignment;
    }

    public void build(Vec3d alignment, Vec3d maxSizing, ScaleableStyling properties){
        this.sizing = maxSizing.mul(sizingPercentage);
        this.alignment = maxSizing.mul(alignmentPercentage).add(alignment);
        this.renderSizing = properties.getRenderSizing(this);
        this.renderAlignment = properties.getRenderAlignment(this).add(this.alignment);
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