package sonar.logistics.client.gsi.components.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.client.gsi.style.ComponentAlignment;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.util.vectors.Quad2F;
import sonar.logistics.util.vectors.Vector2F;

import java.io.IOException;

public class SimpleImageComponent extends Component {

    public ResourceLocation loc;
    private SimpleTexture texture;
    public EnumImageFillType fillType;

    ///
    private int nativeHeight, nativeWidth;
    private Quad2F imageQuad;
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
    public void build(Quad2F bounds) {
        super.build(bounds);

        minU = 0; maxU = 1; minV = 0; maxV = 1;

        switch (fillType){
            case IMAGE_FILL:
                float widthRatio = (float)getBounds().innerSize().width / nativeWidth;
                float heightRatio = (float)getBounds().innerSize().height / nativeHeight;
                if(widthRatio < heightRatio){
                    float widthU = widthRatio / heightRatio;
                    minU = (1-widthU)/2;
                    maxU = minU + widthU;
                }else{
                    float heightV = heightRatio / widthRatio;
                    minV = (1-heightV)/2;
                    maxV = minV + heightV;
                }
                imageQuad = getBounds().innerSize();
                break;
            case IMAGE_FIT:
                Vector2F sizing = Vector2F.getSizingFromRatio(getBounds().innerSize().getSizing(), new Vector2F(nativeWidth, nativeHeight));
                imageQuad = new Quad2F(0, 0, sizing.getX(), sizing.getY()).align(getBounds().innerSize(), ComponentAlignment.CENTERED, ComponentAlignment.CENTERED);
                break;
            case IMAGE_STRETCH:
                imageQuad = getBounds().innerSize();
                break;
        }
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        if(nativeWidth != -1 && nativeHeight != -1){
            GSIRenderHelper.renderTexturedRect(context, RenderType.getEntitySolid(loc), false, imageQuad, ScreenUtils.white.rgba, minU, maxU, minV, maxV);
        }
    }
}
