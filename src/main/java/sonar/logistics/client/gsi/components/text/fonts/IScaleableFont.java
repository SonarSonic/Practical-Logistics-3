package sonar.logistics.client.gsi.components.text.fonts;

import net.minecraft.client.gui.fonts.IGlyph;

public interface IScaleableFont {

    IGlyph getGlyph(char character);

    void renderGlyph(IGlyph glyph);

}
