package sonar.logistics.client.gsi.components.input;

import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.interactions.api.IInteractionComponent;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nullable;

public class SliderComponent extends AbstractComponent implements IInteractionComponent {

    //between 0 & 1
    public double sliderValue;
    public boolean isVertical = false;

    public int handleColour = ScreenUtils.transparent_green_button.rgba;

    @Nullable
    public ITrigger<SliderComponent> trigger;

    public SliderComponent(double sliderValue){
        this.sliderValue = sliderValue;
    }

    public SliderComponent setTrigger(@Nullable ITrigger<SliderComponent> trigger) {
        this.trigger = trigger;
        return this;
    }

    public SliderComponent setHandleColour(int handleColour) {
        this.handleColour = handleColour;
        return this;
    }

    public double getSliderValue() {
        return sliderValue;
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        Quad2D sliderHandle;
        if(!isVertical) {
            sliderHandle = new Quad2D(0, 0, getBounds().renderBounds().getWidth() / 16, getBounds().renderBounds().getHeight());
            sliderHandle.x = getBounds().renderBounds().getX() + ((getBounds().renderBounds().width - sliderHandle.width) * sliderValue);
            sliderHandle.y = getBounds().renderBounds().getY();
        }else{
            sliderHandle = new Quad2D(0, 0, getBounds().renderBounds().getWidth(), getBounds().renderBounds().getHeight() / 16);
            sliderHandle.x = getBounds().renderBounds().getX();
            sliderHandle.y = getBounds().renderBounds().getY() + ((getBounds().renderBounds().height - sliderHandle.height) * sliderValue);
        }
        renderSliderHandle(context, sliderHandle);

    }

    public void renderSliderHandle(GSIRenderContext context, Quad2D sliderHandle){
        GSIRenderHelper.pushLayerOffset(context, 1);
        GSIRenderHelper.renderColouredRect(context, true, sliderHandle, handleColour);
        GSIRenderHelper.popLayerOffset(context, 1);
    }

    /// interactions

    public void updateSliderFromMouse(){
        Vector2D relativeMouse = getRelativeMousePos();
        if(!isVertical) {
            sliderValue = relativeMouse.x / getBounds().renderBounds().width;
        }else{
            sliderValue = relativeMouse.y / getBounds().renderBounds().height;
        }
    }

    @Override
    public boolean mouseClicked(int button) {
        if(isMouseOver()){
            updateSliderFromMouse();
            checkSlider();
            onSliderChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double scroll) {
        if(isMouseOver()){
            sliderValue -= scroll/8;
            checkSlider();
            onSliderChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged() {
        updateSliderFromMouse();
        checkSlider();
        onSliderChanged();
        return true;
    }

    public void checkSlider(){
        if (this.sliderValue < 0.0F){
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F){
            this.sliderValue = 1.0F;
        }
    }

    ///

    public void onSliderChanged(){
        if(trigger != null){
            trigger.trigger(this, getInteractionHandler());
        }
    }
}
