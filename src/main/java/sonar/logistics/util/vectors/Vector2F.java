package sonar.logistics.util.vectors;

import sonar.logistics.client.gsi.style.ComponentAlignment;

public class Vector2F {

    public float x, y;

    public Vector2F(){}

    public Vector2F(Vector2F v) {
        this(v.x, v.y);
    }

    public Vector2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2F(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }

    /////

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    /////

    public Vector2F add(float xV, float yV){
        this.x = x + xV;
        this.y = y + yV;
        return this;
    }

    public Vector2F add(Vector2F v){
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    public Vector2F sub(float xV, float yV){
        this.x = x - xV;
        this.y = y - yV;
        return this;
    }

    public Vector2F sub(Vector2F v){
        this.x = x - v.x;
        this.y = y - v.y;
        return this;
    }

    public Vector2F mul(float xV, float yV) {
        this.x = x * xV;
        this.y = y * yV;
        return this;
    }

    public Vector2F mul(Vector2F v) {
        this.x = x * v.x;
        this.y = y * v.y;
        return this;
    }

    /////

    public static Vector2F getSizingFromRatio(Vector2F sizing, Vector2F ratio){
        float scalar = Math.min(sizing.x / ratio.x, sizing.y / ratio.y);
        float width = (ratio.x * scalar);
        float height = (ratio.y * scalar);
        return new Vector2F(width, height);
    }

    public static Vector2F align(Vector2F scale, Vector2F max, ComponentAlignment xAlign, ComponentAlignment yAlign) {
        float x = xAlign.align(scale.x, max.x);
        float y = yAlign.align(scale.y, max.y);
        return new Vector2F(x, y);
    }

    /////

    public Vector2F copy(){
        return new Vector2F(x, y);
    }

    ////


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector2F){
            return ((Vector2F) obj).x == x && ((Vector2F) obj).y == y;
        }

        return super.equals(obj);
    }
}
