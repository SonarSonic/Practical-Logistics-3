package sonar.logistics.client.gsi.components.shapes;

import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.nodes.Graph;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.util.vectors.Quad2F;

public class ProgressBar extends Component {

    @Override
    public void setupNodePins() {
        super.setupNodePins();
        getInputPins().add(new Graph.DataPin(this, "Progress", DataType.getDataType(Integer.class), Graph.PinKind.Input));
        getInputPins().add(new Graph.DataPin(this, "Maximum", DataType.getDataType(Integer.class), Graph.PinKind.Input));
    }

    @Override
    public void render(GSIRenderContext context) {
        super.render(context);
        Quad2F quad2D = bounds.innerSize().copy();
        quad2D.width *=
        GSIRenderHelper.renderColouredRect(context, true, bounds.innerSize(), ScreenUtils.transparent_blue_bgd.rgba);
    }

}
