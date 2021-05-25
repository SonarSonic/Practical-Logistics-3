package sonar.logistics.client.imgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gui.widgets.AbstractWidget;
import sonar.logistics.util.MathUtils;
import sonar.logistics.util.vectors.Quad2D;

public class GSIViewportWidget extends AbstractWidget implements IImGuiEventListener {

    public GSI gsi;
    public Quad2D bounds;

    public double centreX, centreY;
    public double defaultScale;
    public float scaleMultiplier;

    public boolean isViewportActive = false;
    public boolean isViewportHovered = false;

    public GSIViewportWidget(GSI gsi, double x, double y, double width, double height){
        this.gsi = gsi;
        this.bounds = new Quad2D(x, y, width, height);
        this.defaultCentre();
        this.defaultScaling();
    }

    //the default multiplier for rendering the GSI, which fills 80% on the screen
    public void defaultScaling(){
        this.defaultScale = Math.min(bounds.width / getGSIRenderWidth(), bounds.height / getGSIRenderHeight())*0.8D;
        this.scaleMultiplier = 1;
    }

    public double getScale(){
        return defaultScale * scaleMultiplier;
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
        return gsi.getGSIBounds().getWidth() + getGSIBorderWidth()*2;
    }

    //the total render height of the gsi, including borders
    public double getGSIRenderHeight(){
        return gsi.getGSIBounds().getHeight() + getGSIBorderHeight()*2;
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
        return getCentreOffsetX() + (-(getGSIRenderWidth() - getGSIBorderWidth()*2)/2)* getScale();
    }

    //the total y render offset relative to the gui, not the display
    public double getRenderOffsetY(){
        return getCentreOffsetY() + (-(getGSIRenderHeight() - getGSIBorderHeight()*2)/2)* getScale();
    }

    //the bounds of the gsi, within the "fake" screen
    public Quad2D getBoundsForGSI(){
        return new Quad2D(getRenderOffsetX(), getRenderOffsetY(), gsi.getGSIBounds().getWidth() * getScale(), gsi.getGSIBounds().getHeight() * getScale());
    }

    //the bounds of the gsi, including the fake screens borders
    public Quad2D getBoundsForFakeDisplay(){
        return new Quad2D(getCentreOffsetX() - (getGSIRenderWidth()* getScale())/2, getCentreOffsetY() - (getGSIRenderHeight()* getScale())/2, getGSIRenderWidth()* getScale(), getGSIRenderHeight()* getScale());
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        /*
        //start scissor test
        MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        double scale = mainWindow.getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) ((bounds.x + 1) * scale), (int) (mainWindow.getHeight() - ((bounds.y + bounds.height -1) * scale)), (int) ((bounds.width - 2) * scale), (int) ((bounds.height -2) * scale));
        */

        //gsi rendering - context / interaction handler update
        RenderSystem.enableDepthTest();
        GSIRenderContext context = new GSIRenderContext(gsi, partialTicks, new MatrixStack());
        context.gsiScaling = (float) getScale();
        gsi.interactionHandler.updateMouseFromGui((absoluteMouseX - getRenderOffsetX()) / getScale(), (absoluteMouseY - getRenderOffsetY()) / getScale());

        //gsi rendering - alignment & scaling
        context.matrix.translate(centreX, centreY, 0);
        context.matrix.scale((float) getScale(), (float) getScale(), 1);
        context.matrix.translate(-(getGSIRenderWidth()/2) + getGSIBorderWidth(), - (getGSIRenderHeight()/2) + getGSIBorderHeight(), 0);

        //gsi rendering - main
        context.matrix.scale(1, 1, -1); //GUI'S are rendered back to front.
        context.scaleNormals(1, 1, -1);

        //screen background - scissored
        context.matrix.translate(-getGSIBorderWidth(),-getGSIBorderHeight(), 1); //
        GSIRenderHelper.renderColouredRect(context, true, gsi.getGSIBounds(), 0, 0, getGSIRenderWidth(), getGSIRenderHeight(), ScreenUtils.display_black_border);
        GSIRenderHelper.renderColouredRect(context, true, gsi.getGSIBounds(), getGSIBorderWidth()/2, getGSIBorderHeight()/2, getGSIRenderWidth() - getGSIBorderWidth(), getGSIRenderHeight() - getGSIBorderHeight(), ScreenUtils.display_blue_border);
        GSIRenderHelper.renderColouredRect(context, true, gsi.getGSIBounds(), getGSIBorderWidth(), getGSIBorderHeight(), getGSIRenderWidth() - getGSIBorderWidth()*2, getGSIRenderHeight() - getGSIBorderHeight()*2, ScreenUtils.display_grey_bgd);
        context.matrix.translate(+getGSIBorderWidth(), +getGSIBorderHeight(), -1);

        gsi.render(context);
        context.matrix.scale(1, 1, -1); //revert the scaling

        RenderSystem.disableDepthTest();

        //render coordinates + zoom scaling
        /*
        RenderSystem.enableDepthTest();
        RenderSystem.translated(0, 0, 1);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - bounds.width / 2) + ", Y: " + df.format(centreY - bounds.height / 2) + ", Zoom: " + df.format(scaling/16), (int)bounds.getX() + 2, (int) (bounds.getMaxY() - 11), ScreenUtils.white.rgba);
        RenderSystem.translated(0, 0, -1);
        RenderSystem.disableDepthTest();
        */


        //end scissor test
        //GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }

    @Override
    public Quad2D getBounds() {
        return bounds;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        return isViewportHovered && bounds.contains(absoluteMouseX, absoluteMouseY);
    }

    public boolean isMouseOverGSI(double mouseX, double mouseY){
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        return isViewportHovered && getBoundsForGSI().contains(absoluteMouseX, absoluteMouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        if (isMouseOver(absoluteMouseX, absoluteMouseY)) {
            if(button != 2 && IImGuiEventListener.super.mouseClicked(absoluteMouseX, absoluteMouseY, button)){
                return true;
            }
            if(button == 1) {
                defaultScaling();
                defaultCentre();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        if(gsi.interactionHandler.mouseScrolled(absoluteMouseX, absoluteMouseY, scroll)){
            return true;
        }
        if(isMouseOver(absoluteMouseX, absoluteMouseY)){
            scaleMultiplier = MathUtils.clamp((float)(scaleMultiplier + scroll*0.1F), 0.1F, 10F);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        double scaling = ImGui.getIO().getMousePosX() / mouseX;
        float absoluteMouseX = ImGui.getIO().getMousePosX();
        float absoluteMouseY = ImGui.getIO().getMousePosY();
        float absoluteDragX = (float)(dragX*scaling);
        float absoluteDragY = (float)(dragY*scaling);
        if(button != 2 && isMouseOverGSI(absoluteMouseX, absoluteMouseY) && IImGuiEventListener.super.mouseDragged(mouseX, mouseY, button, dragX, dragY)){
            return true;
        }
        if(!gsi.isDragging()) {
            if (button == 0 || button == 2) {
                centreX += absoluteDragX;
                centreY += absoluteDragY;
                return true;
            }
        }
        return false;
    }

    @Override
    public IGuiEventListener getEventListener() {
        return gsi.interactionHandler;
    }
}
