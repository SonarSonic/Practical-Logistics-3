package sonar.logistics.client.gsi.components.layouts;

import org.lwjgl.util.yoga.Yoga;
import sonar.logistics.PL3;
import sonar.logistics.client.gsi.style.properties.UnitLength;

import javax.annotation.Nullable;

import static org.lwjgl.util.yoga.Yoga.YGNodeStyleSetAspectRatio;

public class YGStyleHelper {

    public static void setFlexBasis(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                Yoga.YGNodeStyleSetFlexBasisAuto(node);
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetFlexBasis(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetFlexBasisPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeWidth(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                Yoga.YGNodeStyleSetWidthAuto(node);
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetWidth(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetWidthPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeHeight(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                Yoga.YGNodeStyleSetHeightAuto(node);
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetHeight(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetHeightPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeMaxWidth(long node, @Nullable UnitLength unit){
        if(unit == null){
            return;
        }
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for maxWidth");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetMaxWidth(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetMaxWidthPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeMaxHeight(long node, @Nullable UnitLength unit){
        if(unit == null){
            return;
        }
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for maxHeight");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetMaxHeight(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetMaxHeightPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeMinWidth(long node, @Nullable UnitLength unit){
        if(unit == null){
            return;
        }
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for minWidth");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetMinWidth(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetMinWidthPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeMinHeight(long node, @Nullable  UnitLength unit){
        if(unit == null){
            return;
        }
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for minHeight");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetMinHeight(node, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetMinHeightPercent(node, (float)unit.value);
                break;
        }
    }

    public static void setNodeAspectRatio(long node, @Nullable Float value){
        if(value == null){
            return;
        }
        YGNodeStyleSetAspectRatio(node, value);
    }

    public static void setPadding(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for padding");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetPadding(node, Yoga.YGEdgeAll, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetPaddingPercent(node, Yoga.YGEdgeAll, (float)unit.value);
                break;
        }
    }

    public static void setBorder(long node, float value){
        Yoga.YGNodeStyleSetBorder(node, Yoga.YGEdgeAll, (float)value);
    }

    public static void setMargin(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                Yoga.YGNodeStyleSetMarginAuto(node, Yoga.YGEdgeAll);
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetMargin(node, Yoga.YGEdgeAll, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetMarginPercent(node, Yoga.YGEdgeAll, (float)unit.value);
                break;
        }
    }

    public static void setPositionLeft(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for positionLeft");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetPosition(node, Yoga.YGEdgeLeft, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetPositionPercent(node, Yoga.YGEdgeLeft, (float)unit.value);
                break;
        }
    }

    public static void setPositionRight(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for positionRight");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetPosition(node, Yoga.YGEdgeRight, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetPositionPercent(node, Yoga.YGEdgeRight, (float)unit.value);
                break;
        }
    }

    public static void setPositionTop(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for positionTop");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetPosition(node, Yoga.YGEdgeTop, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetPositionPercent(node, Yoga.YGEdgeTop, (float)unit.value);
                break;
        }
    }

    public static void setPositionBottom(long node, UnitLength unit){
        switch (unit.unitType){
            case AUTO:
                PL3.LOGGER.error("AUTO is not a valid UnitType for positionBottom");
                break;
            case PIXEL:
                Yoga.YGNodeStyleSetPosition(node, Yoga.YGEdgeBottom, (float)unit.value);
                break;
            case PERCENT:
                Yoga.YGNodeStyleSetPositionPercent(node, Yoga.YGEdgeBottom, (float)unit.value);
                break;
        }
    }
}
