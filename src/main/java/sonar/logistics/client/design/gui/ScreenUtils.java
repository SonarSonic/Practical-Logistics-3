package sonar.logistics.client.design.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import sonar.logistics.client.gsi.properties.ColourProperty;

public class ScreenUtils {

    public static ColourProperty grey_base = new ColourProperty(5, 5, 2, 50);
    public static ColourProperty blue_overlay = new ColourProperty(8, 8, 32, 50);
    public static ColourProperty light_grey = new ColourProperty(120, 120, 120);
    public static ColourProperty resize_green = new ColourProperty(120, 255, 120, 120);
    public static ColourProperty white = new ColourProperty(255, 255, 255);

    // display screen colours
    public static ColourProperty display_black_border = new ColourProperty(34, 34, 34);
    public static ColourProperty display_blue_border = new ColourProperty(128, 190, 213);
    public static ColourProperty display_grey_bgd = new ColourProperty(68, 68, 68);

    public static void fill(double x1, double y1, double x2, double y2, int rgba) {
        double value;
        if (x1 < x2) {
            value = x1;
            x1 = x2;
            x2 = value;
        }

        if (y1 < y2) {
            value = y1;
            y1 = y2;
            y2 = value;
        }

        float lvt_6_3_ = (float)(rgba >> 24 & 255) / 255.0F;
        float lvt_7_1_ = (float)(rgba >> 16 & 255) / 255.0F;
        float lvt_8_1_ = (float)(rgba >> 8 & 255) / 255.0F;
        float lvt_9_1_ = (float)(rgba & 255) / 255.0F;
        BufferBuilder lvt_10_1_ = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        lvt_10_1_.begin(7, DefaultVertexFormats.POSITION_COLOR);
        lvt_10_1_.pos(x1, y2, 0).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
        lvt_10_1_.pos(x2, y2, 0).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
        lvt_10_1_.pos( x2, y1, 0).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
        lvt_10_1_.pos( x1, y1, 0).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
        lvt_10_1_.finishDrawing();
        WorldVertexBufferUploader.draw(lvt_10_1_);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void blit(double left, double top, int uv_left, int uvTop, double width, double height) {
        int texX = 256;
        int texY = 256;
        accurateBlit(left, left + width, top, top + height, (uv_left + 0.0F) / (float)texX, (uv_left + (float)width) / (float)texX, (uvTop + 0.0F) / (float)texY, (uvTop + (float)height) / (float)texY);
    }

    public static void accurateBlit(double left, double right, double bottom, double top, float uvLeft, float uvTop, float uvRight, float uvBottom) {
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
