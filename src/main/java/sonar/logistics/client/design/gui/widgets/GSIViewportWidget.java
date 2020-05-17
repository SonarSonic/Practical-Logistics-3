package sonar.logistics.client.design.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import org.lwjgl.opengl.GL11;
import sonar.logistics.client.design.api.IFlexibleGuiEventListener;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.design.gui.EnumLineStyling;
import sonar.logistics.client.design.gui.GSIDesignSettings;
import sonar.logistics.client.design.gui.ScreenUtils;
import sonar.logistics.client.design.gui.interactions.AbstractViewportInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultResizeInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultDragInteraction;
import sonar.logistics.client.design.gui.interactions.DefaultTextInteraction;
import sonar.logistics.client.design.windows.EnumRescaleType;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import java.text.DecimalFormat;

public class GSIViewportWidget implements IRenderable, IFlexibleGuiEventListener, IInteractWidget {

    public GSI gsi;

    public IScaleableComponent selectedComponent;
    public Quad2D bounds;

    public double centreX, centreY;
    public double scaling;
    public EnumRescaleType currentRescaleType = null;

    public DefaultResizeInteraction resizeInteraction = new DefaultResizeInteraction(this);
    public DefaultDragInteraction dragInteraction = new DefaultDragInteraction(this);
    public AbstractViewportInteraction currentInteraction = dragInteraction;

    DisplayInteractionHandler handler;

    public GSIViewportWidget(GSI gsi, double x, double y, double width, double height){
        this.gsi = gsi;
        this.bounds = new Quad2D(x, y, width, height);
        this.handler = new DisplayInteractionHandler(gsi, Minecraft.getInstance().player, true);
        this.defaultCentre();
        this.defaultScaling();
    }

    //the default multiplier for rendering the GSI.
    public void defaultScaling(){
        this.scaling = Math.min(160, Math.min(bounds.width / getGSIRenderWidth(), bounds.height / getGSIRenderHeight()));
    }

    public void defaultCentre(){
        this.centreX = bounds.width / 2;
        this.centreY = bounds.height / 2;
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
        return gsi.display.getGSIBounds().getWidth() + getGSIBorderWidth()*2;
    }

    //the total render height of the gsi, including borders
    public double getGSIRenderHeight(){
        return gsi.display.getGSIBounds().getHeight() + getGSIBorderHeight()*2;
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
        return bounds.x + centreX;
    }

