package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import sonar.logistics.client.design.gui.GSIDesignScreen;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.StyledTextString;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.elements.ItemStackElement;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.vectors.DisplayVectorHelper;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.multiparts.displays.api.IDisplay;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GSI {


    public List<IScaleableComponent> components = new ArrayList<>();
    public boolean queuedRebuild = true;
    public IDisplay display;

    public GSI(IDisplay display){
        this.display = display;
    }

    public boolean onHover(DisplayInteractionContext context){
        IScaleableComponent component = getInteractedComponent(context);
        if(component != null){
            context.offsetComponentHit(component.getBounds().renderBounds());
            return component.onHovered(context);
        }
        return false;
    }

    public boolean onClicked(DisplayClickContext context){
        if(context.type.isShifting() && context.type.isRight()){
            Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().displayGuiScreen(new GSIDesignScreen(this, Minecraft.getInstance().player)));
            testStructure();
            queueRebuild(); //TODO REMOVE!!! THIS IS JUST FOR TESTING
            return true;
        }

        IScaleableComponent component = getInteractedComponent(context);
        if(component != null){
            context.offsetComponentHit(component.getBounds().renderBounds());
            return component.onClicked(context);
        }
        return false;
    }

    public void render(ScaleableRenderContext context){
        if(queuedRebuild){
            rebuild();
            queuedRebuild = false;
        }

        DisplayInteractionContext hover = DisplayVectorHelper.createHoverContext(Minecraft.getInstance().player, display);
        if(hover != null){
            //onHover(hover);
        }

        context.preRender();
        components.forEach(c -> c.render(context));
        context.postRender();
    }

    public void queueRebuild(){
        queuedRebuild = true;
    }

    ///TODO REMOVE ME!
    public void testStructure(){
        components.clear();

        StyledTextComponent lines = new StyledTextComponent();
        lines.bounds.setBoundPercentages(new Quad2D(0, 0, 1, 1));

        StyledTextString element = new StyledTextString();

        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.alignType = LineStyle.AlignType.CENTER;

        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;

        GlyphStyle style = new GlyphStyle();
        style.textColour = new ColourProperty(120, 220, 220);
        style.fontHeight = 0.0625F;
        style.underlined = true;
        element.addLineBreak(lineStyle);
        element.addStyling(style);
        element.addString("Simple test text, for typing on, this is now going to be much longer and more complicated!");

        lineStyle = new LineStyle();
        lineStyle.alignType = LineStyle.AlignType.ALIGN_TEXT_RIGHT;
        element.addLineBreak(lineStyle);
        style = new GlyphStyle();
        style.textColour = new ColourProperty(220, 220, 120);
        style.fontHeight = 0.0625F/2;
        style.strikethrough = true;
        element.addStyling(style);
        element.addString("Here we have a seperate text with a different colour");
        element.addElement(new ItemStackElement(new ItemStack(PL3Blocks.FORGING_HAMMER_BLOCK), 200));

        lines.glyphString = element;

        addComponent(lines);

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
    public IScaleableComponent getInteractedComponent(DisplayInteractionContext context){
        return getInteractedComponent(components, context);
    }

    @Nullable
    public IScaleableComponent getInteractedComponent(List<IScaleableComponent> components, DisplayInteractionContext context){
        for(IScaleableComponent component : components){
            if(component.canRayTrace() && component.canInteract(context)){
                List<IScaleableComponent> subComponents = component.getSubComponents();
                if(subComponents != null){
                    IScaleableComponent result = getInteractedComponent(subComponents, context);
                    if(result != null){
                        return result;
                    }
                }
                return component;
            }
        }
        return null;
    }
}
