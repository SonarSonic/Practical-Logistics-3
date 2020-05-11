package sonar.logistics.client.gsi.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

public class DisplayRenderTypes extends RenderType {

    public static RenderType COLOURED_RECT = makeType(PL3.MODID + ":" + "rect", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, 7, 256, RenderType.State.getBuilder().texture(NO_TEXTURE).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).build(false));

    public static RenderType getScaledTextType(ResourceLocation p_228658_0_) {
        return makeType(PL3.MODID + ":" + "text", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, true, true, RenderType.State.getBuilder().texture(new TextureState(p_228658_0_, false, true)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(HALF_ALPHA).lightmap(LIGHTMAP_ENABLED).build(false));
    }


    ///NOT USED
    public DisplayRenderTypes(String nameIn, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTask, Runnable clearTask) {
        super(nameIn, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTask, clearTask);
    }
}