    //the y offset of the central render point, can be outside of the scissored range
    public double getCentreOffsetY(){
        return bounds.y + centreY;
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
    public Quad2D getBoundsForDisplay(){
        return new Quad2D(getRenderOffsetX(), getRenderOffsetY(), gsi.display.getGSIBounds().getWidth() * scaling, gsi.display.getGSIBounds().getHeight() * scaling);
    }

    //the components window relative to the gui
    public Quad2D getScaledBoundsForComponent(IScaleableComponent component){
        return component.getBounds().maxBounds().copy().factor(scaling).translate(getRenderOffsetX(), getRenderOffsetY());
    }

    //converts the gui relative window sizing to display relative sizing
    public void setBoundsFromScaledBounds(IScaleableComponent component, Quad2D scaledBounds){
        Quad2D bounds = getBoundsForDisplay();
        double x = (scaledBounds.x - bounds.x) / bounds.width;
        double y = (scaledBounds.y - bounds.y) / bounds.height;
        double width = scaledBounds.width / bounds.width;
        double height = scaledBounds.height / bounds.height;
        component.getBounds().setBoundPercentages(new Quad2D(x, y, width, height));
        gsi.queueRebuild();
    }

    //gets the hit display hit vector, note this could be negative / off the display
    public Vector2D getHitVecFromMouse(double mouseX, double mouseY){
        return new Vector2D((mouseX - getRenderOffsetX()) / scaling, (mouseY - getRenderOffsetY()) / scaling);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        ScaleableRenderContext context = new ScaleableRenderContext(gsi, partialTicks, new MatrixStack());
        handler.update(new Vector2D((mouseX - getRenderOffsetX()) / scaling, (mouseY - getRenderOffsetY()) / scaling));

        //start scissor test
        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (bounds.x * scale), (int) (mainWindow.getHeight() - ((bounds.y + bounds.height) * scale)), (int) (bounds.width * scale), (int) (bounds.height * scale));

        ///align render - scissored
        RenderSystem.pushMatrix();
        RenderSystem.translated(getCentreOffsetX(), getCentreOffsetY(), 10);
        RenderSystem.scaled(scaling, scaling, scaling);
        RenderSystem.translated(-(getGSIRenderWidth()/2) + getGSIBorderWidth(), - (getGSIRenderHeight()/2) + getGSIBorderHeight(), 0);

        //screen background - scissored
        ScreenUtils.fillDouble(-getGSIBorderWidth(), -getGSIBorderHeight(), gsi.display.getGSIBounds().getWidth() + getGSIBorderWidth(), gsi.display.getGSIBounds().getHeight() + getGSIBorderHeight(), ScreenUtils.display_black_border.rgba);
        ScreenUtils.fillDouble(-getGSIBorderWidth()/2, -getGSIBorderHeight()/2, gsi.display.getGSIBounds().getWidth() + getGSIBorderWidth()/2, gsi.display.getGSIBounds().getHeight() + getGSIBorderHeight()/2, ScreenUtils.display_blue_border.rgba);
        ScreenUtils.fillDouble(0, 0, gsi.display.getGSIBounds().getWidth(), gsi.display.getGSIBounds().getHeight(), ScreenUtils.display_grey_bgd.rgba);

        //gsi rendering - scissored
        RenderSystem.enableDepthTest();
        gsi.render(context, handler);
        RenderSystem.disableDepthTest();
        RenderSystem.popMatrix();

        //interaction rendering - scissored
        currentInteraction.renderScissored(mouseX, mouseY, partialTicks);

        //render coordinates + zoom scaling
        RenderSystem.enableDepthTest();
        RenderSystem.translated(0, 0, 10);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - bounds.width / 2) + ", Y: " + df.format(centreY - bounds.height / 2) + ", Zoom: " + df.format(scaling/16), (int)bounds.getX() + 2, (int) (bounds.getMaxY() - 11), ScreenUtils.white.rgba);
        RenderSystem.translated(0, 0, -10);
        RenderSystem.disableDepthTest();

        //end scissor test
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        //interaction rendering - non scissored
        currentInteraction.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public Quad2D getQuad() {
        return bounds;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(!isMouseOver(mouseX, mouseY)){
            return false;
        }

        /* TODO FIX RESIZING MAKE IT CHECK IT THE ELEMENT CAN BE CLICKED FIRST.
        if(selectedComponent != null && resizeInteraction.getRescaleTypeFromMouse(mouseX, mouseY) != null){
            currentInteraction = resizeInteraction;
            return resizeInteraction.mouseClicked(mouseX, mouseY, button);
        }
        */

        //update the selected component
        selectedComponent = gsi.getInteractedComponent(handler);

        if(selectedComponent != null && selectedComponent instanceof StyledTextComponent){
            currentInteraction = new DefaultTextInteraction(this, ((StyledTextComponent) selectedComponent).pages);
            return currentInteraction.mouseClicked(mouseX, mouseY, button);
        }

        ///note resize is not clicked again
        currentInteraction = dragInteraction;
        return dragInteraction.mouseClicked(mouseX, mouseY, button);
    }

    ///// NESTED INTERACTIONS

    public void onGlyphAttributeChanged(GlyphStyleAttributes attribute, Object attributeObj) {
        currentInteraction.onGlyphStyleChanged(attribute, attributeObj);
    }

    public void onLineStyleChanged(EnumLineStyling settings){
        currentInteraction.onLineStyleChanged(settings);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    @Override
    public AbstractViewportInteraction getEventListener() {
        return currentInteraction;
    }


}
