package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.groups.AbstractGroup;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

//TODO Z DEPTH FOR WINDOWS
public abstract class ColourSelectionGroup extends AbstractGroup {

    public TextInputComponent red, green, blue;
    public SliderComponent redSlider, greenSlider, blueSlider;

    public ColourSelectionGroup() {
        init();
    }

    public void init() {
        addComponent(red = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).getStyling().setSizing(0.8, 1/12D, 0.2, 1/6D, UnitType.PERCENT);
        addComponent(green = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).getStyling().setSizing(0.8, 5/12D, 0.2, 1/6D, UnitType.PERCENT);
        addComponent(blue = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).getStyling().setSizing(0.8, 9/12D, 0.2, 1/6D, UnitType.PERCENT);

        //set default gui font height
        red.inputGlyphStyle.fontHeight = 9;
        green.inputGlyphStyle.fontHeight = 9;
        blue.inputGlyphStyle.fontHeight = 9;

        red.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);
        green.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);
        blue.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);

        addComponent(redSlider = new SliderComponent().setHandleColour(ScreenUtils.red_button.rgba).setTrigger(this::updateColourFromSlider)).getStyling().setSizing(0.1, 1/12D, 0.7, 1/6D, UnitType.PERCENT);
        addComponent(greenSlider = new SliderComponent().setHandleColour(ScreenUtils.green_button.rgba).setTrigger(this::updateColourFromSlider)).getStyling().setSizing(0.1, 5/12D, 0.7, 1/6D, UnitType.PERCENT);
        addComponent(blueSlider = new SliderComponent().setHandleColour(ScreenUtils.blue_button.rgba).setTrigger(this::updateColourFromSlider)).getStyling().setSizing(0.1, 9/12D, 0.7, 1/6D, UnitType.PERCENT);

        redSlider.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);
        greenSlider.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);
        blueSlider.styling.setOuterBackgroundColour(ScreenUtils.transparent_disabled_button);


        updateSliders();
        updateTextBoxes();
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        subComponents.forEach(c -> c.build(getBounds().innerSize()));
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible){
            context.matrix.translate(0, 0, -10);
            GSIRenderHelper.renderComponentBackground(context, bounds, styling);
            GSIRenderHelper.renderComponentBorder(context, bounds, styling);
            subComponents.forEach(component -> component.render(context));

            context.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET*4);

            GSIRenderHelper.renderBasicString(context, "R: ", getBounds().innerSize().x + 1, redSlider.getBounds().outerSize().getCentreY() - 9D/2D, ScreenUtils.red_button.rgba, true);
            GSIRenderHelper.renderBasicString(context,"G: ", getBounds().innerSize().x + 1, greenSlider.getBounds().outerSize().getCentreY() - 9D/2D, ScreenUtils.green_button.rgba, true);
            GSIRenderHelper.renderBasicString(context,"B: ", getBounds().innerSize().x + 1, blueSlider.getBounds().outerSize().getCentreY() - 9D/2D, ScreenUtils.blue_button.rgba, true);

        }
    }

    @Override
    public void onOpened() {
        super.onOpened();
        updateSliders();
        updateTextBoxes();
        rebuild();
    }

    public void updateSliders(){
        redSlider.sliderValue = GSIDesignSettings.selectedColour.getRed() / 255D;
        greenSlider.sliderValue = GSIDesignSettings.selectedColour.getGreen() / 255D;
        blueSlider.sliderValue = GSIDesignSettings.selectedColour.getBlue() / 255D;
    }

    public void updateTextBoxes(){
        red.setTextAndRebuild("" + GSIDesignSettings.selectedColour.getRed());
        green.setTextAndRebuild("" + GSIDesignSettings.selectedColour.getGreen());
        blue.setTextAndRebuild("" + GSIDesignSettings.selectedColour.getBlue());
    }

    public void updateColourFromButton(ColouredButtonComponent buttonComponent, GSIInteractionHandler handler){
        setTextColour(buttonComponent.styling.getEnabledTextColour());
        updateSliders();
        updateTextBoxes();
    }

    public void updateColourFromSlider(SliderComponent sliderComponent, GSIInteractionHandler handler){
        int r = (int)(redSlider.sliderValue * 255D);
        int g = (int)(greenSlider.sliderValue * 255D);
        int b = (int)(blueSlider.sliderValue * 255D);
        setTextColour(new ColourProperty(r, g, b));
        updateTextBoxes();
    }

    public void updateColourFromTextBox(TextInputComponent inputComponent, GSIInteractionHandler handler){
        int r = red.text().getIntegerFromText();
        int g = green.text().getIntegerFromText();
        int b = blue.text().getIntegerFromText();
        setTextColour(new ColourProperty(r, g, b));
        updateSliders();
    }

    public abstract void setTextColour(ColourProperty property);

}
