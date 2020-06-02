package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.ColouredButtonComponent;
import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.interactions.triggers.Trigger;
import sonar.logistics.client.gsi.properties.AbsoluteBounds;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.gsi.properties.ComponentBounds;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;

//TODO Z DEPTH FOR WINDOWS
public abstract class ColourSelectionWindow extends WindowGroup {

    public TextInputComponent red, green, blue;
    public SliderComponent redSlider, greenSlider, blueSlider;

    public ColourSelectionWindow(AbsoluteBounds bounds) {
        super("Colour Picker", bounds);
    }

    @Override
    public void init() {
        super.init();
        addComponent(red = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).setBounds(new ScaleableBounds(0.8, 1/12D, 0.2, 1/6D));
        addComponent(green = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).setBounds(new ScaleableBounds(0.8, 5/12D, 0.2, 1/6D));
        addComponent(blue = new TextInputComponent().setInputType(TextInputComponent.EnumTextInputType.RGB_COLOUR_VALUE).setTrigger(this::updateColourFromTextBox)).setBounds(new ScaleableBounds(0.8, 9/12D, 0.2, 1/6D));

        //set default gui font height
        red.inputGlyphStyle.fontHeight = 9;
        green.inputGlyphStyle.fontHeight = 9;
        blue.inputGlyphStyle.fontHeight = 9;

        red.styling.bgdColour = ScreenUtils.transparent_disabled_button;
        green.styling.bgdColour = ScreenUtils.transparent_disabled_button;
        blue.styling.bgdColour = ScreenUtils.transparent_disabled_button;

        addComponent(redSlider = new SliderComponent(1).setHandleColour(ScreenUtils.red_button.rgba).setTrigger(this::updateColourFromSlider)).setBounds(new ScaleableBounds(0.1, 1/12D, 0.7, 1/6D));
        addComponent(greenSlider = new SliderComponent(1).setHandleColour(ScreenUtils.green_button.rgba).setTrigger(this::updateColourFromSlider)).setBounds(new ScaleableBounds(0.1, 5/12D, 0.7, 1/6D));
        addComponent(blueSlider = new SliderComponent(1).setHandleColour(ScreenUtils.blue_button.rgba).setTrigger(this::updateColourFromSlider)).setBounds(new ScaleableBounds(0.1, 9/12D, 0.7, 1/6D));

        redSlider.styling.bgdColour = ScreenUtils.transparent_red_button;
        greenSlider.styling.bgdColour = ScreenUtils.transparent_green_button;
        blueSlider.styling.bgdColour = ScreenUtils.transparent_blue_button;

    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible) {
            context.matrix.translate(0, 0, -10);
            super.render(context);
            context.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET*4);
            GSIRenderHelper.renderBasicString(context, "R: ", getBounds().renderBounds().x + 1, redSlider.getBounds().maxBounds().getCentreY() - 9D/2D, ScreenUtils.red_button.rgba, true);
            GSIRenderHelper.renderBasicString(context,"G: ", getBounds().renderBounds().x + 1, greenSlider.getBounds().maxBounds().getCentreY() - 9D/2D, ScreenUtils.green_button.rgba, true);
            GSIRenderHelper.renderBasicString(context,"B: ", getBounds().renderBounds().x + 1, blueSlider.getBounds().maxBounds().getCentreY() - 9D/2D, ScreenUtils.blue_button.rgba, true);
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
        red.setText("" + GSIDesignSettings.selectedColour.getRed());
        green.setText("" + GSIDesignSettings.selectedColour.getGreen());
        blue.setText("" + GSIDesignSettings.selectedColour.getBlue());
    }

    public void updateColourFromButton(ColouredButtonComponent buttonComponent, GSIInteractionHandler handler){
        setTextColour(new ColourProperty(buttonComponent.activatedColour));
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
