package sonar.logistics.client.gsi.components.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.api.EnumImageFillType;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import java.io.IOException;

public class SimpleImageComponent extends AbstractComponent {

    public ResourceLocation loc;
    private SimpleTexture texture;
    public EnumImageFillType fillType;

    ///
    private int nativeHeight, nativeWidth;
    private Quad2D imageQuad;
    private float minU, maxU, minV, maxV;

    public SimpleImageComponent(ResourceLocation loc, EnumImageFillType fillType){
        this.loc = loc;
        this.texture = new SimpleTexture(loc);
        this.fillType = fillType;
        this.updateDimensions();
    }

    public void updateDimensions(){
        try {
            SimpleTexture.TextureData data = SimpleTexture.TextureData.getTextureData(Minecraft.getInstance().getResourceManager(), loc);
            nativeHeight = data.getNativeImage().getHeight();
            nativeWidth = data.getNativeImage().getWidth();
        } catch (IOException e) {
            nativeHeight = -1;
            nativeWidth = -1;
        }
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);

        minU = 0; maxU = 1; minV = 0; maxV = 1;

        switch (fillType){
            case IMAGE_FILL:
                float widthRatio = (float)getBounds().renderBounds().width / nativeWidth;
                float heightRatio = (float)getBounds().renderBounds().height / nativeHeight;
                if(widthRatio < heightRatio){
                    float widthU = widthRatio / heightRatio;
                    minU = (1-widthU)/2;
                    maxU = minU + widthU;
                }else{
                    float heightV = heightRatio / widthRatio;
                    minV = (1-heightV)/2;
                    maxV = minV + heightV;
                }
                imageQuad = getBounds().renderBounds();
                break;
            case IMAGE_FIT:
                Vector2D sizing = Vector2D.getSizingFromRatio(getBounds().renderBounds().getSizing(), new Vector2D(nativeWidth, nativeHeight));
                imageQuad = new Quad2D(0, 0, sizing.getX(), sizing.getY()).align(getBounds().renderBounds(), ComponentAlignment.CENTERED, ComponentAlignment.CENTERED);
                break;
            case IMAGE_STRETCH:
                imageQuad = getBounds().renderBounds();
                break;
        }
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(nativeWidth != -1 && nativeHeight != -1){
            //texture.bindTexture();
            GSIRenderHelper.renderTexturedRect(context, RenderType.getEntitySolid(loc), false, imageQuad, ScreenUtils.white.rgba, minU, maxU, minV, maxV);
        }
    }
}
