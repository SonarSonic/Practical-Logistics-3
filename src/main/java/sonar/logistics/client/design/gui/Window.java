package sonar.logistics.client.design.gui;

/*a simple rescaleable window which has position and size*/
public class Window {

    public double x, y;
    public double width, height;

    public Window(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
