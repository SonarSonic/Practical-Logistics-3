package sonar.logistics.client.gui.widgets;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.gui.api.EnumRescaleType;
import sonar.logistics.client.vectors.Quad2D;

//TODO DELETE ME
@OnlyIn(Dist.CLIENT)
public class ScaleableWidget implements IGuiEventListener, IRenderable {

    public Quad2D window;

    public boolean active = true;
    public boolean visible = true;
    private boolean focused;

    public ScaleableWidget(double xIn, double yIn, double widthIn, double heightIn) {
        this.window = new Quad2D(xIn, yIn, widthIn, heightIn);
    }

    public boolean changeFocus(boolean focused) {
        if (this.active && this.visible) {
            this.focused = !this.focused;
            this.onFocusedChanged(this.focused);
            return this.focused;
        } else {
            return false;
        }
    }

    protected void onFocusedChanged(boolean focused) {

    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        EnumRescaleType box  = EnumRescaleType.getRescaleTypeFromMouse(window, mouseX, mouseY, 4.0F);
        return this.active && this.visible && (box != null || currentRescaleType != null) || (mouseX >= window.x && mouseY >= window.y && mouseX < (window.x + window.width) && mouseY < (window.y + window.height));
    }

    double dragX = 0;
    double dragY = 0;

    EnumRescaleType currentRescaleType = null;
    boolean isShifting = false;

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(currentRescaleType != null) {
            this.dragX += dragX;
            this.dragY += dragY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(currentRescaleType != null) {
            window = currentRescaleType.rescaleWindow(window, bounds, snapToGrid(dragX, snapToPixels), snapToGrid(dragY, snapToPixels), isShifting);
            currentRescaleType = null;
            dragX = dragY = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isOver = isMouseOver(mouseX, mouseY);
        if(this.focused != isOver){
            changeFocus(isOver);
            return isOver;
        }
        if(this.focused){
            currentRescaleType = EnumRescaleType.getRescaleTypeFromMouse(window, mouseX, mouseY, 4.0F);
            dragX = dragY = 0;
            return true;
        }
        return false;
    }


    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if(key == 340 || key == 344){ // shift keys
            isShifting = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int key, int scanCode, int modifiers) {
        if(key == 340 || key == 344){ // shift keys
            isShifting = false;
            return true;
        }
        return false;
    }

    @Override
    public void render(int i, int i1, float v) {
        ScreenUtils.fillDouble(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, ScreenUtils.transparent_grey_bgd.rgba);
        ScreenUtils.fillDouble(window.x, window.y, window.x + window.width, window.y + window.height, ScreenUtils.light_grey.rgba);
        if(focused){
            float boxWidth = 3.0F;
            for(EnumRescaleType type : EnumRescaleType.values()){
                if(type.xPos != -1) {
                    double[] clickBox = type.getClickBox(window, boxWidth);
                    ScreenUtils.fillDouble(clickBox[0], clickBox[1], clickBox[2], clickBox[3], ScreenUtils.white.rgba);
                }
            }
        }
        if(currentRescaleType != null){
            Quad2D rescaledWindow = currentRescaleType.rescaleWindow(window, bounds, snapToGrid(dragX, snapToPixels), snapToGrid(dragY, snapToPixels), isShifting);
            ScreenUtils.fillDouble(rescaledWindow.x, rescaledWindow.y, rescaledWindow.x + rescaledWindow.width, rescaledWindow.y + rescaledWindow.height, ScreenUtils.transparent_activated_button.rgba);
        }
    }

    Quad2D bounds = new Quad2D(100, 100, 200, 200);

    double snapToPixels = 1;
    double snapToGrid1 = snapToPixels*4;
    double snapToGrid2 = snapToPixels*8;

    public static double snapToGrid(double value, double gridSize){
        return gridSize * (Math.round(value / gridSize));
    }


}
