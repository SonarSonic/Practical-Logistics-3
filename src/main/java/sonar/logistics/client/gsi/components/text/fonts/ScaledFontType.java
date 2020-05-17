package sonar.logistics.client.gsi.components.text.fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

public abstract class ScaledFontType {

    public static ScaledFontType DEFAULT_MINECRAFT = new ScaledFontType(Minecraft.DEFAULT_FONT_RENDERER_NAME) {
        ResourceLocation loc = Minecraft.DEFAULT_FONT_RENDERER_NAME;

        @Override
        public void initFont(Font font) {
            
        }

        @Override
        public RenderType getRenderType(TexturedGlyph glyph) {
            return glyph.getRenderType(false);
        }

        @Override
        public float getMinimumSpaceSize() {
            return 4.0F;
        }

        @Override
        public int getFontScaling() {
            return 9;
        }

        public int getElementScaling(){
            return 8;
        }
    };

    public static ScaledFontType RALEWAY = new ScaledFontType(new ResourceLocation(PL3.MODID, "ralewayregular")) {
        RenderType type = RenderType.getText(new ResourceLocation(PL3.MODID, "ralewayregular" + "/" + 1));

        @Override
        public void initFont(Font font) {

        }

        @Override
        public RenderType getRenderType(TexturedGlyph glyph) {
            return type;
        }

        @Override
        public float getMinimumSpaceSize() {
            return (4.0F / 9F) * getFontScaling();
        }

        @Override
        public int getFontScaling() {
            return 9;
        }

        @Override
        public int getElementScaling() {
            return (int)(getFontScaling() * (8F/9F));
        }
    };

    Font font;

    public ScaledFontType(ResourceLocation loc){
        this.font = getFont(loc);
        this.initFont(font);
    }

    public Font getFont(){
        return font;
    }

    private Font getDefaultMinecraftFont(){
        return getFont(Minecraft.DEFAULT_FONT_RENDERER_NAME);
    }

    private Font getFont(ResourceLocation loc) {
        FontRenderer renderer = Minecraft.getInstance().getFontResourceManager().getFontRenderer(loc);
        return renderer == null ? getDefaultMinecraftFont() : renderer.font; ///access transformed renderer.font
    }

    public abstract void initFont(Font font);

    public abstract RenderType getRenderType(TexturedGlyph glyph);

    public abstract float getMinimumSpaceSize();

    public abstract int getFontScaling();

    public abstract int getElementScaling();



}
