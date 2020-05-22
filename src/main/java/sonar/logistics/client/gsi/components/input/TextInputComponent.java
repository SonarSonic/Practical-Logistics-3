package sonar.logistics.client.gsi.components.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sonar.logistics.client.gsi.api.ComponentAlignment;
import sonar.logistics.client.gsi.api.IComponent;
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
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.interactions.DefaultInteraction;
import sonar.logistics.client.gsi.interactions.IInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.vectors.Quad2D;

import javax.annotation.Nonnull;

public class TextInputComponent extends AbstractComponent implements ITextComponent {

    @OnlyIn(Dist.CLIENT)
    public ScaledFontType fontType = ScaledFontType.DEFAULT_MINECRAFT;
    public StyledTextPages pages = new StyledTextPages(new StyledTextString());
    public EnumTextInputType inputType = EnumTextInputType.STRING;
    public int maxInputLength = -1;

    public static LineStyle inputLineStyle;

    static{
        inputLineStyle = new LineStyle();
        inputLineStyle.justifyType = LineStyle.JustifyType.JUSTIFY_CENTRE;
        inputLineStyle.wrappingType = LineStyle.WrappingType.WRAP_OFF;
    }

    @Override
    public void build(Quad2D bounds) {
        super.build(bounds);
        StyledTextWrapper.INSTANCE.build(pages, fontType, this.bounds.renderBounds(), inputLineStyle);
        StyledTextWrapper.INSTANCE.alignPages(ComponentAlignment.CENTERED);
    }

    @Override
    public void render(GSIRenderContext context, DisplayInteractionHandler handler) {
        super.render(context, handler);

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
            return inputType.canAddChar(charGlyph.aChar);
        }
        return false;
    }

    @Override
    public StyledTextPages pages() {
        return pages;
    }

    public enum EnumTextInputType{
        DIGIT_ONLY,
        STRING;

        public boolean canAddChar(char ch){
            switch (this){
                case DIGIT_ONLY:
                    return Character.isDigit(ch);
                default:
                    return true;
            }
        }
    }
}
