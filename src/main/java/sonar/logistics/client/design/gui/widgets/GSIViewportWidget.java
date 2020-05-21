package sonar.logistics.client.design.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import org.lwjgl.opengl.GL11;
import sonar.logistics.client.design.api.IFlexibleGuiEventListener;
import sonar.logistics.client.design.api.IInteractWidget;
import sonar.logistics.client.design.gui.EnumLineBreakGlyph;
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
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.render.ScaleableRenderHelper;
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

    private DefaultTextInteraction textInteraction = null;
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
        return gsi.host.getGSIBounds().getWidth() + getGSIBorderWidth()*2;
    }

    //the total render height of the gsi, including borders
    public double getGSIRenderHeight(){
        return gsi.host.getGSIBounds().getHeight() + getGSIBorderHeight()*2;
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
        return new Quad2D(getRenderOffsetX(), getRenderOffsetY(), gsi.host.getGSIBounds().getWidth() * scaling, gsi.host.getGSIBounds().getHeight() * scaling);
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
        component.setBounds(new ScaleableBounds(new Quad2D(x, y, width, height)));
        gsi.queueRebuild();
    }

    //gets the hit display hit vector, note this could be negative / off the display
    public Vector2D getHitVecFromMouse(double mouseX, double mouseY){
        return new Vector2D((mouseX - getRenderOffsetX()) / scaling, (mouseY - getRenderOffsetY()) / scaling);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        //start scissor test
        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (bounds.x * scale), (int) (mainWindow.getHeight() - ((bounds.y + bounds.height) * scale)), (int) (bounds.width * scale), (int) (bounds.height * scale));


        //gsi rendering - context / interaction handler update
        RenderSystem.enableDepthTest();
        ScaleableRenderContext context = new ScaleableRenderContext(gsi, partialTicks, new MatrixStack());
        handler.update(new Vector2D((mouseX - getRenderOffsetX()) / scaling, (mouseY - getRenderOffsetY()) / scaling));

        //gsi rendering - alignment & scaling
        context.matrix.translate(getCentreOffsetX(), getCentreOffsetY(), 0);
        context.matrix.scale((float)scaling, (float)scaling, 1);
        context.matrix.translate(-(getGSIRenderWidth()/2) + getGSIBorderWidth(), - (getGSIRenderHeight()/2) + getGSIBorderHeight(), 0);

        //gsi rendering - main
        context.matrix.scale(1, 1, -1); //GUI'S are rendered back to front.
        context.scaleNormals(1, 1, -1);

        //screen background - scissored
        context.matrix.translate(-getGSIBorderWidth(),-getGSIBorderHeight(), 1); //
        ScaleableRenderHelper.renderColouredRect(context, gsi.host.getGSIBounds(), 0, 0, getGSIRenderWidth(), getGSIRenderHeight(), ScreenUtils.display_black_border);
        ScaleableRenderHelper.renderColouredRect(context, gsi.host.getGSIBounds(), getGSIBorderWidth()/2, getGSIBorderHeight()/2, getGSIRenderWidth() - getGSIBorderWidth(), getGSIRenderHeight() - getGSIBorderHeight(), ScreenUtils.display_blue_border);
        ScaleableRenderHelper.renderColouredRect(context, gsi.host.getGSIBounds(), getGSIBorderWidth(), getGSIBorderHeight(), getGSIRenderWidth() - getGSIBorderWidth()*2, getGSIRenderHeight() - getGSIBorderHeight()*2, ScreenUtils.display_grey_bgd);
        context.matrix.translate(+getGSIBorderWidth(), +getGSIBorderHeight(), -1);

        gsi.render(context, handler);
        context.matrix.scale(1, 1, -1); //revert the scaling

        RenderSystem.disableDepthTest();


        //interaction rendering - scissored
        currentInteraction.renderScissored(mouseX, mouseY, partialTicks);

        //render coordinates + zoom scaling
        RenderSystem.enableDepthTest();
        RenderSystem.translated(0, 0, 1);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - bounds.width / 2) + ", Y: " + df.format(centreY - bounds.height / 2) + ", Zoom: " + df.format(scaling/16), (int)bounds.getX() + 2, (int) (bounds.getMaxY() - 11), ScreenUtils.white.rgba);
        RenderSystem.translated(0, 0, -1);
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

        //update the selected component
        if(currentInteraction.mouseClicked(mouseX, mouseY, button)){
            return true;
        }
        selectedComponent = gsi.getComponentAt(handler.mousePos);
        ///note resize is not clicked again
        return dragInteraction.mouseClicked(mouseX, mouseY, button);
    }

    ///// NESTED INTERACTIONS

    public void setTextInteraction(){
        if(selectedComponent != null && selectedComponent instanceof StyledTextComponent){
            textInteraction = new DefaultTextInteraction(this, ((StyledTextComponent) selectedComponent).pages);
            currentInteraction = textInteraction;
        }
    }

    public void onGlyphAttributeChanged(GlyphStyleAttributes attribute, Object attributeObj) {
        currentInteraction.onGlyphStyleChanged(attribute, attributeObj);
    }

    public void onLineStyleChanged(EnumLineStyling settings){
        currentInteraction.onLineStyleChanged(settings);
    }

    public void onLineBreakGlyphChanged(EnumLineBreakGlyph settings){
        currentInteraction.onLineBreakGlyphChanged(settings);
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
