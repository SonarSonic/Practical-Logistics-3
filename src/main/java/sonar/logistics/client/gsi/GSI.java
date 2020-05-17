package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import sonar.logistics.client.design.gui.GSIDesignScreen;
import sonar.logistics.client.gsi.api.IInteractionListener;
import sonar.logistics.client.gsi.api.INestedInteractionListener;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.ButtonComponent;
import sonar.logistics.client.gsi.context.DisplayInteractionHandler;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.vectors.VectorHelper;
import sonar.logistics.client.vectors.Vector2D;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//TODO SYNC PACKETS & SAVING
public class GSI implements INestedInteractionListener {


    public List<IScaleableComponent> components = new ArrayList<>();
    public boolean queuedRebuild = true;
    public IDisplay display;

    public GSI(IDisplay display){
        this.display = display;
    }

    private DisplayInteractionHandler defaultInteraction = null;

    public DisplayInteractionHandler getDefaultInteractionHandler(PlayerEntity player){
        if(defaultInteraction == null){
            defaultInteraction = new DisplayInteractionHandler(this, Minecraft.getInstance().player, false);
        }
        Vector2D clickPosition = VectorHelper.getEntityLook(player, display, 8);
        defaultInteraction.update(clickPosition);
        return defaultInteraction;
    }

    @Override
    public boolean mouseClicked(DisplayInteractionHandler handler, int button) {
        if(!handler.isUsingGui && handler.hasShiftDown()){
            Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIDesignScreen(this, Minecraft.getInstance().player)));
            testStructure();
            rebuild();
            return true;
        }
        return INestedInteractionListener.super.mouseClicked(handler, button);
    }

    public void render(ScaleableRenderContext context, DisplayInteractionHandler interact){
        if(queuedRebuild){
            rebuild();
            queuedRebuild = false;
        }

        context.preRender();
        components.forEach(c -> c.render(context, interact));
        context.postRender();
    }

    public void queueRebuild(){
        queuedRebuild = true;
    }

    ///TODO REMOVE ME!
    public void testStructure(){
        components.clear();
        addComponent(new ButtonComponent(0, 176, 0));

        /*
        StyledTextComponent lines = new StyledTextComponent();
        lines.bounds.setBoundPercentages(new Quad2D(0, 0, 1, 1));

        StyledTextString element = new StyledTextString();

        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.alignType = LineStyle.AlignType.CENTER;

        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;

        element.addLineBreak(lineStyle);
        element.addString("Simple test text, for typing on, this is now going to be much longer and more complicated!");

        lineStyle = new LineStyle();
        lineStyle.alignType = LineStyle.AlignType.ALIGN_TEXT_RIGHT;
        element.addLineBreak(lineStyle);
        element.addString("Here we have a seperate text with a different colour");
        element.addElement(new ItemStackElement(new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK), 200));

        lines.pages.text = element;

        addComponent(lines);
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

    public void rebuild(){
        components.forEach(c -> c.build(display.getGSIBounds()));
    }

    public void addComponent(IScaleableComponent component){
        components.add(component);
        queueRebuild();
    }

    public void removeComponent(IScaleableComponent component){
        components.remove(component);
        queueRebuild();
    }

    @Nullable
    public IScaleableComponent getInteractedComponent(DisplayInteractionHandler handler){
        return getInteractedComponent(components, handler);
    }

    @Nullable
    public IScaleableComponent getInteractedComponent(List<IScaleableComponent> components, DisplayInteractionHandler handler){
        for(IScaleableComponent component : components){
            if(component.canRayTrace() && component.getInteraction(handler).isMouseOver(handler)){
                List<IScaleableComponent> subComponents = component.getSubComponents();
                if(subComponents != null){
                    IScaleableComponent result = getInteractedComponent(subComponents, handler);
                    if(result != null){
                        return result;
                    }
                }
                return component;
            }
        }
        return null;
    }

    //// Triggers

    public boolean testTrigger; //TODO REMOVE ME!

    public boolean toggle(IInteractionListener listener, int triggerId){
        if(triggerId == 0){
            return testTrigger = !testTrigger;
        }
        return false; //TODO
    }


    public boolean isActive(IInteractionListener listener, int triggerId){
        if(triggerId == 0){
            return testTrigger;
        }

        return false; //TODO
    }

    //// INestedInteractionListener

    public boolean isDragging;
    public IScaleableComponent focused;

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Nullable
    @Override
    public IScaleableComponent getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable IScaleableComponent component) {
        this.focused = component;
    }

    @Override
    public List<IScaleableComponent> children() {
        return components;
    }

}
