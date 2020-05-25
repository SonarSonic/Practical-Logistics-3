package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.gsi.components.input.SliderComponent;
import sonar.logistics.client.gsi.components.input.TextInputComponent;
import sonar.logistics.client.gsi.interactions.DraggingInteraction;
import sonar.logistics.client.gsi.interactions.ResizingInteraction;
import sonar.logistics.client.gsi.interactions.api.IInteractionListener;
import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.glyph.LineBreakGlyph;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.interactions.api.INestedInteractionListener;
import sonar.logistics.client.gsi.render.GSIRenderContext;
import sonar.logistics.client.gsi.properties.ScaleableBounds;
import sonar.logistics.client.gui.GSIDesignScreen;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.client.vectors.Vector2D;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GSI implements INestedInteractionListener {

    public GSIInteractionHandler interactionHandler = new GSIInteractionHandler(this, Minecraft.getInstance().player);
    private List<IComponent> components = new ArrayList<>();
    private List<IInteractionListener> interactions = new ArrayList<>();
    private Quad2D bounds;

    public GSI(Quad2D bounds){
        this.bounds = bounds;
    }

    public Quad2D getGSIBounds(){
        return bounds;
    }

    public void build(){
        components.forEach(c -> c.build(bounds));
    }

    public void render(GSIRenderContext context){
        context.preRender();
        components.forEach(c -> c.render(context));
        renderInteraction(context);
        context.postRender();
    }

    public IComponent addComponent(IComponent component){
        component.setGSI(this);
        components.add(component);
        if(component instanceof  IInteractionListener){
            interactions.add((IInteractionListener)component);
        }
        return component;
    }

    public IComponent removeComponent(IComponent component){
        component.setGSI(null);
        components.remove(component);
        if(component instanceof  IInteractionListener){
            interactions.remove(component);
        }
        return component;
    }

    public IComponent getComponentAt(Vector2D mouseHit) {
        return getComponent(components, component -> component.getBounds().maxBounds().contains(mouseHit));
    }

    @Nullable
    public IComponent getComponent(List<IComponent> components, Function<IComponent, Boolean> filter){
        for (IComponent component : components) {
            if (filter.apply(component)) {
                List<IComponent> subComponents = component.getSubComponents();
                if (subComponents != null) {
                    IComponent result = getComponent(subComponents, filter);
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
                    Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIDesignScreen(this)));
                    testStructure();
                    build();
                    return true;
                }
            case GUI_INTERACTION:
                break;
            case GUI_EDITING:
                break;
            case GUI_RESIZING:
                if(focused instanceof ResizingInteraction && focused.mouseClicked(button)){
                    return true;
                }
                IComponent hovered = getComponent(components, c -> c.getBounds().maxBounds().contains(interactionHandler.mousePos));
                if(hovered != null) {
                    this.setFocused(new ResizingInteraction(hovered));
                    return true;
                }
                setFocused(null);
                return false;
        }
        return INestedInteractionListener.super.mouseClicked(button);
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
        lines.setBounds(new ScaleableBounds(new Quad2D(0, 0, 1, 0.5)));

        lines.styling.marginWidth.value = 0.0625F/2;
        lines.styling.marginHeight.value = 0.0625F/2;

        lines.styling.borderSize.value = 0.0625F/4;
        lines.styling.borderPadding.value = 0.0625F/4;
        StyledTextString element = new StyledTextString();

        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.justifyType = LineStyle.JustifyType.JUSTIFY;
        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;
        element.addGlyph(new LineBreakGlyph(false, lineStyle));
        element.addString("Insert text here");

        lines.pages.text = element;

        addComponent(lines);

        TextInputComponent textInput = new TextInputComponent();
        textInput.setBounds(new ScaleableBounds(new Quad2D(0, 0.5, 1, 0.2)));
        textInput.maxInputLength = 3;
        textInput.inputType = TextInputComponent.EnumTextInputType.DIGIT_ONLY;
        addComponent(textInput);


        SliderComponent slider = new SliderComponent(0.5);
        slider.setBounds(new ScaleableBounds(new Quad2D(0, 0.7, 1, 0.3)));
        addComponent(slider);

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
    public IInteractionListener getFocused() {
        return focused;
    }


    @Override
    public void setFocused(IInteractionListener listener) {
        this.focused = listener;
    }
}
