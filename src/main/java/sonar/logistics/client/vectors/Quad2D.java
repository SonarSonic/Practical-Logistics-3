package sonar.logistics.client.vectors;

import sonar.logistics.common.multiparts.displays.old.info.elements.base.ElementAlignment;

public class Quad2D {

    public static final Quad2D DEFAULT_PERCENTAGES = new Quad2D(0, 0, 1, 1);

    public double x, y, width, height;

    public Quad2D() {
        super();
    }

    public Quad2D(Quad2D quad) {
        this(quad.x, quad.y, quad.width, quad.height);
    }

    public Quad2D(Vector2D alignment, Vector2D sizing) {
        this(alignment.x, alignment.y, sizing.x, sizing.y);
    }

    public Quad2D(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /////

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    public double getMaxX(){
        return x + width;
    }

    public double getMaxY(){
        return y + height;
    }

    public double getCentreX(){
        return x + width / 2;
    }

    public double getCentreY(){
        return y + height / 2;
    }

    /////

    public Vector2D getAlignment(){
        return new Vector2D(x, y);
    }

    public Vector2D getSizing(){
        return new Vector2D(width, height);
    }

    /////

    public Vector2D getTopLeft(){
        return new Vector2D(x, y);
    }

    public Vector2D getTopRight(){
        return new Vector2D(x + width, y);
    }

    public Vector2D getBottomLeft(){
        return new Vector2D(x, y + height);
    }

    public Vector2D getBottomRight(){
        return new Vector2D(x + width, y + height);
    }

    /////

    public Quad2D setAlignment(double x, double y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Quad2D setSizing(double width, double height){
        this.width = width;
        this.height = height;
        return this;
    }

    public Quad2D setMax(double maxX, double maxY){
        this.width = maxX-x;
        this.height = maxY-y;
        return this;
    }

    /////

    public Quad2D factor(double factor){
        this.x *= factor;
        this.y *= factor;
        this.width *= factor;
        this.height *= factor;
        return this;
    }

    public Quad2D translate(double xV, double yV){
        this.x += xV;
        this.y += yV;
        return this;
    }


    public Quad2D translate(Vector2D v){
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Quad2D scale(double xV, double yV) {
        this.width *= xV;
        this.height *= yV;
        return this;
    }

    public Quad2D scale(Vector2D v) {
        this.width *= v.x;
        this.height *= v.y;
        return this;
    }

    /////

    public boolean isValid(){
        return width != 0 && height != 0;
    }

    public Quad2D flipNegatives(){
        double value = 0;
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


    public boolean contains(Vector2D v){
        return containsX(v.x) && containsY(v.y);
    }

    public boolean contains(double xV, double yV){
        return containsX(xV) && containsY(yV);
    }

    public boolean containsX(double xV){
        return width != 0 && xV >= getX() && xV <= getMaxX();
    }

    public boolean containsY(double yV){
        return height != 0 && yV >= getY() && yV <= getMaxY();
    }

    public static boolean contains(double xV, double yV, double wV, double hV, Vector2D v){
        return contains(xV, yV, wV, hV, v.x, v.y);
    }

    public static boolean contains(double xV, double yV, double wV, double hV, double hitX, double hitY){
        return hitX >= xV && hitX <= xV + wV && hitY >= yV && hitY <= yV + hV;
    }

    public boolean inside(Vector2D v){
        return v.x > getX() && v.x < getMaxX() && v.y > getY() && v.y < getMaxY();
    }

    public boolean inside(double xV, double yV){
        return xV > getX() && xV < getMaxX() && yV > getY() && yV < getMaxY();
    }

    public boolean canFit(Quad2D quad){
        return quad.width <= width && quad.height <= height;
    }

    public boolean canFit(double w, double h){
        return w <= width && h <= height;
    }

    public boolean intersects(Quad2D quad2D){
        return intersects(quad2D.x, quad2D.y, quad2D.width, quad2D.height);
    }

    public boolean intersects(double xV, double yV, double wV, double hV){
        if (wV <= 0 || hV <= 0 || width <= 0 || height <= 0) {
            return false;
        }
        double tw = width;
        double th = height;
        double tx = x;
        double ty = y;
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

    public static Quad2D multiply(Quad2D quad2D, Quad2D scalar){
        return new Quad2D(quad2D.x * scalar.x, quad2D.y * scalar.y, quad2D.width * scalar.width, quad2D.height * scalar.height);
    }

    /**this method multiplies the given quad's width / height to obtain the translation and size*/
    public static Quad2D getQuadFromPercentage(Quad2D quad2D, Quad2D percentages){
        return new Quad2D(quad2D.getX() + (quad2D.width * percentages.x), quad2D.getY() + (quad2D.height * percentages.y), quad2D.width * percentages.width, quad2D.height * percentages.height);
    }

    /**aligns the quad within the given alignments, the adjusts the quad it is called on.*/
    public Quad2D align(Quad2D bounds, ElementAlignment alignX, ElementAlignment alignY){
        x = alignX.align(width, bounds.getWidth()) + bounds.getX();
        y = alignY.align(height, bounds.getHeight()) + bounds.getY();
        return this;
    }

    public Quad2D copy(){
        return new Quad2D(x, y, width, height);
    }

}
