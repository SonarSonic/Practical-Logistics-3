package sonar.logistics.client.design.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;
import sonar.logistics.client.gsi.properties.ColourProperty;

public class ScreenUtils {

    public static ColourProperty transparent_grey_bgd = new ColourProperty(5, 5, 2, 50);
    public static ColourProperty transparent_blue_bgd = new ColourProperty(8, 8, 32, 50);

    public static ColourProperty transparent_grey_button = new ColourProperty(80, 80, 80, 80);
    public static ColourProperty transparent_red_button = new ColourProperty(255, 120, 120, 120);
    public static ColourProperty transparent_green_button = new ColourProperty(120, 255, 120, 120);

    public static ColourProperty light_grey = new ColourProperty(120, 120, 120);
    public static ColourProperty white = new ColourProperty(255, 255, 255);

    // display screen colours
    public static ColourProperty display_black_border = new ColourProperty(34, 34, 34);
    public static ColourProperty display_blue_border = new ColourProperty(128, 190, 213);
    public static ColourProperty display_grey_bgd = new ColourProperty(68, 68, 68);

    // textures
    public static ResourceLocation BUTTONS_ALPHA = new ResourceLocation(PL3.MODID,"textures/gui/buttons_alpha.png" );

    ////DEPTH VALUES \\\\
    /*
    GL_NEVER    = 0x200, - 512
    GL_LESS     = 0x201, - 513
    GL_EQUAL    = 0x202, - 514
    GL_LEQUAL   = 0x203, - 515
    GL_GREATER  = 0x204, - 516
    GL_NOTEQUAL = 0x205, - 517
    GL_GEQUAL   = 0x206, - 518
    GL_ALWAYS   = 0x207; - 519
    */

    public static void fillDouble(double x1, double y1, double x2, double y2, int rgba) {
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

        float alpha = (float)(rgba >> 24 & 255) / 255.0F;
        float red = (float)(rgba >> 16 & 255) / 255.0F;
        float green = (float)(rgba >> 8 & 255) / 255.0F;
        float blue = (float)(rgba & 255) / 255.0F;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y2, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos( x2, y1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos( x1, y1, 0).color(red, green, blue, alpha).endVertex();
        buffer.finishDrawing();
        WorldVertexBufferUploader.draw(buffer);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void blitDouble(double left, double top, int uv_left, int uvTop, double width, double height) {
        int texX = 256;
        int texY = 256;
        blitDouble(left, left + width, top, top + height, (uv_left + 0.0F) / (float)texX, (uv_left + (float)width) / (float)texX, (uvTop + 0.0F) / (float)texY, (uvTop + (float)height) / (float)texY);
    }

    public static void blitDouble(double left, double right, double bottom, double top, float uvLeft, float uvTop, float uvRight, float uvBottom) {
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
