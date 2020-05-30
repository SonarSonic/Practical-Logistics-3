package sonar.logistics.client.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import org.lwjgl.opengl.GL11;
import sonar.logistics.client.gui.api.IFlexibleGuiEventListener;
import sonar.logistics.client.gui.api.IInteractWidget;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.vectors.Quad2D;

import java.text.DecimalFormat;

//TODO REPLACE WITH COMPONENT? would this be a massive pain with scissoring though?
public class GSIViewportWidget implements IRenderable, IFlexibleGuiEventListener, IInteractWidget {

    public GSI gsi;

    public IComponent selectedComponent;
    public Quad2D bounds;

    public double centreX, centreY;
    public double scaling;

    public GSIViewportWidget(GSI gsi, double x, double y, double width, double height){
        this.gsi = gsi;
        this.bounds = new Quad2D(x, y, width, height);
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
        return getCentreOffsetX() + (-(getGSIRenderWidth() - getGSIBorderWidth()*2)/2)*scaling;
    }

    //the total y render offset relative to the gui, not the display
    public double getRenderOffsetY(){
        return getCentreOffsetY() + (-(getGSIRenderHeight() - getGSIBorderHeight()*2)/2)*scaling;
    }

    //the bounds of the gsi, within the "fake" screen
    public Quad2D getBoundsForGSI(){
        return new Quad2D(getRenderOffsetX(), getRenderOffsetY(), gsi.getGSIBounds().getWidth() * scaling, gsi.getGSIBounds().getHeight() * scaling);
    }

    //the bounds of the gsi, including the fake screens borders
    public Quad2D getBoundsForFakeDisplay(){
        return new Quad2D(getCentreOffsetX() - getGSIRenderWidth()/2, getCentreOffsetY() - getGSIRenderHeight()/2, getGSIRenderWidth(), getGSIRenderHeight());
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
        GSIRenderContext context = new GSIRenderContext(gsi, partialTicks, new MatrixStack());
        context.gsiScaling = (float)scaling;
        gsi.interactionHandler.updateMouseFromGui((mouseX - getRenderOffsetX()) / scaling, (mouseY - getRenderOffsetY()) / scaling);

        //gsi rendering - alignment & scaling
        context.matrix.translate(getCentreOffsetX(), getCentreOffsetY(), 0);
        context.matrix.scale((float)scaling, (float)scaling, 1);
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
        RenderSystem.enableDepthTest();
        RenderSystem.translated(0, 0, 1);
        DecimalFormat df = new DecimalFormat("#.##");
        Minecraft.getInstance().fontRenderer.drawString("X: " + df.format(centreX - bounds.width / 2) + ", Y: " + df.format(centreY - bounds.height / 2) + ", Zoom: " + df.format(scaling/16), (int)bounds.getX() + 2, (int) (bounds.getMaxY() - 11), ScreenUtils.white.rgba);
        RenderSystem.translated(0, 0, -1);
        RenderSystem.disableDepthTest();

        //end scissor test
        GL11.glDisable(GL11.GL_SCISSOR_TEST);


    }

    @Override
    public Quad2D getQuad() {
        return bounds;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    public boolean isMouseOverGSI(double mouseX, double mouseY){
        return getBoundsForGSI().contains(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if(IFlexibleGuiEventListener.super.mouseClicked(mouseX, mouseY, button)){
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
        if(gsi.interactionHandler.mouseScrolled(mouseX, mouseY, scroll)){
            return true;
        }
        if(isMouseOver(mouseX, mouseY)){
            scaling += scroll;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isMouseOver(mouseX, mouseY)) {
            if(isMouseOverGSI(mouseX, mouseY)){
                return IFlexibleGuiEventListener.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            }
            if(!gsi.isDragging() && !getBoundsForFakeDisplay().contains(mouseX, mouseY)) {
                if (button == 0) {
                    centreX += dragX;
                    centreY += dragY;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IGuiEventListener getEventListener() {
        return gsi.interactionHandler;
    }
}
