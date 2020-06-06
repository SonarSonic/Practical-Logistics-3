package sonar.logistics.client.gsi.components.layouts.yoga;

import org.lwjgl.util.yoga.Yoga;
import sonar.logistics.client.gsi.style.properties.UnitLength;

public class YogaStylingDefault extends YogaStylingBase {

    public static final YogaStylingDefault INSTANCE = new YogaStylingDefault();

    {
        //flex
        direction = Yoga.YGDirectionLTR;
        flexDirection = Yoga.YGFlexDirectionRow;
        flexWrap = Yoga.YGWrapNoWrap;

        flexBasis = UnitLength.AUTO;
        flexGrow = 0F;
        flexShrink = 1F;

        //alignment
        justifyContent = Yoga.YGJustifyFlexStart;
        alignItems = Yoga.YGAlignStretch;
        alignSelf = Yoga.YGAlignAuto;
        alignContent = Yoga.YGAlignStretch;


        //layout
        width = UnitLength.MAX_PERCENT;
        height = UnitLength.MAX_PERCENT;

        maxWidth = null;
        maxHeight = null;
        minWidth = null;
        minHeight = null;
        aspectRatio = null;

        //positioning
        paddingRadius = UnitLength.MIN_PIXEL;
        borderRadius = 0F;
        marginRadius = UnitLength.MIN_PIXEL;
        isAbsolute = false;
        left = UnitLength.MIN_PIXEL;
        right = UnitLength.MIN_PIXEL;
        top = UnitLength.MIN_PIXEL;
        bottom = UnitLength.MIN_PIXEL;

    }

}
