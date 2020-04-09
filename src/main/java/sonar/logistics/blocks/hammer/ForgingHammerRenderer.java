package sonar.logistics.blocks.hammer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import sonar.logistics.PL3;
import sonar.logistics.blocks.PL3Blocks;

@OnlyIn(Dist.CLIENT)
public class ForgingHammerRenderer extends TileEntityRenderer<ForgingHammerTile> {

	public ResourceLocation ROPE_TEXTURE = new ResourceLocation(PL3.MODID + ":textures/model/forging_hammer_rope.png");
	public RenderType RENDER_TYPE = RenderType.makeType("rope", DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder().texture(new RenderState.TextureState(ROPE_TEXTURE, false, false)).build(false));

	public ForgingHammerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(ForgingHammerTile hammer, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight) {

		float partialTickSize = 0;//(hammer.coolDown != 0 && hammer.coolDown != ForgingHammerTile.coolDownSpeed) || (hammer.progress != 0 && hammer.progress != ForgingHammerTile.processSpeed) ? partialTicks : 0;
		//FIXME PARTIAL TICKS

		///// RENDER WEIGHT \\\\\\

		matrix.push();
		double percentage = hammer.coolDown != 0 ? ((double)(ForgingHammerTile.coolDownSpeed - hammer.coolDown + 1 - partialTickSize) /  ForgingHammerTile.coolDownSpeed) : (double)(hammer.progress - 1 + (hammer.canProcess ? partialTickSize :  -partialTickSize)) /  ForgingHammerTile.processSpeed;
		matrix.translate(0, 1 - ((27*0.0625) * percentage), 0);

		BlockState state = PL3Blocks.FORGING_HAMMER_BLOCK.getDefaultState();
		BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		dispatcher.getBlockModelRenderer().renderModel(matrix.getLast(), renderer.getBuffer(RenderTypeLookup.getRenderType(state)), state, dispatcher.getModelForState(state), 1, 1, 1, light, overlayLight, EmptyModelData.INSTANCE);
		matrix.pop();



		///// RENDER ROPE \\\\\

		matrix.push();
		state = state.with(ForgingHammerBlock.POS, 2);
		matrix.translate(0.5, 3 - 0.0625*3, 0.5+0.025);
		matrix.rotate(Vector3f.XP.rotationDegrees(180));
		matrix.scale(0.05F, ((float)((27*0.0625) * percentage)), 0.05F);
		dispatcher.getBlockModelRenderer().renderModel(matrix.getLast(), renderer.getBuffer(RenderTypeLookup.getRenderType(state)), state, dispatcher.getModelForState(state), 1, 1, 1, light, overlayLight, EmptyModelData.INSTANCE);
		matrix.pop();



		///// RENDER ITEMS \\\\\\

		matrix.push();
		ItemStack toRender = (hammer.progress == 0 || hammer.coolDown != 0) && !hammer.inventory.getStackInSlot(1).isEmpty() ? hammer.inventory.getStackInSlot(1) : hammer.inventory.getStackInSlot(0);
		if (!toRender.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

			IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(toRender, null, null);
			if(!ibakedmodel.isGui3d()) {
				matrix.rotate(Vector3f.XP.rotationDegrees(90));
				matrix.translate(0.5, 0.5, -0.89);
				matrix.scale(0.5F, 0.5F, 0.5F);
			}else{
				float squashPercentage = 0;
				double squashProcessAt = ((double)ForgingHammerTile.processSpeed / 100)*85;
				double squashCoolDownAt = ((double)ForgingHammerTile.coolDownSpeed / 100)*85;

				if(hammer.progress > squashProcessAt){
					squashPercentage = (float)((hammer.progress) -1  - squashProcessAt) / (float)(ForgingHammerTile.processSpeed - squashProcessAt);
				}
				if((hammer.coolDown != 0 && ForgingHammerTile.coolDownSpeed - hammer.coolDown > squashCoolDownAt)){
					squashPercentage = (float)(ForgingHammerTile.coolDownSpeed - hammer.coolDown -1 - squashCoolDownAt) / (float)(ForgingHammerTile.coolDownSpeed - squashCoolDownAt);
				}

				float yScaleOffset = (float)(0.5 * squashPercentage);
				float yTranslateOffset = ((0.0625F*4) * squashPercentage)/2;
				matrix.translate(0.5, 1 - yTranslateOffset, 0.5);
				matrix.scale(0.5F, 0.5F - yScaleOffset, 0.5F);
			}

			itemRenderer.renderItem(toRender, ItemCameraTransforms.TransformType.FIXED, false, matrix, renderer, light, overlayLight, ibakedmodel);
		}
		matrix.pop();

	}

	@Override
	public boolean isGlobalRenderer(ForgingHammerTile tile) {
		return true;
	}
}