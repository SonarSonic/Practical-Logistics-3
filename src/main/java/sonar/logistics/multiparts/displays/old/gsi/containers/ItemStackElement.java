package sonar.logistics.multiparts.displays.old.gsi.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import sonar.logistics.blocks.PL3Blocks;

public class ItemStackElement extends ScaleableElement {

    public ItemStackElement(IScaleable host) {
        super(host);
    }

    @Override
    public boolean canRender() {
        return true;
    }

    @Override
    public void render() {
        /*
        RenderSystem.pushMatrix();

        scaled(elementScaling.z, elementScaling.z, elementScaling.z);
        translate(elementAlignment);
        scaled(1, 1 , 0.1); //compresses the items on the z axis
        rotatef(180, 0, 1, 0); // flips the items
        scaled(-1, 1, 1);
        */
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(PL3Blocks.SAPPHIRE_ORE);
        //RenderSystem.translatef(0.0F, 0.0F, 32.0F);
       // this.setBlitOffset(200);
        itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null)
            font = Minecraft.getInstance().fontRenderer;
        itemRenderer.renderItemAndEffectIntoGUI(stack, 0, 0);
        itemRenderer.renderItemOverlayIntoGUI(font, stack, 0, 0, "" + 500);
        //this.setBlitOffset(0);
        itemRenderer.zLevel = 0.0F;


       // RenderSystem.popMatrix();
    }
}
