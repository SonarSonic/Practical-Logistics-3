package sonar.logistics.client.design.gui;

import net.minecraft.client.gui.IGuiEventListener;
import sonar.logistics.client.design.gui.interactions.ViewportAbstractInteraction;
import sonar.logistics.client.design.gui.interactions.ViewportResizeInteraction;
import sonar.logistics.client.design.gui.interactions.ViewportTextInteraction;
import sonar.logistics.client.design.gui.interactions.ViewportZoomInteraction;
import sonar.logistics.client.design.gui.widgets.GSIViewportWidget;

import java.util.function.Function;

public class GSIDesignSettings {

    //// GSI VIEWPORT \\\\

    public static ViewportInteractSetting viewportInteractSetting = ViewportInteractSetting.RESIZE_COMPONENTS;
    private static ViewportAbstractInteraction viewportInteraction;

    public static void setViewportInteractSetting(ViewportInteractSetting setting) {
        viewportInteractSetting = setting;
    }

    public static void clearViewportInteraction() {
        viewportInteraction = null;
    }

    public static ViewportAbstractInteraction getViewportInteraction(GSIViewportWidget viewportWidget) {
        if (viewportInteraction == null || viewportInteraction.getViewportSetting() != viewportInteractSetting) {
            viewportInteraction = viewportInteractSetting.getInteractionListener(viewportWidget);
        }
        return viewportInteraction;
    }

    public enum ViewportInteractSetting implements IGuiEventListener {
        ZOOM_VIEWPORT(ViewportZoomInteraction::new),
        RESIZE_COMPONENTS(ViewportResizeInteraction::new),
        EDIT_TEXT(ViewportTextInteraction::new);

        private Function<GSIViewportWidget, ViewportAbstractInteraction> func;

        ViewportInteractSetting(Function<GSIViewportWidget, ViewportAbstractInteraction> func) {
            this.func = func;
        }

        public ViewportAbstractInteraction getInteractionListener(GSIViewportWidget viewportWidget) {
            return func.apply(viewportWidget);
        }
    }

    //// SNAPPING \\\\

    public static double snapping = 0;

    public static void setOrResetSnapping(double value) {
        snapping = snapping == value ? 0 : value;
    }

    public static double snapToScaledGrid(double value, double scaled) {
        return snapping == 0 ? value : (snapping * scaled) * (Math.round(value / (snapping * scaled)));
    }

    public static double snapToNormalGrid(double value) {
        return snapping == 0 ? value : snapping * (Math.round(value / snapping));
    }

    //// CURSOR COUNTER \\\\

    private static int cursorCounter;

    public static void tickCursorCounter() {
        cursorCounter ++;
        if(cursorCounter == 100){
            cursorCounter = 1;
        }
    }

    public static boolean canRenderCursor(){
        return cursorCounter / 6 % 2==0;
    }

}
