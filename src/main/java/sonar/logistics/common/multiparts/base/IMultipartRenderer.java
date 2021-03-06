package sonar.logistics.common.multiparts.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public interface IMultipartRenderer<T extends MultipartTile> {

    void render(TileEntityRendererDispatcher renderDispatcher, T tile, MultipartEntry entry, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight, Matrix4f lightingMatrix);

}
