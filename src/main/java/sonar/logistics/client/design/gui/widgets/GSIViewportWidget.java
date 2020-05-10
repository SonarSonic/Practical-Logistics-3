package sonar.logistics.client.design.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import sonar.logistics.client.design.api.Window;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.design.gui.interactions.ViewportAbstractInteraction;
import sonar.logistics.client.design.api.IFlexibleGuiEventListener;
import sonar.logistics.client.design.windows.EnumRescaleType;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;

import java.text.DecimalFormat;

public class GSIViewportWidget implements IRenderable, IFlexibleGuiEventListener {

    public GSI gsi;

    public IScaleableComponent selectedComponent;
    public double x, y, width, height;

    public double centreX, centreY;
    public double scaling;
    public EnumRescaleType currentRescaleType = null;

    public GSIViewportWidget(GSI gsi, double x, double y, double width, double height){
        GSIDesignSettings.clearViewportInteraction();
        this.gsi = gsi;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.defaultCentre();
        this.defaultScaling();
    }

    //the default multiplier for rendering the GSI.
    public void defaultScaling(){
        this.scaling = Math.min(160, Math.min(width / getGSIRenderWidth(), height / getGSIRenderHeight()));
    }

    public void defaultCentre(){
        this.centreX = width / 2;
        this.centreY = height / 2;
    }

    //// DISPLAY RELATIVE VALUES \\\\

    //the screens border width, this may need to be dynamic with different screen designs
    public double getGSIBorderWidth(){
        return 0.0625;
    }

    //the screens border height, this may need to be dynamic with different screen designs
    public double getGSIBorderHeight(){
        return 0.0625;
    }

    //the total render width of the gsi, including borders
    public double getGSIRenderWidth(){
        return gsi.display.getGSISizing().getX() + getGSIBorderWidth()*2;
    }

    //the total render height of the gsi, including borders
    public double getGSIRenderHeight(){
        return gsi.display.getGSISizing().getY() + getGSIBorderHeight()*2;
    }

    //the drag x value relative to the display, snapped to the nearest pixel(s) according to the current grid size
    public double getSnappedDragX(double dragX){
        return GSIDesignSettings.snapToScaledGrid(dragX, scaling);
    }

    //the drag x value relative to the display, snapped to the nearest pixel(s) according to the current grid size
    public double getSnappedDragY(double dragY) {
        return GSIDesignSettings.snapToScaledGrid(dragY, scaling);
    }

    //// GUI RELATIVE VALUES \\\\

    //the x offset of the central render point, can be outside of the scissored range
    public double getCentreOffsetX(){
        return x + centreX;
    }

    //the y offset of the central render point, can be outside of the scissored range
    public double getCentreOffsetY(){
        return y + centreY;
    }

    //the total x render offset relative to the gui, not the display
    public double getRenderOffsetX(){
        return getCentreOffsetX() + (-(getGSIRenderWidth() - getGSIBorderWidth()*2)/2)*scaling;
    }

    //the total y render offset relative to the gui, not the display
    public double getRenderOffsetY(){
        return getCentreOffsetY() + (-(getGSIRenderHeight() - getGSIBorderHeight()*2)/2)*scaling;
    }

    //the bounds of the display relative to the gui
    public Window getBoundsForDisplay(){
        return new Window(getRenderOffsetX(), getRenderOffsetY(), gsi.display.getGSISizing().getX() * scaling, gsi.display.getGSISizing().getY() * scaling);
    }

    //the components window relative to the gui
    public Window getWindowForComponent(IScaleableComponent component){
        double wX = component.getAlignment().getAlignment().getX() * scaling;
        double wY = component.getAlignment().getAlignment().getY() * scaling;
        double wW = component.getAlignment().getSizing().getX() * scaling;
        double wH = component.getAlignment().getSizing().getY() * scaling;
        return new Window(wX + getRenderOffsetX(), wY + getRenderOffsetY(), wW, wH);
    }

    //converts the gui relative window sizing to display relative sizing
    public void setAlignmentFromWindow(IScaleableComponent component, Window window){
        Window bounds = getBoundsForDisplay();
        double x = (window.x - bounds.x)/bounds.width;
        double y = (window.y - bounds.y)/bounds.height;
        double width = window.width / bounds.width;
        double height = window.height / bounds.height;
        component.getAlignment().setAlignmentPercentages(new Vec3d(x, y, 0), new Vec3d(width, height, 100));
        gsi.queueRebuild();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        //start scissor test
        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scale), (int) (mainWindow.getHeight() - ((y + height) * scale)), (int) (width * scale), (int) (height * scale));

        ///align render - scissored
        RenderSystem.pushMatrix();
        RenderSystem.translated(getCentreOffsetX(), getCentreOffsetY(), 10);
        RenderSystem.scaled(scaling, scaling, scaling);
        RenderSystem.translated(-(getGSIRenderWidth()/2) + getGSIBorderWidth(), - (getGSIRenderHeight()/2) + getGSIBorderHeight(), 0);

        //screen background - scissored
        ScreenUtils.fillDouble(-getGSIBorderWidth(), -getGSIBorderHeight(), gsi.display.getGSISizing().getX() + getGSIBorderWidth(), gsi.display.getGSISizing().getY() + getGSIBorderHeight(), ScreenUtils.display_black_border.rgba);
        ScreenUtils.fillDouble(-getGSIBorderWidth()/2, -getGSIBorderHeight()/2, gsi.display.getGSISizing().getX() + getGSIBorderWidth()/2, gsi.display.getGSISizing().getY() + getGSIBorderHeight()/2, ScreenUtils.display_blue_border.rgba);
        ScreenUtils.fillDouble(0, 0, gsi.display.getGSISizing().getX(), gsi.display.getGSISizing().getY(), ScreenUtils.display_grey_bgd.rgba);

        //gsi rendering - scissored
        ScaleableRenderContext context = new ScaleableRenderContext(gsi, partialTicks, new MatrixStack());
        RenderSystem.enableDepthTest();
        gsi.render(context);
        RenderSystem.disableDepthTest();
        RenderSystem.popMatrix();

        //interaction rendering - scissored
        getEventListener().renderScissored(mouseX, mouseY, partialTicks);

        //render coordinates + zoom scaling
        RenderSystem.enableDepthTest();
        RenderSystem.translated(0, 0, 10);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - width / 2) + ", Y: " + df.format(centreY - height / 2) + ", Zoom: " + df.format(scaling/16), (int)x + 2, (int) (y + height - 11), ScreenUtils.white.rgba);
        RenderSystem.translated(0, 0, -10);
        RenderSystem.disableDepthTest();

        //end scissor test
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        //interaction rendering - non scissored
        getEventListener().render(mouseX, mouseY, partialTicks);
    }

    public boolean isMouseOverViewport(double mouseX, double mouseY) {
        return (mouseX >= x && mouseY >= y && mouseX < (x + width) && mouseY < (y + height));
    }

    @Override
    public ViewportAbstractInteraction getEventListener() {
        return GSIDesignSettings.getViewportInteraction(this);
    }
}
