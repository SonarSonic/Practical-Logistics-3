package sonar.logistics.client.imgui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.FramebufferConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL40;
import sonar.logistics.PL3;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.client.gui.SimpleWidgetScreen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * SOURCES
 * https://github.com/kotlin-graphics/imgui/wiki/Usage-in-Minecraft-Mods
 * https://github.com/breadbyte/fabric-example-imgui/blob/master/src/main/java/net/fabricmc/example/screen/ImguiScreen.java
 */
public class ImGuiScreen extends SimpleWidgetScreen {

    protected static ImGuiImplGl3 implGl3;
    protected static ImGuiImplGlfw implGlfw;
    protected static HashSet<Integer> keyBuffer = new HashSet<Integer>();
    protected static NodeEditorContext nodeEditorContext;

    static {
        ImGui.createContext();
        ImNodes.createContext();
        nodeEditorContext = NodeEditor.createEditor();

        /*
        //// fancy font loader
        byte[] fontData = null;
        try {
            InputStream stream = ImGuiScreen.class.getResourceAsStream("/assets/practicallogistics3/font/Cousine-Regular.ttf");
            if(stream != null){
                fontData = new byte[stream.available()];
                stream.read(fontData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fontData = null;
        }
        if(fontData != null){
            ImGui.getIO().getFonts().addFontFromMemoryTTF(fontData, 16);
        }

        //// fancy font loader
        fontData = null;
        try {
            InputStream stream = ImGuiScreen.class.getResourceAsStream("/assets/practicallogistics3/font/Karla-Regular.ttf");
            if(stream != null){
                fontData = new byte[stream.available()];
                stream.read(fontData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fontData = null;
        }
        if(fontData != null){
            ImGui.getIO().getFonts().addFontFromMemoryTTF(fontData, 16);
        }
        */

        //// load icon font

        byte[] fontData = null;
        try {
            InputStream stream = ImGuiScreen.class.getResourceAsStream("/assets/practicallogistics3/font/fontawesome-webfont.ttf");
            if(stream != null){
                fontData = new byte[stream.available()];
                stream.read(fontData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fontData = null;
        }

        if(fontData != null){
            // Merge icons into default tool font
            ImGui.getIO().getFonts().addFontDefault();
            ImFontConfig config = new ImFontConfig();
            config.setMergeMode(true);
            config.setGlyphMinAdvanceX(13.0F); // Use if you want to make the icon monospaced
            short[] glyphRanges = {(short) FontAwesome.IconMin, (short) FontAwesome.IconMax, 0 };
            ImGui.getIO().getFonts().addFontFromMemoryTTF(fontData, 13, config, glyphRanges);
        }


        implGlfw = new ImGuiImplGlfw();
        implGlfw.init(Minecraft.getInstance().getMainWindow().getHandle(), false);
        implGl3 = new ImGuiImplGl3();
        implGl3.init();
    }

    public ImGuiScreen() {
        super();
    }

    public void renderImGui(int x, int y, float partialTicks) {
        ImGui.showDemoWindow();
    }

    @Override
    public final void render(int x, int y, float partialTicks) {

        this.renderBackground();

        implGlfw.newFrame();
        ImGui.newFrame();
        startItemStackRenderer();

        renderImGui(x, y, partialTicks);

        finishItemStackRenderer();
        ImGui.render();
        implGl3.renderDrawData(ImGui.getDrawData());

    }

    // Tells imgui to enter a character, when typing on a textbox or similar.
    @Override
    public boolean charTyped(char chr, int keyCode) {
        if (ImGui.getIO().getWantTextInput()) {
            ImGui.getIO().addInputCharacter(chr);
        }

        super.charTyped(chr, keyCode);
        return true;
    }

    // Passes mouse scrolling to imgui.
    @Override
    public boolean mouseScrolled(double d, double e, double amount) {
        if (ImGui.getIO().getWantCaptureMouse()) {
            ImGui.getIO().setMouseWheel((float) amount);
        }

        super.mouseScrolled(d, e, amount);
        return true;
    }

    // Passes keypresses for imgui to handle.
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ImGui.getIO().getWantCaptureKeyboard()) {
            ImGui.getIO().setKeysDown(keyCode, true);
            keyBuffer.add(keyCode);
        }

        // Skip handling of the ESC key, because Minecraft uses it to close the screen.
        if (keyCode == 256) {
            ImGui.getIO().setKeysDown(256, false);
        }

        super.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    // Tells imgui the keys pressed have been released.
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        ImGui.getIO().setKeysDown(keyCode, false);
        keyBuffer.remove(keyCode);

        super.keyReleased(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public void onClose() {
        // When Minecraft closes the screen, clear the key buffer.
        for (int keyCode: keyBuffer) {
            ImGui.getIO().setKeysDown(keyCode, false);
        }
        keyBuffer.clear();

        super.onClose();

        ClientDataCache.INSTANCE.clearUICaches();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    ///// ItemStack Helper Methods \\\\\

    public void itemStackImage(ItemStack stack){
        loadItemStack(stack);
        ImGui.image(itemStackframeBuffer.textureID, getGridRenderSize(), getGridRenderSize(), getItemStackMinX(), getItemStackMinY(), getItemStackMaxX(), getItemStackMaxY());
    }

    public boolean itemStackButton(ItemStack stack){
        loadItemStack(stack);
        return ImGui.imageButton(itemStackframeBuffer.textureID, getGridRenderSize(), getGridRenderSize(), getItemStackMinX(), getItemStackMinY(), getItemStackMaxX(), getItemStackMaxY());
    }

    ///// ItemStack Renderer \\\\\

    public static OffscreenFrameBuffer itemStackframeBuffer = new OffscreenFrameBuffer(getItemStackFrameBufferSize(), getItemStackFrameBufferSize());

    public static List<ItemStack> prevItemStackRenderList = new ArrayList<>();
    public static List<ItemStack> itemStackRenderList = new ArrayList<>();
    public static int renderCount = 0;
    public static float lastMinX = 0;
    public static float lastMaxX = 0;
    public static float lastMinY = 0;
    public static float lastMaxY = 0;
    public static boolean markDirty = true;

    public static void startItemStackRenderer(){
        prevItemStackRenderList.clear();
        prevItemStackRenderList = itemStackRenderList;
        itemStackRenderList = new ArrayList<>();
        renderCount = 0;
        markDirty = false;
    }

    public static void finishItemStackRenderer(){
        if(!markDirty){
            return;
        }
        int prevFrameBuffer = GL40.glGetInteger(GL40.GL_FRAMEBUFFER_BINDING);
        GlStateManager.bindFramebuffer(GL40.GL_FRAMEBUFFER, itemStackframeBuffer.frameBufferID);
        GlStateManager.viewport(0, 0, itemStackframeBuffer.width, itemStackframeBuffer.height);
        GL40.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set background color to black and opaque

        RenderSystem.clear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT | GL40.GL_ACCUM_BUFFER_BIT | GL40.GL_STENCIL_BUFFER_BIT, Minecraft.IS_RUNNING_ON_MAC);
        RenderSystem.matrixMode(GL40.GL_PROJECTION);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, itemStackframeBuffer.width, itemStackframeBuffer.height, 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode( GL40.GL_MODELVIEW);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
        RenderHelper.setupGui3DDiffuseLighting();
        RenderHelper.enableStandardItemLighting();
        int length = (getItemStackFrameBufferSize() / getGridRenderSize());
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        stacks: for(int y = 0; y < length; y++){
            for(int x = 0; x < length; x++){
                int count = x + y*length;
                if(count < renderCount){
                    ItemStack stack = itemStackRenderList.get(count);
                    float scale = getItemStackRenderSize() / 16F;
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef(x*getGridRenderSize() + getItemStackPadding(), y*getGridRenderSize() + getItemStackPadding(), 0);
                    RenderSystem.scalef(scale, scale, scale);
                    itemRenderer.renderItemIntoGUI(stack, 0, 0);
                    itemRenderer.renderItemOverlays(fontRenderer, stack, 0, 0);
                    RenderSystem.popMatrix();
                }else{
                    break stacks;
                }
            }
        }
        GlStateManager.bindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, prevFrameBuffer);
    }

    public static void loadItemStack(ItemStack stack){
        if(renderCount >= getItemStackRenderLimit()){
            lastMinX = 0;
            lastMaxX = (float)getGridRenderSize() / getItemStackFrameBufferSize();
            lastMinY = 0;
            lastMaxY = (float)getGridRenderSize() / getItemStackFrameBufferSize();
            return;
        }

        itemStackRenderList.add(stack);

        int length = (getItemStackFrameBufferSize() / getGridRenderSize());
        int yLevel = renderCount / length;
        int xLevel = renderCount - yLevel*length;

        lastMinX = ((float)xLevel*getGridRenderSize())/getItemStackFrameBufferSize();
        lastMaxX = ((float)(xLevel*getGridRenderSize()) + getGridRenderSize())/getItemStackFrameBufferSize();

        lastMinY = ((float)yLevel*getGridRenderSize())/getItemStackFrameBufferSize();
        lastMaxY = ((float)(yLevel*getGridRenderSize()) + getGridRenderSize())/getItemStackFrameBufferSize();

        ///frame buffers are always upside down
        lastMinY = 1 - lastMinY;
        lastMaxY = 1 - lastMaxY;

        if(!markDirty){
            if(prevItemStackRenderList.size() <= renderCount){
                markDirty = true;
            }else{
                ItemStack prevStack = prevItemStackRenderList.get(renderCount);
                if(!ItemStack.areItemStacksEqual(prevStack, stack) || !ItemStack.areItemStackTagsEqual(prevStack, stack)){
                    markDirty = true;
                }
            }
        }
        renderCount++;
    }

    public static float getItemStackMinX(){
        return lastMinX;
    }

    public static float getItemStackMaxX(){
        return lastMaxX;
    }

    public static float getItemStackMinY(){
        return lastMinY;
    }

    public static float getItemStackMaxY(){
        return lastMaxY;
    }

    public static int getGridRenderSize(){
        return 36;
    }

    public static int getItemStackRenderSize(){
        return 32;
    }

    public static int getItemStackPadding(){
        return 2;
    }

    public static int getItemStackFrameBufferSize(){
        return 512;
    }

    public static int getItemStackRenderLimit(){

        int length = (getItemStackFrameBufferSize() / getGridRenderSize());
        return length* length;
    }
}