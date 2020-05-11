package sonar.logistics.client.vectors;

import sonar.logistics.multiparts.displays.old.info.elements.base.ElementAlignment;

public class Vector2D {

    public double x, y;

    public Vector2D(){}

    public Vector2D(Vector2D v) {
        this(v.x, v.y);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /////

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    /////

    public Vector2D add(double xV, double yV){
        this.x = x + xV;
        this.y = y + yV;
        return this;
    }

    public Vector2D add(Vector2D v){
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    public Vector2D sub(double xV, double yV){
        this.x = x - xV;
        this.y = y - yV;
        return this;
    }

    public Vector2D sub(Vector2D v){
        this.x = x - v.x;
        this.y = y - v.y;
        return this;
    }

    public Vector2D mul(double xV, double yV) {
        this.x = x * xV;
        this.y = y * yV;
        return this;
    }

    public Vector2D mul(Vector2D v) {
        this.x = x * v.x;
        this.y = y * v.y;
        return this;
    }

    /////

    public static Vector2D getSizingFromRatio(Vector2D sizing, Vector2D ratio){
        double actualElementScale = Math.min(sizing.x / ratio.x, sizing.y / ratio.y);
        double actualElementWidth = (ratio.x * actualElementScale);
        double actualElementHeight = (ratio.y * actualElementScale);
        return new Vector2D( actualElementWidth, actualElementHeight);
    }

    public static Vector2D align(Vector2D scale, Vector2D max, ElementAlignment xAlign, ElementAlignment yAlign) {
        double x = xAlign.align(scale.x, max.x);
        double y = yAlign.align(scale.y, max.y);
        return new Vector2D(x, y);
    }

    /////

    public Vector2D copy(){
        return new Vector2D(x, y);
    }

}
