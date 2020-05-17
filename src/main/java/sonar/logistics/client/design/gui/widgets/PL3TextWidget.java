package sonar.logistics.client.design.gui.widgets;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.FontRenderer;
import sonar.logistics.client.design.gui.ScreenUtils;

/**straight from Flux Networks*/
public class PL3TextWidget extends TextFieldWidget {

    private String origin;
    private String extraText;
    private int textWidth;
    private boolean hexOnly;
    private FontRenderer font;

    ///digits
    private boolean digitsOnly;
    private long maxValue = Integer.MAX_VALUE;
    private boolean allowNegatives = false;

    private int outlineColor = 0xffb4b4b4;
    private static final int boxColor = ScreenUtils.transparent_disabled_button.rgba;

    public PL3TextWidget(String text, FontRenderer fontRenderer, int x, int y, int par5Width, int par6Height, int width) {
        super(fontRenderer, x + width, y, par5Width - width, par6Height, "");
        this.extraText = text;
        this.textWidth = width;
        this.font = fontRenderer;
    }

    public static PL3TextWidget create(String text, FontRenderer fontRenderer, int x, int y, int width, int height) {
        return new PL3TextWidget(text, fontRenderer, x, y, width, height, fontRenderer.getStringWidth(text));
    }

    public int getIntegerFromText(boolean allowNegatives) {
        if(getText().isEmpty() || getText().equals("-")){
            return 0;
        }
        int parseInt = Integer.parseInt(getText());
        return allowNegatives ?  parseInt : Math.max(parseInt, 0);
    }

    public long getLongFromText(boolean allowNegatives) {
        if(getText().isEmpty() || getText().equals("-")){
            return 0;
        }
        long parseLong = Long.parseLong(getText());
        return allowNegatives ? parseLong : Math.max(parseLong, 0);
    }

    public int getIntegerFromHex() {
        return Integer.parseInt(getText(), 16);
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        this.setEnableBackgroundDrawing(true);
        if (this.getVisible()) {
            fill(this.x - textWidth - 1, this.y - 1, this.x + this.width + 1, this.y, outlineColor);
            fill(this.x - textWidth - 1, this.y + this.height, this.x + this.width + 1, this.y + this.height + 1, outlineColor);
            fill(this.x - textWidth - 1, this.y, this.x - textWidth, this.y + this.height, outlineColor);
            fill(this.x + width, this.y, this.x + this.width + 1, this.y + this.height, outlineColor);
            fill(this.x - textWidth, this.y, this.x + this.width, this.y + this.height, boxColor);
        }
        this.setEnableBackgroundDrawing(false);
        x += 4;
        y += (this.height - 8) / 2;

        super.renderButton(mouseX, mouseY, partialTicks);


        font.drawString(extraText, x - textWidth, y, outlineColor);
        x -= 4;
        y -= (this.height - 8) / 2;
    }

    @Override
    public void writeText(String textToWrite) {
        if(digitsOnly) {
            for(int i = 0; i < textToWrite.length(); i++) {
                char c = textToWrite.charAt(i);
                if(!Character.isDigit(c)) {
                    if(getText().isEmpty()) {
                        if(c != '-') {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
        if(hexOnly) {
            for(int i = 0; i < textToWrite.length(); i++) {
                char c = textToWrite.charAt(i);
                if(c == '-') {
                    return;
                }
            }
            String origin = getText();
            super.writeText(textToWrite);
            try {
                Integer.parseInt(getText(), 16);
            } catch (final NumberFormatException ignored) {
                setText(origin);
            }
            return;
        }
        super.writeText(textToWrite);
    }



    @Override
    public void setFocused2(boolean isFocusedIn) {
        super.setFocused2(isFocusedIn);
        if(digitsOnly) {
            if(isFocusedIn) {
                origin = getText();
            } else {
                try {
                    setText(String.valueOf(getValidLong()));
                } catch (final NumberFormatException ignored) {
                    setText(origin);
                    System.out.println(ignored.getMessage());
                }
            }
        }
    }

    public long getValidLong(){
        return Math.min(getLongFromText(allowNegatives), maxValue);
    }

    public int getValidInt(){
        return (int)Math.min(getValidLong(), Integer.MAX_VALUE);
    }

    public PL3TextWidget setOutlineColor(int color) {
        this.outlineColor = color;
        return this;
    }

    public PL3TextWidget setTextInvisible() {
        this.setTextFormatter(PL3TextWidget::getInvisibleText);
        return this;
    }

    public static String getInvisibleText(String string, int cursorPos) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < string.length(); i++) {
            builder.append("*");
        }
        return builder.toString();
    }

    public PL3TextWidget setDigitsOnly() {
        this.digitsOnly = true;
        return this;
    }

    public PL3TextWidget setAllowNegatives(boolean allowNegatives){
        this.allowNegatives = allowNegatives;
        return this;
    }

    public PL3TextWidget setMaxValue(long max){
        this.maxValue = max;
        return this;
    }

    public PL3TextWidget setHexOnly() {
        this.hexOnly = true;
        return this;
    }

}
