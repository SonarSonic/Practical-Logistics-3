package sonar.logistics.multiparts.displays;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class DisplayRenderHelper {

    public static final int FULL_LIGHT = 15728880;

    public static void translate(MatrixStack matrixStack, Vec3d vec3d){
        matrixStack.translate(vec3d.x, vec3d.y, vec3d.z);
    }

    public static void scale(MatrixStack matrixStack, Vec3d vec3d){
        matrixStack.scale((float)vec3d.x, (float)vec3d.y, (float)vec3d.z);
    }


    public static void colouredRect(MatrixStack.Entry matrix, IRenderTypeBuffer renderer, int light, int overlayLight, float left, float top, float width, float height, int red, int green, int blue, int alpha) {

        ////// BUILDING QUADS \\\\\\

        Matrix4f matrix4f = matrix.getMatrix();

        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR_LIGHTMAP);
        float z = 0;
        builder.pos(matrix4f, left, top + height, z).color(red, green, blue, alpha).lightmap(light).overlay(overlayLight).endVertex();
        builder.pos(matrix4f, left + width, top + height, z).color(red, green, blue, alpha).lightmap(light).overlay(overlayLight).endVertex();
        builder.pos(matrix4f, left + width, top, z).color(red, green, blue, alpha).lightmap(light).overlay(overlayLight).endVertex();
        builder.pos(matrix4f, left, top, z).color(red, green, blue, alpha).lightmap(light).overlay(overlayLight).endVertex();
        builder.finishDrawing();



        ////// RENDERING \\\\\

        RenderSystem.disableTexture();
        Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
        RenderSystem.enableDepthTest();

        WorldVertexBufferUploader.draw(builder);

        RenderSystem.disableDepthTest();
        Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
        RenderSystem.enableTexture();

    }


    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
    }

    public static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    public static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    public static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    public static float getAlpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }

    public static void fluidRect(FluidStack fluidStack, MatrixStack.Entry matrix, IRenderTypeBuffer renderer, int light, int overlayLight, float startX, float startY, float width, float height) {


        ////// PREP RENDER \\\\\\

        TextureAtlasSprite sprite = getSprite(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        int luminosity = Math.max(light, fluidStack.getFluid().getAttributes().getLuminosity(fluidStack));
        float red = getRed(color), green = getGreen(color), blue = getBlue(color), alpha = getAlpha(color);

        int blockLight = LightTexture.getLightBlock(light);
        int skyLight = LightTexture.getLightSky(light);
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

        ////// BUILDING QUADS \\\\\\
        Matrix4f matrix4f = matrix.getMatrix();

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

    public static void texturedRect(double left, double top, double width, double height, int uv_left, int uvTop) {
        texturedRect(left, left + width, top, top + height, (uv_left + 0.0F) / (float)256, (uv_left + (float)width) / (float)256, (uvTop + 0.0F) / (float)256, (uvTop + (float)height) / (float)256);
    }

    public static void texturedRect(double left, double top, double width, double height, int uv_left, int uvTop, int texX, int texY) {
        texturedRect(left, left + width, top, top + height, (uv_left + 0.0F) / (float)texX, (uv_left + (float)width) / (float)texX, (uvTop + 0.0F) / (float)texY, (uvTop + (float)height) / (float)texY);
    }

    public static void texturedRect(double left, double right, double bottom, double top, float uvLeft, float uvTop, float uvRight, float uvBottom) {

        double z = 0;
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(left, top, z).tex(uvLeft, uvBottom).endVertex();
        builder.pos(right, top, z).tex(uvTop, uvBottom).endVertex();
        builder.pos(right, bottom, z).tex(uvTop, uvRight).endVertex();
        builder.pos(left, bottom, z).tex(uvLeft, uvRight).endVertex();
        builder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(builder);
    }



}
