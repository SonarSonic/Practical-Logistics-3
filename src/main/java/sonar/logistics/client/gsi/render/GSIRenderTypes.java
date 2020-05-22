package sonar.logistics.client.gsi.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class GSIRenderTypes extends RenderType {

    ///diffuse lighting prevents rectangles being their true colour, hence this version with it disabled.
    public static RenderType getEntityTranslucentCullNoDiffuse(ResourceLocation loc) {
        RenderType.State lvt_1_1_ = RenderType.State.getBuilder().texture(new TextureState(loc, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
        return makeType("entity_translucent_cull_no_diffuse", DefaultVertexFormats.ENTITY, 7, 256, true, true, lvt_1_1_);
    }

    public GSIRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }
}
