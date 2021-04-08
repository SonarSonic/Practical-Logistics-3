package sonar.logistics.client.gsi.components.buttons;

public enum EnumButtonIcons {

    MODE_SELECT(1, 1),
    MODE_MOVE(1, 2),
    MODE_EDIT_TEXT(1, 3),

    ///

    GRID_NORMAL(2, 1),

    ///

    JUSTIFY_LEFT(3, 1),
    JUSTIFY_CENTRE(3, 2),
    JUSTIFY_RIGHT(3, 3),
    JUSTIFY(3, 4),

    ///

    STYLE_BOLD(4, 1),
    STYLE_ITALIC(4, 2),
    STYLE_UNDERLINE(4, 3),
    STYLE_STRIKETHROUGH(4, 4),
    STYLE_TEXT_COLOUR(4, 5),
    STYLE_OBFUSCATE(4, 6),
    STYLE_SHADOW(4, 7),
    CLEAR_FORMATTING(4, 8),

    ///

    BULLET_POINT_TOGGLE(5, 1),

    ///

    DECREASE_FONT_SIZE(6, 1),
    INCREASE_FONT_SIZE(6, 2),

    ///

    DROPDOWN_ARROW(15, 1, 8, 16),

    ///

    EMPTY_ICON(15,15);

    int x;
    int y;
    int width;
    int height;

    EnumButtonIcons(int x, int y){
        this(x, y, 16, 16);
    }

    EnumButtonIcons(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getUVLeft(){
        return x*16;
    }

    public int getUVTop(){
        return y*16;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

}
