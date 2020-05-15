package sonar.logistics.client.gsi.components.text.api;

public class CursorPoint {

    public boolean isLeading; //isLeading = before the glyph otherwise isTrailing = after the glyph
    public int index; //the index of the clicked glyph, in most cases you should use getInsertionIndex

    public CursorPoint(boolean isLeading, int index){
        this.isLeading = isLeading;
        this.index = index;
    }

    public int getCharIndex(){
        return index;
    }

    public int getInsertionIndex(){
        return isLeading ? index : index + 1;
    }

    public boolean isSelectedGlyph(int glyphIndex){
        return index == glyphIndex;
    }

    ///only for use when the index moved to is known to be visible, e.g. after removing a single glyph
    public void moveLeft(int move){
        if(!isLeading){
            isLeading = true;
            move--;
        }
        index -= move;
    }

    ///only for use when the index moved to is known to be visible, e.g. after adding a single glyph
    public void moveRight(int move){
        if(isLeading){
            isLeading = false;
            move--;
        }
        index += move;
    }

}