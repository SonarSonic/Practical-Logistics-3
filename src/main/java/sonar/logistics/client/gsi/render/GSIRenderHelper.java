package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.PL3;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ComponentStyling;
import sonar.logistics.client.vectors.Quad2D;

public class GSIRenderHelper {

    /// TEXTURES \\\
    public static final ResourceLocation BATCHED_RECT_TEXTURE = new ResourceLocation(PL3.MODID,"textures/gui/batched_rect_texture.png" );

    /// RENDER TYPES \\\
    public static final RenderType RECT_RENDER_TYPE = GSIRenderTypes.getTranslucentCullNoDiffuse(BATCHED_RECT_TEXTURE);
    public static final RenderType BUTTON_RENDER_TYPE = GSIRenderTypes.getTranslucentCullNoDiffuse(ScreenUtils.BUTTONS_ALPHA);

    /// LIGHTING STATES \\\
    public static final int FULL_LIGHT = 15728880;

    /// Z OFFSETS \\\
    public static final float MIN_Z_OFFSET = -0.001F;
    public static final float ITEM_OFFSET = -0.001F;
    public static final float ITEM_Z_SCALE = 0.01F;

    //0 = Background


    /**layer 0 has no offset from the display*/
    public static float layerZOffset(int layer){
        return MIN_Z_OFFSET * layer;
    }

    /**simple helper method to translate the context to a specific z layer*/
    public static void pushLayerOffset(GSIRenderContext context, int layer){
        context.matrix.translate(0, 0, layerZOffset(layer));
    }

    /**simple helper method to translate the context back from specific z layer*/
    public static void popLayerOffset(GSIRenderContext context, int layer){
        context.matrix.translate(0, 0, -layerZOffset(layer));
    }

    ///// COLOURED RECTS \\\\\

    public static void renderColouredRect(GSIRenderContext context, boolean batched, Quad2D bounds, int rgba){
        renderColouredRect(context, batched, (float)bounds.getX(), (float)bounds.getY(), (float)bounds.getMaxX(), (float)bounds.getMaxY(), ColourProperty.getRed(rgba), ColourProperty.getGreen(rgba), ColourProperty.getBlue(rgba), ColourProperty.getAlpha(rgba));
    }

    public static void renderColouredRect(GSIRenderContext context, boolean batched, Quad2D bounds, double x, double y, double width, double height, ColourProperty colourProperty){
        renderColouredRect(context, batched, bounds, x, y, width, height, colourProperty.getRed(), colourProperty.getGreen(), colourProperty.getBlue(), colourProperty.getAlpha());
    }

    public static void renderColouredRect(GSIRenderContext context, boolean batched, Quad2D bounds, double x, double y, double width, double height, int red, int green, int blue, int alpha){
        renderColouredRect(context, batched, (float) (bounds.getX() + x), (float) (bounds.getY() + y), (float) (bounds.getX() + x + width), (float) (bounds.getY() + y + height), red, green, blue, alpha);
    }

