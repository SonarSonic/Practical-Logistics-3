package sonar.logistics.blocks.host;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

public class MultipartHostRenderer extends TileEntityRenderer<MultipartHostTile> {

    public MultipartHostRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MultipartHostTile multipartHostTile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight) {


        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        ///// RENDER BASIC BLOCK STATES \\\\\
        multipartHostTile.MULTIPARTS.forEach((multipart) -> {
            dispatcher.getBlockModelRenderer().renderModel(matrix.getLast(), renderer.getBuffer(RenderTypeLookup.getRenderType(multipart.getBlockState())), multipart.getBlockState(), dispatcher.getModelForState(multipart.getBlockState()), 1, 1, 1, light, overlayLight, EmptyModelData.INSTANCE);
        });


        ///// RENDER SPECIAL RENDERERS \\\\\
        multipartHostTile.MULTIPARTS.stream().filter(E -> E.renderer != null).forEach((multipart) -> {
            //multipart.renderer.render(renderDispatcher, multipart.tile, multipart, partialTicks, matrix, renderer, light, overlayLight);
        });


    }

    @Override
    public boolean isGlobalRenderer(MultipartHostTile tile) {
        return false;
    }
}
