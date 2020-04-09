package sonar.logistics.blocks.hammer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sonar.logistics.PL3;

public class ForgingHammerScreen extends ContainerScreen<ForgingHammerContainer> {

    private ResourceLocation GUI = new ResourceLocation(PL3.MODID, "textures/gui/forging_hammer_screen.png");

    public ForgingHammerTile tileEntity;

    public ForgingHammerScreen(ForgingHammerContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        this.tileEntity = container.tileEntity;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) { }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);

        double l = ((double)tileEntity.progress + partialTicks) * 23 / ForgingHammerTile.processSpeed;
        blit(guiLeft + 76, guiTop + 24, 176, 0, l, 16);
    }



    public void blit(double left, double top, int uv_left, int uvTop, double width, double height) {
        int texX = 256;
        int texY = 256;
        accurateBlit(left, left + width, top, top + height, (uv_left + 0.0F) / (float)texX, (uv_left + (float)width) / (float)texX, (uvTop + 0.0F) / (float)texY, (uvTop + (float)height) / (float)texY);
    }

    public static void accurateBlit(double left, double right, double bottom, double top, float uvLeft, float uvTop, float uvRight, float uvBottom) {

        double z = 0;
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(left, top, z).tex(uvLeft, uvBottom).endVertex();
        builder.pos(right, top, z).tex(uvTop, uvBottom).endVertex();
        builder.pos(right, bottom, z).tex(uvTop, uvRight).endVertex();
        builder.pos(left, bottom, z).tex(uvLeft, uvRight).endVertex();
        builder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(builder);
    }
}
