package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.client.gsi.components.image.EnumImageFillType;
import sonar.logistics.client.gsi.components.text.IComponentHost;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.components.image.SimpleImageComponent;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.interactions.resize.ResizingInteraction;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.gui.GSIDesignScreen;
import sonar.logistics.util.vectors.Quad2D;
import sonar.logistics.util.vectors.Vector2D;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GSI implements IComponentHost {

    public GSIInteractionHandler interactionHandler = new GSIInteractionHandler(this, Minecraft.getInstance().player);
    private List<Component> components = new ArrayList<>();
    private List<IInteractionListener> interactions = new ArrayList<>();
    private Quad2D bounds;

    @Nullable //if the GSI is a GUI Interface only the display will be null.
    public IDisplay display;

    //set bounds and rebuild should be called
    public GSI(){}

    public GSI(Quad2D bounds){
        this.bounds = bounds;
    }

    public Quad2D getGSIBounds(){
        return bounds;
    }

    public void setBoundsAndRebuild(Quad2D bounds){
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
        context.preRender();
        components.forEach(c -> c.render(context));
        renderInteraction(context);
        context.postRender();
    }

    public Component addComponent(Component component){
        component.setHost(this);
        components.add(component);
        if(component instanceof  IInteractionListener){
            interactions.add((IInteractionListener)component);
        }
        return component;
    }

    public Component removeComponent(Component component){
        component.setHost(null);
        components.remove(component);
        if(component instanceof  IInteractionListener){
            interactions.remove((IInteractionListener)component);
        }
        return component;
    }

    public Component getComponentAt(Vector2D mouseHit) {
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


    @Override
    public boolean mouseClicked(int button) {
        switch (interactionHandler.getInteractionType()){
            case WORLD_INTERACTION:
                if(interactionHandler.hasShiftDown()) {
                    //TODO REPLACE ME!
                    Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIDesignScreen(this)));
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
                break;
            case GUI_RESIZING:
                if(focused instanceof ResizingInteraction && focused.isMouseOver()){
                    if(!isDragging()){
                        tryStartDragging(button);
                    }
                    return true;
                }
                Component hovered = getComponent(components, c -> c.getBounds().outerSize().contains(interactionHandler.mousePos));
                if(hovered != null) {
                    setFocused(new ResizingInteraction(hovered));
                    tryStartDragging(button);
                    return true;
                }
                setFocused(null);
                return false;
        }
        return IComponentHost.super.mouseClicked(button);
    }


    ///TODO REMOVE ME!
    public void testStructure(){
        components.clear();
        interactions.clear();
        setFocused(null);
        //addComponent(new ButtonComponent(0, 176, 0));

        //components.add(new IconButtonComponent(EnumButtonIcons.MODE_SELECT, EmptyTrigger.INSTANCE));

        //addComponent(new IconButtonComponent(EnumButtonIcons.STYLE_BOLD, new Trigger((b, h) -> GSIDesignSettings.toggleBoldStyling(), (b, h) -> GSIDesignSettings.glyphStyle.bold)));


        StyledTextComponent lines = new StyledTextComponent();
        lines.getStyling().setSizing(0, 0, 1, 0.5, Unit.PERCENT);

        lines.getStyling().setBorderWidth(new LengthProperty(Unit.PIXEL, 0.0625/4));
        lines.getStyling().setBorderHeight(new LengthProperty(Unit.PIXEL, 0.0625/4));

        StyledTextString element = new StyledTextString();

        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.justifyType = LineStyle.JustifyType.JUSTIFY;
        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;
        element.addGlyph(new LineBreakGlyph(false, lineStyle));
        element.addString("Insert text here");

        lines.pages.text = element;

        addComponent(lines);

        SliderComponent slider = new SliderComponent();
        slider.getStyling().setSizing(0, 0.5, 1, 0.2, Unit.PERCENT);
        addComponent(slider);


        SimpleImageComponent imageComponent = new SimpleImageComponent(new ResourceLocation("minecraft", "textures/block/bedrock.png"), EnumImageFillType.IMAGE_FILL);
        imageComponent.getStyling().setSizing(0, 0.7, 1, 0.3, Unit.PERCENT);
        addComponent(imageComponent);

        /*
        GridContainer grid = new GridContainer();
        grid.setBounds(new ScaleableBounds(new Quad2D(0, 0.5, 1, 0.5)));

        grid.styling.marginWidth.value = 0.0625F/2;
        grid.styling.marginHeight.value = 0.0625F/2;

        grid.styling.borderSize.value = 0.0625F/4;
        grid.styling.borderPadding.value = 0.0625F/4;
        grid.setGridSize(3, 3);

        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(Items.STONE), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(PL3Items.SIGNALLING_PLATE), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(PL3Items.SAPPHIRE_GEM), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(PL3Items.STONE_PLATE), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(Items.DIAMOND), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(Items.ACACIA_LEAVES), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(Items.ACACIA_PLANKS), 200)));
        grid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(Items.BELL), 200)));

        addComponent(grid);

         */
        /*
        GridContainer subGrid = new GridContainer();
        subGrid.alignment.setAlignmentPercentages(new Vec3d(0, 0.5, 0), new Vec3d(1, 0.5 , 1));
        subGrid.setGridSize(2, 1);

        subGrid.addComponent(new ElementComponent(new ItemStackElement(new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK), 200)));
        addComponent(subGrid);
        */
        /*

        GridContainer subsubGrid = new GridContainer(subGrid);
        subGrid.addComponent(subsubGrid);
        subsubGrid.setGridSize(3, 3);

        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.STONE), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(PL3Items.SIGNALLING_PLATE), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(PL3Items.SAPPHIRE_GEM), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(PL3Items.STONE_PLATE), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.DIAMOND), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.ACACIA_LEAVES), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.ACACIA_PLANKS), 200)));
        subsubGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.BELL), 200)));




        GridContainer subsubsubGrid = new GridContainer(subsubGrid);
        subsubGrid.addComponent(subsubsubGrid);
        subsubsubGrid.setGridSize(3, 3);

        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(Items.STONE), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(PL3Items.SIGNALLING_PLATE), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(PL3Items.SAPPHIRE_GEM), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(PL3Items.STONE_PLATE), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(Items.DIAMOND), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(Items.ACACIA_LEAVES), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(Items.ACACIA_PLANKS), 200)));
        subsubsubGrid.addComponent(new ElementComponent(subsubsubGrid, new ItemStackElement(new ItemStack(Items.BELL), 200)));

        subGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.ENDER_PEARL), 200)));
        subGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.CACTUS), 200)));
        subGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.BROWN_BED), 200)));
        subGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.NETHER_BRICK), 200)));
        subGrid.addComponent(new ElementComponent(subsubGrid, new ItemStackElement(new ItemStack(Items.RABBIT), 200)));



        addComponent(subGrid);
        */
    }



    public boolean toggle(Object source, int triggerId) {
        return false;
    }

    public boolean isActive(Object source, int triggerId) {
        return false;
    }

    ///

    private IInteractionListener focused = null;

    @Override
    public List<IInteractionListener> getChildren() {
        return interactions;
    }

    @Nullable
    @Override
    public Optional<IInteractionListener> getFocusedListener() {
        return Optional.ofNullable(focused);
    }


    @Override
    public void setFocused(IInteractionListener listener) {
        this.focused = listener;
    }

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
