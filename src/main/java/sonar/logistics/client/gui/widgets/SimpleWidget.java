package sonar.logistics.client.gui.widgets;

import sonar.logistics.client.gui.api.ISimpleWidget;
import sonar.logistics.client.vectors.Quad2D;

public abstract class SimpleWidget implements ISimpleWidget {

    public Quad2D quad;

    public SimpleWidget(double x, double y, double w, double h){
        this(new Quad2D(x, y, w, h));
    }

    public SimpleWidget(Quad2D quad){
        this.quad = quad;
    }

    @Override
    public Quad2D getQuad() {
        return quad;
    }
}
