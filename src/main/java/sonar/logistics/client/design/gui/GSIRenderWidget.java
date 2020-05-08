package sonar.logistics.client.design.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import sonar.logistics.client.design.windows.EnumRescaleType;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;

import java.text.DecimalFormat;

public class GSIRenderWidget implements IRenderable, IGuiEventListener {

    public GSI gsi;
    public double x, y, width, height;

    public double centreX, centreY;
    public double scaling;

    public GSIRenderWidget(GSI gsi, double x, double y, double width, double height){
        this.gsi = gsi;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scaling = Math.min(width / getGSIRenderWidth(), height / getGSIRenderHeight());
        this.centreX = width / 2;
        this.centreY = height / 2;
    }

    public double getGSIBorderWidth(){
        return 0.0625;
    }

    public double getGSIBorderHeight(){
        return 0.0625;
    }

    public double getGSIRenderWidth(){
        return gsi.display.getScreenSizing().getX() + getGSIBorderWidth()*2;
    }

    public double getGSIRenderHeight(){
        return gsi.display.getScreenSizing().getY() + getGSIBorderHeight()*2;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return (mouseX >= x && mouseY >= y && mouseX < (x + width) && mouseY < (y + height));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        ///// START SCISSOR TEST \\\\\

        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scale), (int) (mainWindow.getHeight() - ((y + height) * scale)), (int) (width * scale), (int) (height * scale));

        ///// SCREEN RENDERING \\\\\

        RenderSystem.pushMatrix();
        RenderSystem.translated(x + centreX, y + centreY, 0);
        RenderSystem.scaled(scaling, scaling, 0);
        RenderSystem.translated(-getGSIRenderWidth()/2, - getGSIRenderHeight()/2, 0);
        RenderSystem.translated(getGSIBorderWidth(), getGSIBorderHeight(), 0);

        //// SCREEN BACKGROUND \\\\\

        ScreenUtils.fill(-getGSIBorderWidth(), -getGSIBorderHeight(), gsi.display.getScreenSizing().getX() + getGSIBorderWidth(), gsi.display.getScreenSizing().getY() + getGSIBorderHeight(), ScreenUtils.display_black_border.rgba);
        ScreenUtils.fill(-getGSIBorderWidth()/2, -getGSIBorderHeight()/2, gsi.display.getScreenSizing().getX() + getGSIBorderWidth()/2, gsi.display.getScreenSizing().getY() + getGSIBorderHeight()/2, ScreenUtils.display_blue_border.rgba);
        ScreenUtils.fill(0, 0, gsi.display.getScreenSizing().getX(), gsi.display.getScreenSizing().getY(), ScreenUtils.display_grey_bgd.rgba);

        ///// GSI RENDERING \\\\
        IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        ScaleableRenderContext renderContext = new ScaleableRenderContext(gsi, partialTicks, new MatrixStack(), buffer, ScaleableRenderHelper.FULL_LIGHT, 0, null, true);
        gsi.render(renderContext);
        buffer.finish();

        RenderSystem.popMatrix();

        ///// OVERLAY RENDERING - SCISSORED \\\\\\

        double renderOffsetX = x + centreX + (-(getGSIRenderWidth() - getGSIBorderWidth()*2)/2)*scaling;
        double renderOffsetY = y + centreY + (-(getGSIRenderHeight() - getGSIBorderHeight()*2)/2)*scaling;

        IScaleableComponent selected = gsi.components.get(0);
        double wX = selected.getAlignment().getAlignment().getX()*scaling;
        double wY = selected.getAlignment().getAlignment().getY()*scaling;
        double wW = selected.getAlignment().getSizing().getX()*scaling;
        double wH = selected.getAlignment().getSizing().getY()*scaling;
        Window window = new Window(wX + renderOffsetX, wY + renderOffsetY, wW, wH);

        for(EnumRescaleType type : EnumRescaleType.values()){
            if(type.xPos != -1) {
                double[] clickBox = type.getClickBox(window, 3.0F);
                ScreenUtils.fill(clickBox[0], clickBox[1], clickBox[2], clickBox[3], ScreenUtils.white.rgba);
            }
        }

        ///// END SCISSOR TEST \\\\\

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - width / 2) + ", Y: " + df.format(centreY - height / 2) + ", Zoom: " + df.format(scaling/16), (int)x + 2, (int) (y + height - 11), ScreenUtils.white.rgba);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isMouseOver(mouseX, mouseY)) {
            if(button == 1){
                this.scaling = Math.min(width / (gsi.display.getScreenSizing().getX() + 0.0625*2), height / (gsi.display.getScreenSizing().getY() + 0.0625*2));
                this.centreX = width / 2;
                this.centreY = height / 2;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if(isMouseOver(mouseX, mouseY)) {
            this.scaling += scroll;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(isMouseOver(mouseX, mouseY)) {
            centreX += dragX;
            centreY += dragY;
            return true;
        }
        return false;
    }
}