    public static void renderColouredRect(GSIRenderContext context, boolean batched, float startX, float startY, float endX, float endY, int r, int g, int b, int a){
        IRenderTypeBuffer buffer = context.startRenderBuffer(batched);
        IVertexBuilder builder = buffer.getBuffer(RECT_RENDER_TYPE);
        Vector3f normal = context.getNormal();
        builder.pos(context.getMatrix4f(), startX, endY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(0,1).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), endX, endY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(1,1).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), endX, startY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(1,0).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), startX, startY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(0,0).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        context.finishRenderBuffer(batched);
    }

    ///// TEXTURED RECTS \\\\\

    public static void renderTexturedRect(GSIRenderContext context, RenderType type, boolean batched, Quad2D quad, int rgba, float minU, float maxU, float minV, float maxV) {
        renderTexturedRect(context, type, batched, quad.getX(), quad.getY(), quad.getMaxX(), quad.getMaxY(), ColourProperty.getRed(rgba), ColourProperty.getGreen(rgba), ColourProperty.getBlue(rgba), ColourProperty.getAlpha(rgba), minU, maxU, minV, maxV);
    }

    public static void renderTexturedRect(GSIRenderContext context, RenderType type, boolean batched, double startX, double startY, double endX, double endY, int rgba, float minU, float maxU, float minV, float maxV){
        renderTexturedRect(context, type, batched, startX, startY, endX, endY, ColourProperty.getRed(rgba), ColourProperty.getGreen(rgba), ColourProperty.getBlue(rgba), ColourProperty.getAlpha(rgba), minU, maxU, minV, maxV);
    }

    public static void renderTexturedRect(GSIRenderContext context, RenderType type, boolean batched, double startX, double startY, double endX, double endY, int r, int g, int b, int a, float minU, float maxU, float minV, float maxV){
        renderTexturedRect(context, type, batched, (float)startX, (float)startY, (float)endX, (float)endY, r, g, b, a, minU, maxU, minV, maxV);
    }

    public static void renderTexturedRect(GSIRenderContext context, RenderType type, boolean batched, float startX, float startY, float endX, float endY, int r, int g, int b, int a, float minU, float maxU, float minV, float maxV){
        IRenderTypeBuffer buffer = context.startRenderBuffer(batched);
        IVertexBuilder builder = buffer.getBuffer(type);
        Vector3f normal = context.getNormal();
        builder.pos(context.getMatrix4f(), startX, endY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(minU, maxV).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), endX, endY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(maxU, maxV).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), endX, startY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(maxU, minV).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        builder.pos(context.getMatrix4f(), startX, startY, GSIRenderHelper.MIN_Z_OFFSET).color(r, g, b, a).tex(minU, minV).overlay(context.overlay).lightmap(context.light).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        context.finishRenderBuffer(batched);
    }

    ///// STYLES \\\\\

    public static void renderBorders(GSIRenderContext context, ComponentBounds alignment, ComponentStyling styling){
        double marginWidth = styling.marginWidth.getRenderSize(alignment.maxBounds().getWidth());
        double marginHeight = styling.marginHeight.getRenderSize(alignment.maxBounds().getHeight());

        double endX = alignment.maxBounds().getWidth() - marginWidth;
        double endY = alignment.maxBounds().getHeight() - marginHeight;

        double borderWidth = styling.borderSize.getRenderSize(alignment.maxBounds().getWidth());
        double borderHeight = styling.borderSize.getRenderSize(alignment.maxBounds().getHeight());

        GSIRenderHelper.renderColouredRect(context, true, alignment.maxBounds(), marginWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);
        GSIRenderHelper.renderColouredRect(context, true, alignment.maxBounds(), endX-borderWidth, marginHeight, borderWidth, endY - borderHeight*2, styling.borderColour);

        GSIRenderHelper.renderColouredRect(context, true, alignment.maxBounds(), marginWidth, marginHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
        GSIRenderHelper.renderColouredRect(context, true, alignment.maxBounds(), marginWidth, endY - borderHeight, endX - borderWidth*2, borderHeight, styling.borderColour);
    }


    ///// FLUID STACKS \\\\\

    public static void renderScaledFluidStack(GSIRenderContext context, FluidStack toRender, float startX, float startY, float width, float height){
        context.matrix.translate(0,0, MIN_Z_OFFSET);

        TextureAtlasSprite sprite = getSprite(toRender.getFluid().getAttributes().getStillTexture(toRender));
        int color = toRender.getFluid().getAttributes().getColor(toRender);
        int luminosity = Math.max(context.light, toRender.getFluid().getAttributes().getLuminosity(toRender));
        float red = ColourProperty.getRed(color), green = ColourProperty.getGreen(color), blue = ColourProperty.getBlue(color), alpha = ColourProperty.getAlpha(color);

        int blockLight = LightTexture.getLightBlock(context.light);
        int skyLight = LightTexture.getLightSky(context.light);
        int fluidLight = LightTexture.packLight(Math.max(blockLight, luminosity), Math.max(skyLight, luminosity));

        if (luminosity >= 15) {
            fluidLight = FULL_LIGHT;
        }
        float endX = startX + width, endY = startY + height;
        float progress = 100;
        float maxProgress = 100;
        float rectWidth = (progress * (endX - startX)) / maxProgress;
        float maxU = (sprite.getMinU() + (rectWidth * (sprite.getMaxU() - sprite.getMinU()) / (endX - startX)));
        float maxV = (sprite.getMinV() + ((endY - startY) * (sprite.getMaxV() - sprite.getMinV()) / (endX - startX)));
        float z = 0;



        Matrix4f matrix4f = context.matrix.getLast().getMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);

        builder.pos(matrix4f, startX, endY, z).color(red, green, blue, alpha).tex(sprite.getMinU(), maxV).lightmap(fluidLight).endVertex();
        builder.pos(matrix4f, startX + rectWidth, endY, z).color(red, green, blue, alpha).tex(maxU, maxV).lightmap(fluidLight).endVertex();
        builder.pos(matrix4f,startX + rectWidth, startY, z).color(red, green, blue, alpha).tex(maxU, sprite.getMinV()).lightmap(fluidLight).endVertex();
        builder.pos(matrix4f, startX, startY, z).color(red, green, blue, alpha).tex(sprite.getMinU(), sprite.getMinV()).lightmap(fluidLight).endVertex();

        builder.finishDrawing();

        ////// RENDERING QUADS \\\\\

        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        RenderSystem.enableTexture();
        Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
        RenderSystem.enableDepthTest();

        WorldVertexBufferUploader.draw(builder);

        RenderSystem.disableDepthTest();
        Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
    }



    ///// HELPER METHODS \\\\\\

    public static int getBakedLight(int lightmapCoord) {
        int bl = LightTexture.getLightBlock(lightmapCoord);
        int sl = LightTexture.getLightSky(lightmapCoord);
        return LightTexture.packLight(bl, sl);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
    }

}
