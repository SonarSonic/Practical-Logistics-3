package sonar.logistics.client.gsi.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ColourProperty;

public class ScaleableRenderHelper {

    public static final int FULL_LIGHT = 15728880;
    public static final Vec3d DEFAULT_ALIGNMENT_PERCENTAGE =  new Vec3d(0, 0, 0);
    public static final Vec3d DEFAULT_SIZING_PERCENTAGE =  new Vec3d(1, 1, 1);

    //// Z OFFSETS \\\\
    public static final float TEXTURE_OFFSET = -0.001F;
    public static final float ITEM_OFFSET = -0.001F;
    public static final float ITEM_Z_SCALE = 0.01F;

    public static int getBakedLight(int lightmapCoord) {
        int bl = LightTexture.getLightBlock(lightmapCoord);
        int sl = LightTexture.getLightSky(lightmapCoord);
        return LightTexture.packLight(bl, sl);
    }

    ///// COLOURED RECTS \\\\\

    public static void renderColouredRect(ScaleableRenderContext context, Vec3d alignment, float x, float y, float width, float height, ColourProperty colourProperty){
        renderColouredRect(context, alignment, x, y, width, height, colourProperty.getRed(), colourProperty.getGreen(), colourProperty.getBlue(), colourProperty.getAlpha());
    }

    public static void renderColouredRect(ScaleableRenderContext context, Vec3d alignment, float x, float y, float width, float height, int red, int green, int blue, int alpha){
        renderColouredRect(context, (float) (alignment.getX() + x), (float) (alignment.getY() + y), (float) (alignment.getX() + x + width), (float) (alignment.getY() + y + height), red, green, blue, alpha);
    }

    public static void renderColouredRect(ScaleableRenderContext context, float startX, float startY, float endX, float endY, int r, int g, int b, int a){
        IVertexBuilder builder = context.getWorldBuffer().getBuffer(DisplayRenderTypes.COLOURED_RECT);
        Matrix4f matrix4f = context.getMatrix4f();
        builder.pos(matrix4f, startX, endY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).lightmap(context.light).endVertex();
        builder.pos(matrix4f, endX, endY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).lightmap(context.light).endVertex();
        builder.pos(matrix4f, endX, startY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).lightmap(context.light).endVertex();
        builder.pos(matrix4f, startX, startY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).lightmap(context.light).endVertex();
    }

    ///// TEXTURED RECTS \\\\\

    /*
    public static void renderTexturedRect(ScaleableRenderContext context, Vec3d alignment, float x, float y, float width, float height, int r, int g, int b, int a, ResourceLocation location){
        renderTexturedRect(context, (float) (alignment.getX() + x), (float) (alignment.getY() + y), (float) (alignment.getX() + x + width), (float) (alignment.getY() + y + height), sprite);
    }

    public static void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite) {
        innerBlit(x, x + width, y, y + height, z, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }
    */

    public static void renderTexturedRect(ScaleableRenderContext context, IVertexBuilder builder, float startX, float startY, float endX, float endY, int r, int g, int b, int a, float minU, float maxU, float minV, float maxV, int light){
        builder.pos(context.getMatrix4f(), startX, endY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).tex(minU, maxV).normal(context.getNormal3f(), 0, 0 , 0).lightmap(light).endVertex();
        builder.pos(context.getMatrix4f(), endX, endY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).tex(maxU, maxV).normal(context.getNormal3f(), 0, 0 , 0).lightmap(light).endVertex();
        builder.pos(context.getMatrix4f(), endX, startY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).tex(maxU, minV).normal(context.getNormal3f(), 0, 0 , 0).lightmap(light).endVertex();
        builder.pos(context.getMatrix4f(), startX, startY, ScaleableRenderHelper.TEXTURE_OFFSET).color(r, g, b, a).tex(minU, minV).normal(context.getNormal3f(), 0, 0 , 0).lightmap(light).endVertex();
    }


    ///// ITEM STACKS \\\\\

    public static void renderScaledFluidStack(ScaleableRenderContext context, FluidStack toRender, float startX, float startY, float width, float height){
        context.matrix.translate(0,0, TEXTURE_OFFSET);

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

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
    }

}
