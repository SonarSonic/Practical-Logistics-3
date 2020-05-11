package sonar.logistics.client.gsi;

import net.minecraft.client.Minecraft;
import sonar.logistics.client.design.gui.GSIDesignScreen;
import sonar.logistics.client.gsi.api.IScaleableComponent;
import sonar.logistics.client.gsi.components.text.StyledTextComponent;
import sonar.logistics.client.gsi.components.text.string.GlyphString;
import sonar.logistics.client.gsi.components.text.style.GlyphStyle;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.context.DisplayClickContext;
import sonar.logistics.client.gsi.context.DisplayInteractionContext;
import sonar.logistics.client.gsi.context.ScaleableRenderContext;
import sonar.logistics.client.gsi.properties.ColourProperty;
import sonar.logistics.client.vectors.Quad2D;
import sonar.logistics.multiparts.displays.DisplayVectorHelper;
import sonar.logistics.multiparts.displays.api.IDisplay;

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
            context.offsetComponentHit(component.getAlignment().getRenderBounds());
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
            context.offsetComponentHit(component.getAlignment().getRenderBounds());
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
        lines.alignment.setAlignmentPercentages(new Quad2D(0, 0, 1, 1));

        GlyphString element = new GlyphString();


        LineStyle lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.SCALED_FILL;

        lineStyle.alignType = LineStyle.AlignType.CENTER;


        GlyphStyle style = new GlyphStyle();
        style.textColour = new ColourProperty(255, 255, 255);
        //style.underlined = true;

        element.addLineBreak(lineStyle);
        element.addStyling(style);
        element.addString("Simple Title");


        lineStyle = new LineStyle();
        lineStyle.wrappingType = LineStyle.WrappingType.WRAP_ON;
        lineStyle.alignType = LineStyle.AlignType.JUSTIFY;

        lineStyle.breakPreference = LineStyle.BreakPreference.SPACES;
        element.addLineBreak(lineStyle);

        style = new GlyphStyle();
        style.textColour = new ColourProperty(200, 200, 200);
        style.fontHeight = 0.0625F;
        element.addStyling(style);
        element.addString("Simple text, to allow me to see space breaks. Hopefully it's justifying nicely, let's see");
        lines.glyphStrings.add(element);

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
