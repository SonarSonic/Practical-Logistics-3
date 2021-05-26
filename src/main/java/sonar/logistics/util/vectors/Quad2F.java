package sonar.logistics.util.vectors;

import sonar.logistics.client.gsi.style.ComponentAlignment;

public class Quad2F {

    public static final Quad2F DEFAULT_PERCENTAGES = new Quad2F(0, 0, 1, 1);

    public float x, y, width, height;

    public Quad2F() {
        super();
    }

    public Quad2F(Quad2F quad) {
        this(quad.x, quad.y, quad.width, quad.height);
    }

    public Quad2F(Vector2F alignment, Vector2F sizing) {
        this(alignment.x, alignment.y, sizing.x, sizing.y);
    }

    public Quad2F(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public Quad2F(double x, double y, double w, double h) {
        this.x = (float)x;
        this.y = (float)y;
        this.width = (float)w;
        this.height = (float)h;
    }

    /////

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public float getMaxX(){
        return x + width;
    }

    public float getMaxY(){
        return y + height;
    }

    public float getCentreX(){
        return x + width / 2;
    }

    public float getCentreY(){
        return y + height / 2;
    }

    /////

    public Vector2F getAlignment(){
        return new Vector2F(x, y);
    }

    public Vector2F getSizing(){
        return new Vector2F(width, height);
    }

    /////

    public Vector2F getTopLeft(){
        return new Vector2F(x, y);
    }

    public Vector2F getTopRight(){
        return new Vector2F(x + width, y);
    }

    public Vector2F getBottomLeft(){
        return new Vector2F(x, y + height);
    }

    public Vector2F getBottomRight(){
        return new Vector2F(x + width, y + height);
    }

    /////

    public Quad2F setX(float x) {
        this.x = x;
        return this;
    }

    public Quad2F setY(float y) {
        this.y = y;
        return this;
    }

    public Quad2F setWidth(float width) {
        this.width = width;
        return this;
    }

    public Quad2F setHeight(float height) {
        this.height = height;
        return this;
    }


    /////

    public Quad2F setAlignment(float x, float y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Quad2F setSizing(float width, float height){
        this.width = width;
        this.height = height;
        return this;
    }

    public Quad2F setMax(float maxX, float maxY){
        this.width = maxX-x;
        this.height = maxY-y;
        return this;
    }

    /////

    public Quad2F factor(float factor){
        this.x *= factor;
        this.y *= factor;
        this.width *= factor;
        this.height *= factor;
        return this;
    }

    public Quad2F translate(float xV, float yV){
        this.x += xV;
        this.y += yV;
        return this;
    }


    public Quad2F translate(Vector2F v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Quad2F scale(float xV, float yV) {
        this.width *= xV;
        this.height *= yV;
        return this;
    }

    public Quad2F scale(Vector2F v) {
        this.width *= v.x;
        this.height *= v.y;
        return this;
    }

    /////

    public boolean isValid(){
        return width != 0 && height != 0;
    }

    public Quad2F flipNegatives(){
        float value = 0;
        if(width < 0){ // flip negative width
            x += width;
            width = -width;
        }

        if(height < 0){ // flip negative height
            y += height;
            height = -height;
        }
        return this;
    }


    /////


    public boolean contains(Vector2F v){
        return containsX(v.x) && containsY(v.y);
    }

    public boolean contains(float xV, float yV){
        return containsX(xV) && containsY(yV);
    }

    public boolean containsX(float xV){
        return width != 0 && xV >= getX() && xV <= getMaxX();
    }

    public boolean containsY(float yV){
        return height != 0 && yV >= getY() && yV <= getMaxY();
    }

    public static boolean contains(float xV, float yV, float wV, float hV, Vector2F v){
        return contains(xV, yV, wV, hV, v.x, v.y);
    }

    public static boolean contains(float xV, float yV, float wV, float hV, float hitX, float hitY){
        return hitX >= xV && hitX <= xV + wV && hitY >= yV && hitY <= yV + hV;
    }

    public boolean inside(Vector2F v){
        return v.x > getX() && v.x < getMaxX() && v.y > getY() && v.y < getMaxY();
    }

    public boolean inside(float xV, float yV){
        return xV > getX() && xV < getMaxX() && yV > getY() && yV < getMaxY();
    }

    public boolean canFit(Quad2F quad){
        return quad.width <= width && quad.height <= height;
    }

    public boolean canFit(float w, float h){
        return w <= width && h <= height;
    }

    public boolean intersects(Quad2F quad2D){
        return intersects(quad2D.x, quad2D.y, quad2D.width, quad2D.height);
    }

    public boolean intersects(float xV, float yV, float wV, float hV){
        if (wV <= 0 || hV <= 0 || width <= 0 || height <= 0) {
            return false;
        }
        float tw = width;
        float th = height;
        float tx = x;
        float ty = y;
        wV += xV;
        hV += yV;
        tw += tx;
        th += ty;
        return ((wV < xV || wV > tx) &&
                (hV < yV || hV > ty) &&
                (tw < tx || tw > xV) &&
                (th < ty || th > yV));
    }

    /////

    public static Quad2F multiply(Quad2F quad2D, Quad2F scalar){
        return new Quad2F(quad2D.x * scalar.x, quad2D.y * scalar.y, quad2D.width * scalar.width, quad2D.height * scalar.height);
    }

    /**this method multiplies the given quad's width / height to obtain the translation and size*/
    public static Quad2F getQuadFromPercentage(Quad2F quad2D, Quad2F percentages){
        return new Quad2F(quad2D.getX() + (quad2D.width * percentages.x), quad2D.getY() + (quad2D.height * percentages.y), quad2D.width * percentages.width, quad2D.height * percentages.height);
    }

    public static Quad2F getQuadFromAbsolutes(Quad2F quad2D, Quad2F scaleables, boolean[] absolutes){
        return new Quad2F(
                absolutes[0] ? quad2D.getX() + scaleables.getX() : quad2D.getX() + (quad2D.width * scaleables.x),
                absolutes[1] ? quad2D.getY() + scaleables.getY() : quad2D.getY() + (quad2D.height * scaleables.y),
                absolutes[2] ? scaleables.getWidth() : quad2D.width * scaleables.width,
                absolutes[3] ? scaleables.getHeight() : quad2D.height * scaleables.height);
    }

    /**aligns the quad within the given alignments, the adjusts the quad it is called on.*/
    public Quad2F align(Quad2F bounds, ComponentAlignment alignX, ComponentAlignment alignY){
        x = alignX.align(width, bounds.getWidth()) + bounds.getX();
        y = alignY.align(height, bounds.getHeight()) + bounds.getY();
        return this;
    }

    public Quad2F copy(){
        return new Quad2F(x, y, width, height);
    }

}
