package sonar.logistics.client.gsi.components.groups.interfaces;

import sonar.logistics.client.gsi.components.buttons.TextButtonComponent;
import sonar.logistics.client.gsi.components.groups.AbstractGroup;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.UnitType;
import sonar.logistics.client.vectors.Quad2D;

public abstract class SliderOptionGroup<O extends Object> extends AbstractGroup {

    public String optionName;

    protected O optionValue;

    ///

    protected SliderComponent slider;
    protected TextInputComponent input;
    protected TextButtonComponent reset;

    public SliderOptionGroup(String optionName){
        this.optionName = optionName;
    }

    /**called at the end of constructor by overriding class, TODO proper option init phase.*/
    public void initOption(){
        addComponent(slider = new SliderComponent().setTrigger(this::updateOptionFromSlider)).getStyling().setSizing(0.2, 0.5, 0.6, 0.5, UnitType.PERCENT);
        addComponent(input = new TextInputComponent().setInputType(getInputType()).setTrigger(this::updateOptionFromTextInput)).getStyling().setSizing(0.8, 5/12D, 0.2, 1/6D, UnitType.PERCENT);
        addComponent(reset = new TextButtonComponent("\u21BA", (b, h) -> {})).getStyling().setSizing(0.8, 0.0, 0.2, 1.0, UnitType.PERCENT);
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        subComponents.forEach(component ->  component.build(getBounds().innerSize()));
    }

    @Override
    public void render(GSIRenderContext context) {
        if(isVisible) {
            GSIRenderHelper.renderComponentBackground(context, bounds, styling);
            GSIRenderHelper.renderComponentBorder(context, bounds, styling);
            subComponents.forEach(component -> component.render(context));
            GSIRenderHelper.renderBasicString(context, optionName, getBounds().outerSize().getX() + 2, getBounds().outerSize().getY() + 2, -1, false);
        }
    }

    public void updateOptionFromSlider(SliderComponent sliderComponent, GSIInteractionHandler interactionHandler){
        optionValue = getOptionFromSlider(sliderComponent);
        updateTextInput();
    }

    public void updateOptionFromTextInput(TextInputComponent textInputComponent, GSIInteractionHandler interactionHandler){
        optionValue = getOptionFromTextInput(textInputComponent);
        updateSlider();
    }

    public void updateSlider(){
        slider.sliderValue = getSliderValueFromOption(optionValue);
    }

    public void updateTextInput(){
        input.setText("" + optionValue);
    }

    ///

    public abstract O getOptionFromTextInput(TextInputComponent textInputComponent);

    public abstract String getTextValueFromOption(O option);

    public abstract TextInputComponent.EnumTextInputType getInputType();

    ///

    public abstract O getOptionFromSlider(SliderComponent slider);

    public abstract double getSliderValueFromOption(O option);


    public static class RangedInteger extends SliderOptionGroup<Integer>{

        public int maxValue, minValue;

        public RangedInteger(String optionName, int tempValue, int minValue, int maxValue) {
            super(optionName);
            this.optionValue = tempValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.initOption();
        }

        @Override
        public void initOption() {
            super.initOption();
            slider.stepSize = 1D/(maxValue - minValue);
        }

        @Override
        public Integer getOptionFromTextInput(TextInputComponent textInputComponent) {
            return textInputComponent.text().getIntegerFromText();
        }

        @Override
        public String getTextValueFromOption(Integer option) {
            return option.toString();
        }

        @Override
        public TextInputComponent.EnumTextInputType getInputType() {
            return TextInputComponent.EnumTextInputType.DIGIT_ONLY;
        }

        @Override
        public Integer getOptionFromSlider(SliderComponent slider) {
            return (int)slider.getRangedValue(minValue, maxValue);
        }

        @Override
        public double getSliderValueFromOption(Integer option) {
            return (option.doubleValue() - minValue) / (maxValue - minValue);
        }
    }

}
