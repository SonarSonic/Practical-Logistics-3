package sonar.logistics.client.gsi.components.layouts.yoga;

import org.lwjgl.util.yoga.Yoga;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.components.layouts.Layout;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.util.yoga.Yoga.*;

public class YogaLayout extends Layout {

    public static YogaLayout INSTANCE = new YogaLayout();

    @Override
    public void buildComponents(Quad2D bounds, List<IComponent> components){
        long rootNode = YGNodeNew();
        prepareNodeFromStyle(YogaStylingDefault.INSTANCE, rootNode);
        Yoga.YGNodeStyleSetWidth(rootNode, (float)bounds.getWidth());
        Yoga.YGNodeStyleSetHeight(rootNode, (float)bounds.getHeight());
        Yoga.YGNodeStyleSetDisplay(rootNode, YGDisplayFlex);

        List<Long> childNodes = new ArrayList<>();
        for(IComponent component : components){
            long childNode = YGNodeNew();
            prepareNodeFromStyle(((YogaRectTest)component).flex, childNode);
            YGNodeInsertChild(rootNode, childNode, childNodes.size());
            childNodes.add(childNode);
        }

        YGNodeCalculateLayout(rootNode, (float)bounds.getWidth(), (float)bounds.getHeight(), YGDirectionLTR);

        // apply to components
        for (int i = 0; i < components.size(); i++) {
            IComponent childComponent = components.get(i);
            Long yogaNode = childNodes.get(i);

            float left = Yoga.YGNodeLayoutGetLeft(yogaNode);
            float top = Yoga.YGNodeLayoutGetTop(yogaNode);
            float width = Yoga.YGNodeLayoutGetWidth(yogaNode);
            float height = Yoga.YGNodeLayoutGetHeight(yogaNode);

            childComponent.build(new Quad2D(left, top, width, height));
        }

        // free memory
        childNodes.forEach(Yoga::YGNodeFree);
        Yoga.YGNodeFree(rootNode);
    }


    public void prepareNodeFromStyle(YogaStylingBase styling, long node){

        //flex
        YGNodeStyleSetDirection(node, styling.getDirection());
        YGNodeStyleSetFlexDirection(node, styling.getFlexDirection());
        YGNodeStyleSetFlexWrap(node, styling.getFlexWrap());

        YGStyleHelper.setFlexBasis(node, styling.getFlexBasis());
        YGNodeStyleSetFlexGrow(node, styling.getFlexGrow());
        YGNodeStyleSetFlexShrink(node, styling.getFlexShrink());

        //alignment
        YGNodeStyleSetJustifyContent(node, styling.getJustifyContent());
        YGNodeStyleSetAlignItems(node, styling.getAlignItems());
        YGNodeStyleSetAlignSelf(node, styling.getAlignSelf());
        YGNodeStyleSetAlignContent(node, styling.getAlignContent());

        //layout
        YGStyleHelper.setNodeWidth(node, styling.getWidth());
        YGStyleHelper.setNodeHeight(node, styling.getHeight());
        YGStyleHelper.setNodeMaxWidth(node, styling.getMaxWidth());
        YGStyleHelper.setNodeMaxHeight(node, styling.getMaxHeight());
        YGStyleHelper.setNodeMinWidth(node, styling.getMinWidth());
        YGStyleHelper.setNodeMinHeight(node, styling.getMinHeight());
        YGStyleHelper.setNodeAspectRatio(node, styling.getAspectRatio());

        //positioning
        YGStyleHelper.setPadding(node, styling.getPaddingRadius());
        YGStyleHelper.setBorder(node, styling.getBorderRadius());
        YGStyleHelper.setMargin(node, styling.getMarginRadius());
        YGNodeStyleSetPositionType(node, styling.isAbsolute() ? 1 : 0);
        YGStyleHelper.setPositionLeft(node, styling.getLeft());
        YGStyleHelper.setPositionRight(node, styling.getRight());
        YGStyleHelper.setPositionTop(node, styling.getTop());
        YGStyleHelper.setPositionBottom(node, styling.getBottom());
    }


}
