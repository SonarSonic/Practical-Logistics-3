package sonar.logistics.client.gsi.components.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.api.ITextComponent;
import sonar.logistics.client.gsi.components.AbstractComponent;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.StyledTextWrapper;
import sonar.logistics.client.gsi.components.text.fonts.ScaledFontType;
import sonar.logistics.client.gsi.components.text.glyph.CharGlyph;
import sonar.logistics.client.gsi.components.text.glyph.Glyph;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.render.StyledTextPages;
import sonar.logistics.client.gsi.components.text.render.StyledTextRenderer;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.text.StandardTextInteraction;
import sonar.logistics.client.gsi.interactions.api.IFlexibleInteractionListener;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.interactions.triggers.ITrigger;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

public class TextInputComponent extends AbstractComponent implements ITextComponent, IFlexibleInteractionListener {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.DEFAULT_MINECRAFT;
    public StyledTextPages pages = new StyledTextPages(new StyledTextString());
    public EnumTextInputType inputType = EnumTextInputType.STRING;
    public StandardTextInteraction<ITextComponent> textInteraction = new StandardTextInteraction<>(this);
    public int maxInputLength = -1;

    public ITrigger<TextInputComponent> trigger = null;
    public boolean textChanged;

    public LineStyle inputLineStyle = new LineStyle();
    public GlyphStyle inputGlyphStyle = new GlyphStyle();


    {
        styling.borderSize.getRenderSize(1);
        inputLineStyle.justifyType = LineStyle.JustifyType.JUSTIFY_CENTRE;
        inputLineStyle.wrappingType = LineStyle.WrappingType.WRAP_OFF;
    }

    public TextInputComponent setMaxStringLength(int maxInputLength) {
        this.maxInputLength = maxInputLength;
        return this;
    }

    public TextInputComponent setInputType(EnumTextInputType inputType){
        this.inputType = inputType;
        return this;
    }

    public TextInputComponent setTrigger(ITrigger<TextInputComponent> trigger){
        this.trigger = trigger;
        return this;
    }

    /**if this is called after the component has been built- rebuild should be called*/
    public TextInputComponent setText(String text){
        pages.text.glyphs.clear();
        pages.text.addString(text);
        return this;
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        StyledTextWrapper.INSTANCE.build(pages, fontType, this.bounds.renderBounds(), inputLineStyle, inputGlyphStyle);
        StyledTextWrapper.INSTANCE.alignPages(ComponentAlignment.CENTERED);
    }

    @Override
    public void tick() {
        super.tick();
        if(textChanged){
            if(trigger != null) {
                trigger.trigger(this, getInteractionHandler());
            }
            textChanged = false;
        }
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);

        context.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET);
        GSIRenderHelper.renderColouredRect(context, true, bounds.renderBounds(), ScreenUtils.transparent_grey_bgd.rgba);
        context.matrix.push();
        context.matrix.translate(0, 0, GSIRenderHelper.MIN_Z_OFFSET*2);
        StyledTextRenderer.INSTANCE.renderCurrentPage(context, fontType, pages);
        context.matrix.pop();
    }

    @Override
    public boolean canStyle() {
        return false;
    }

    @Override
    public boolean canAddGlyph(Glyph glyph) {
       if(maxInputLength != -1 && pages.text.glyphs.size() >= maxInputLength){
            return false;
        }
        if(glyph instanceof LineBreakGlyph){
            return false;
        }
        if(glyph instanceof CharGlyph){
            CharGlyph charGlyph = (CharGlyph) glyph;
            return inputType.canAddChar(this, charGlyph.aChar);
        }
        return false;
    }

    @Override
    public void onTextChanged() {
        inputType.reformatText(this);
        textChanged = true;
        rebuild();
    }

    @Override
    public void onStylingChanged() {
        rebuild();
    }

    @Override
    public boolean isMouseOver() {
        return getBounds().maxBounds().contains(getInteractionHandler().mousePos);
    }

    @Override
    public IInteractionListener getInteractionListener() {
        switch (getInteractionHandler().getInteractionType()){
            case WORLD_INTERACTION:
                break;
            case GUI_INTERACTION:
            case GUI_EDITING:
                return textInteraction;
        }
        return null;
    }

    @Override
    public StyledTextPages pages() {
        return pages;
    }

    public enum EnumTextInputType{
        DIGIT_ONLY,
        RGB_COLOUR_VALUE,
        STRING;


        public boolean canAddChar(TextInputComponent component, char ch){
            switch (this){
                case DIGIT_ONLY:
                case RGB_COLOUR_VALUE:
                    return Character.isDigit(ch);
                default:
                    return true;
            }
        }

        public void reformatText(TextInputComponent component){
            switch (this){
                case RGB_COLOUR_VALUE:
                    int rgb = Math.min(255, component.pages.text.getIntegerFromText());
                    component.setText("" + rgb);
                    break;
                default:
                    break;
            }
        }
    }
}
