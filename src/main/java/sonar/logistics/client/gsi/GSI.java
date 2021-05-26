package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionHandler;
import sonar.logistics.client.gsi.interactions.resize.ResizeInteraction;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.render.GSIRenderHelper;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.imgui.GSIEditorScreen;
import sonar.logistics.client.nodes.*;
import sonar.logistics.util.vectors.Quad2F;
import sonar.logistics.util.vectors.Vector2F;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class GSI implements IComponentHost, INestedInteractionHandler {

    public GSIInteractionHandler interactionHandler = new GSIInteractionHandler(this, Minecraft.getInstance().player);
    public List<Component> components = new ArrayList<>();
    public List<IInteractionHandler> interactions = new ArrayList<>();
    public Quad2F bounds;

    public AdvancedNodeGraph nodeGraph = new AdvancedNodeGraph();

    @Nullable //if the GSI is a GUI Interface only the display will be null.
    public IDisplay display;

    //set bounds and rebuild should be called
    public GSI(){}

    public GSI(Quad2F bounds){
        this.bounds = bounds;
    }

    public Quad2F getGSIBounds(){
        return bounds;
    }

    public void setBoundsAndRebuild(Quad2F bounds){
        this.bounds = bounds;
        build();
    }

    public void build(){
        //build the components
        components.forEach(c -> c.build(bounds));
        //for rendering components we sort back to front - lowest z first
        components.sort(Comparator.comparingInt(i -> i.getStyling().getZLayer()));
        //for interactions we sort front to back - highest z first
        interactions.sort(Comparator.comparingInt(i -> -((Component)i).getStyling().getZLayer()));
    }

    public void tick(){
        components.forEach(Component::tick);
    }

    public void render(GSIRenderContext context){
        if(isDragging()){
            //dragging is performed by the GSI itself, this allows more consistency between World & Gui interactions
            // it also allows smoother dragging as positions are updated before rendering
            mouseDragged();
        }
        hovered = getHoveredComponent(interactions);

        context.preRender();
        components.forEach(c -> GSIRenderHelper.renderComponent(context, c));
        renderInteraction(context);

        context.postRender();
    }

    //// COMPONENTS \\\\

    public Component addComponent(Component component){
        component.setHost(this);
        components.add(component);
        interactions.add(component);
        nodeGraph.addNode(component);
        return component;
    }

    public Component removeComponent(Component component){
        component.setHost(null);
        components.remove(component);
        interactions.remove(component);
        nodeGraph.removeNode(component);
        return component;
    }

    public Component getComponentAt(Vector2F mouseHit) {
        return getComponent(components, component -> component.getBounds().outerSize().contains(mouseHit));
    }

    @Nullable
    public Component getComponent(List<Component> components, Function<Component, Boolean> filter){
        for (Component component : components) {
            if (filter.apply(component)) {
                List<Component> subComponents = component.getSubComponents();
                if (subComponents != null) {
                    Component result = getComponent(subComponents, filter);
                    if (result != null) {
                        return result;
                    }
                }
                return component;
            }
        }
        return null;
    }

    //// NODES \\\\

    /// INTERACTIONS \\\

    @Override
    public boolean mouseClicked(int button) {
        switch (interactionHandler.getInteractionType()){
            case WORLD_INTERACTION:
                if(interactionHandler.hasShiftDown()) {
                    //TODO REPLACE ME!
                    Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIEditorScreen(this)));

                    //Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIDesignScreen(this)));
                    if(components.isEmpty()) {
                        testStructure();
                    }
                    build();
                    return true;
                }
                break;
            case GUI_INTERACTION:
                break;
            case GUI_EDITING:
                if(resizeInteraction == null){
                    break;
                }
                boolean mouseClicked;
                if(resizeInteraction.isMouseOver() || (mouseClicked = INestedInteractionHandler.super.mouseClicked(button))){
                    if(!isDragging()){
                        isResizing = true;
                        tryStartDragging(button);
                    }
                    return true;
                }
                isResizing = false;
                return mouseClicked;
                /*

                Component hovered = getComponent(components, c -> c.canInteract() && c.getBounds().outerSize().contains(interactionHandler.mousePos));
                if(hovered != null) {
                    setFocused(new ResizeInteraction(hovered));
                    tryStartDragging(button);
                    return true;
                }
                setFocused(null);
                return false;
                 */
        }
        return INestedInteractionHandler.super.mouseClicked(button);
    }


    ///TODO REMOVE ME!
    public void testStructure(){
        components.clear();
        interactions.clear();
        setFocused(null);

        StyledTextComponent lines = new StyledTextComponent();
        lines.getStyling().setSizing(0F, 0F, 1F, 0.5F, Unit.PERCENT);

        lines.getStyling().setBorderWidth(new LengthProperty(Unit.PIXEL, 0.0625F/4F));
        lines.getStyling().setBorderHeight(new LengthProperty(Unit.PIXEL, 0.0625F/4F));

        StyledTextString element = new StyledTextString();

        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.justifyType = LineStyle.JustifyType.JUSTIFY;
        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;
        element.addGlyph(new LineBreakGlyph(false, lineStyle));
        element.addString("Insert text here");

        lines.pages.text = element;

        addComponent(lines);
    }

    private boolean isResizing = false;
    private ResizeInteraction resizeInteraction = null;

    @Override
    public void renderInteraction(GSIRenderContext context) {
        INestedInteractionHandler.super.renderInteraction(context);
        if(interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_EDITING && resizeInteraction != null){
            resizeInteraction.renderInteraction(context);
        }
    }

    @Override
    public void onDragFinished(int button) {
        INestedInteractionHandler.super.onDragFinished(button);
        if(isResizing){
            isResizing = false;
        }
    }

    private IInteractionHandler focused = null;
    private IInteractionHandler hovered = null;

    public void setSelectedComponent(Component component){
        setFocused(component);
    }

    public Component getSelectedComponent(){
        return focused instanceof Component ? (Component)focused : null;
    }

    @Override
    public List<IInteractionHandler> getChildren() {
        return interactions;
    }

    @Override
    public Optional<IInteractionHandler> getFocusedListener() {
        return Optional.ofNullable(isResizing ? resizeInteraction : focused);
    }

    @Override
    public Optional<IInteractionHandler> getHoveredListener() {
        return Optional.ofNullable(hovered);
    }

    @Override
    public void setFocused(IInteractionHandler listener) {
        this.focused = listener;
        if(interactionHandler.getInteractionType() == GSIInteractionHandler.InteractionType.GUI_EDITING && listener instanceof Component){
            resizeInteraction = new ResizeInteraction((Component) listener);
        }else{
            resizeInteraction = null;
        }
    }


    /**returns the first child to return true for isMouseOver, note the interactions are already sorted by z layer.*/
    public <I extends IInteractionHandler> IInteractionHandler getHoveredComponent(List<I> interactions){
        for(IInteractionHandler interaction : interactions){
            if(interaction.canInteract() && interaction.isMouseOver()){
                if(interaction instanceof Component){
                    List<Component> subComponents = ((Component) interaction).getSubComponents();
                    if(subComponents != null && !subComponents.isEmpty()){
                        IInteractionHandler subComponent = getHoveredComponent(subComponents);
                        return subComponent == null ? interaction : subComponent;
                    }
                }
                return interaction;
            }
        }
        return null;
    }

    ///

    private boolean isDragging;

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Override
    public GSI getGSI() {
        return this;
    }

}
