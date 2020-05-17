package sonar.logistics.client.gsi.components.text.api;

public class CursorPoint {

    private boolean isLeading; //isLeading = before the glyph otherwise isTrailing = after the glyph
    private int index; //the index of the clicked glyph, in most cases you should use getInsertionIndex

    public CursorPoint(boolean leading, int index){
        setLeading(leading);
        setIndex(index);
    }

    ////

    public void setIndex(int index) {
        this.index = Math.max(0, index);
    }

    public void setLeading(boolean leading) {
        isLeading = leading;
    }

    ////

    public boolean isLeading() {
        return isLeading;
    }

    public int getCharIndex(){
        return index;
    }

    public int getInsertionIndex(){
        return isLeading ? index : index + 1;
    }

    ////

    ///only for use when the index moved to is known to be visible, e.g. after removing a single glyph
    public void moveLeft(int move){
        if(!isLeading){
            isLeading = true;
            move--;
        }
        setIndex(index - move);
    }

    ///only for use when the index moved to is known to be visible, e.g. after adding a single glyph
    public void moveRight(int move){
        if(isLeading){
            isLeading = false;
            move--;
        }
        setIndex(index + move);
    }

}