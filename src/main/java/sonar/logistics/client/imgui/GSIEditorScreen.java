package sonar.logistics.client.imgui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.shader.FramebufferConstants;
import org.lwjgl.opengl.GL40;
import sonar.logistics.client.ClientDataCache;
import sonar.logistics.client.gsi.GSI;
import sonar.logistics.client.gsi.components.Component;
import sonar.logistics.client.gsi.components.buttons.EnumButtonIcons;
import sonar.logistics.client.gsi.components.text.style.GlyphStyleAttributes;
import sonar.logistics.client.gsi.components.text.style.LineStyle;
import sonar.logistics.client.gsi.interactions.AbstractComponentInteraction;
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.interactions.api.IInteractionHandler;
import sonar.logistics.client.gsi.interactions.resize.ResizingInteraction;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.gui.DesignInterfaces;
import sonar.logistics.client.gui.EnumLineBreakGlyphTypes;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.server.data.methods.categories.DataCategories;
import sonar.logistics.server.data.methods.categories.DataCategory;
import sonar.logistics.server.data.types.sources.SourceData;

import java.util.List;

public class GSIEditorScreen extends ImGuiScreen{

    private static Texture buttonTexture;
    private static OffscreenFrameBuffer viewportFrameBuffer = new OffscreenFrameBuffer(1024, 768);
    public GSIViewportWidget widget = null;

    public GSI displayGSI;

    public GSIEditorScreen(GSI displayGSI) {
        super();
        GSIDesignSettings.setDesignScreen(this);
        this.displayGSI = displayGSI;
        this.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_RESIZING);
    }


    @Override
    protected void init() {
        super.init();
        addWidget(widget = new GSIViewportWidget(displayGSI, 0, 0, viewportFrameBuffer.width, viewportFrameBuffer.height));

        //load textures
        if(buttonTexture == null){
            Minecraft.getInstance().getTextureManager().bindTexture(ScreenUtils.BUTTONS_ALPHA);
            buttonTexture = Minecraft.getInstance().getTextureManager().getTexture(ScreenUtils.BUTTONS_ALPHA);
        }
    }

    @Override
    public void renderImGui(int x, int y, float partialTicks) {
        super.renderImGui(x, y, partialTicks);


        ImGui.begin("Display Editor", ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize);

        ImGui.beginChild("Data", 500, 30);
        if(ImGui.button("Add Data")){
            ImGui.openPopup("Create Data");
        }

        if(ImGui.beginPopup("Create Data")){
            for(DataCategory category : DataCategories.categories){

                Texture texture = Minecraft.getInstance().getTextureManager().getTexture(category.getIconLocation());
                if(texture == null){
                    Minecraft.getInstance().getTextureManager().bindTexture(category.getIconLocation());
                    texture = Minecraft.getInstance().getTextureManager().getTexture(category.getIconLocation());
                }
                if(texture != null){
                    ImGui.image(texture.getGlTextureId(), 32, 32, 0, 0, 1, 1);
                }

                ImGui.sameLine();
                ImGui.selectable(category.getID());
            }


            if(ImGui.beginListBox("##sources", 1000, 200)){

                List<SourceData> sources = ClientDataCache.INSTANCE.getOrRequestNetworkSources(displayGSI.display.getNetworkID(), false);
                for(SourceData source : sources){
                    itemStackImage(source.stack);
                    ImGui.sameLine();
                    ImGui.selectable(source.address.toString());
                }

                ImGui.endListBox();
            }


            ImGui.endPopup();
        }

        ImGui.endChild();

        float[] inputArray;
        if(false) {

            if (createSelectableButton(EnumButtonIcons.STYLE_BOLD, 32, 32, GSIDesignSettings.glyphStyle.bold)) {
                GSIDesignSettings.toggleBoldStyling();
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_ITALIC, 32, 32, GSIDesignSettings.glyphStyle.italic)) {
                GSIDesignSettings.toggleItalicStyling();
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_UNDERLINE, 32, 32, GSIDesignSettings.glyphStyle.underlined)) {
                GSIDesignSettings.toggleUnderlineStyling();
            }

            ImGui.sameLine();
            ImGui.spacing();

            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_STRIKETHROUGH, 32, 32, GSIDesignSettings.glyphStyle.strikethrough)) {
                GSIDesignSettings.toggleStrikethroughStyling();
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_OBFUSCATE, 32, 32, GSIDesignSettings.glyphStyle.obfuscated)) {
                GSIDesignSettings.toggleObfuscatedStyling();
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_SHADOW, 32, 32, GSIDesignSettings.glyphStyle.shadow)) {
                GSIDesignSettings.toggleShadowStyling();
            }

            ImGui.sameLine();
            ImGui.spacing();

            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.JUSTIFY_LEFT, 32, 32, GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_LEFT)) {
                GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_LEFT);
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.JUSTIFY_CENTRE, 32, 32, GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_CENTRE)) {
                GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_CENTRE);
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.JUSTIFY_RIGHT, 32, 32, GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY_RIGHT)) {
                GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY_RIGHT);
            }
            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.JUSTIFY, 32, 32, GSIDesignSettings.lineStyle.justifyType == LineStyle.JustifyType.JUSTIFY)) {
                GSIDesignSettings.setJustifyType(LineStyle.JustifyType.JUSTIFY);
            }

            ImGui.sameLine();
            ImGui.spacing();

            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.BULLET_POINT_TOGGLE, 32, 32, GSIDesignSettings.currentLineBreakStyle != EnumLineBreakGlyphTypes.DEFAULT_BREAK)) {
                GSIDesignSettings.toggleLineBreakStyle();
            }

            ImGui.sameLine();
            if (createSelectableButton(EnumButtonIcons.STYLE_TEXT_COLOUR, 32, 32, false)) {
                GSIDesignSettings.setTextColour(GSIDesignSettings.selectedColour.copy());
            }


            ImGui.sameLine();
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 12);
            inputArray = GSIDesignSettings.selectedColour.asFloatArray();
            if (ImGui.colorEdit4("text_colour", inputArray, ImGuiColorEditFlags.NoInputs | ImGuiColorEditFlags.NoLabel)) {
                GSIDesignSettings.selectedColour = new ColourProperty(inputArray);
            }
            ImGui.popStyleVar();


            ImGui.sameLine();
            ImGui.spacing();

            ImGui.sameLine();
            ImGui.pushID("fontsize");
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 12);
            ImGui.setNextItemWidth(180);
            ImInt fontSize = new ImInt(GSIDesignSettings.getPixelFontHeight());
            if (ImGui.inputInt("##fontsize", fontSize)) {
                GSIDesignSettings.setFontHeight(fontSize.get());
            }
            ImGui.popStyleVar();
            ImGui.popID();
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip("Font Height");
            }
        }

        //// Component Settings \\\\

        ImGui.beginChild("Components", 350, 768, true, ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text("Components");

        Component component = null;

        if(displayGSI.getFocusedListener().isPresent()){
            IInteractionHandler handler = displayGSI.getFocusedListener().get();
            if(handler instanceof AbstractComponentInteraction) {
                AbstractComponentInteraction<Component> componentInteraction = (AbstractComponentInteraction<Component>) handler;
                component = componentInteraction.component;
            }
        }

        if(!displayGSI.components.isEmpty()){
            if(ImGui.beginListBox("##Components", 500, 100)){
                Component delete = null;
                for(Component c : displayGSI.components){
                    boolean isSelected = c == component;
                    String displayName = c.getClass().getSimpleName();
                    if(!c.isVisible){
                        displayName += " (Invisible)";
                    }
                    if(ImGui.selectable(displayName, isSelected)){
                        displayGSI.setFocused(new ResizingInteraction(c));
                    }
                    if(isSelected){
                        ImGui.setItemDefaultFocus();
                    }
                    if(ImGui.beginPopupContextItem()){
                        if(ImGui.selectable("Select")){
                            ///TODO - MAKE SELECTION SIMPLER
                        }
                        if(ImGui.selectable("Duplicate")){
                            //TODO DUPLICATE COMPONENTS
                        }
                        if(ImGui.selectable("Delete")){
                            delete = c;
                        }
                        ImGui.endPopup();
                    }
                }
                if(delete != null){
                    displayGSI.components.remove(delete);
                }
                ImGui.endListBox();
            }
        }

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.text("Component Editor");
        if(component == null){
            ImGui.textColored(180, 0 ,0, 255,"No component selected");
        }else{
            if(ImGui.treeNodeEx("Info")){
                ImGui.text("Type: " + component.getClass().getSimpleName());
                ImGui.treePop();
            }

            if(ImGui.treeNodeEx("General")){


                if(ImGui.checkbox("Visible", component.isVisible)){
                    component.isVisible = !component.isVisible;
                }


                inputArray = new float[]{(float)component.getStyling().getXPos().value};
                if(ImGui.sliderFloat("X Position", inputArray, 0, 1, "%.2f / 1.00")){
                    component.getStyling().setXPos(new LengthProperty(Unit.PERCENT, inputArray[0]));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getYPos().value};
                if(ImGui.sliderFloat("Y Position", inputArray, 0, 1, "%.2f / 1.00")){
                    component.getStyling().setYPos(new LengthProperty(Unit.PERCENT, inputArray[0]));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getWidth().value};
                if(ImGui.sliderFloat("Width", inputArray, 0, 1, "%.2f / 1.00")){
                    component.getStyling().setWidth(new LengthProperty(Unit.PERCENT, inputArray[0]));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getHeight().value};
                if(ImGui.sliderFloat("Height", inputArray, 0, 1, "%.2f / 1.00")){
                    component.getStyling().setHeight(new LengthProperty(Unit.PERCENT, inputArray[0]));
                    component.rebuild();
                }
                ImGui.treePop();
            }

            if(ImGui.treeNode("Component Offsets")){

                inputArray = new float[]{(float)component.getStyling().getMarginWidth().value*16};
                if(ImGui.sliderFloat("Margin Width", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setMarginWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getMarginHeight().value*16};
                if(ImGui.sliderFloat("Margin Height", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setMarginHeight(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getPaddingWidth().value*16};
                if(ImGui.sliderFloat("Padding Width", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setPaddingWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getPaddingHeight().value*16};
                if(ImGui.sliderFloat("Padding Height", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setPaddingHeight(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getBorderWidth().value*16};
                if(ImGui.sliderFloat("Border Width", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setBorderWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                inputArray = new float[]{(float)component.getStyling().getBorderHeight().value*16};
                if(ImGui.sliderFloat("Border Height", inputArray, 0, 1, "%.2f px")){
                    component.getStyling().setBorderHeight(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                    component.rebuild();
                }

                ImGui.treePop();
            }

            if(ImGui.treeNode("Component Colours")){

                inputArray = component.getStyling().getInnerBackgroundColour().asFloatArray();
                if(ImGui.colorEdit4("Inner Background Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setInnerBackgroundColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getOuterBackgroundColour().asFloatArray();
                if(ImGui.colorEdit4("Outer Background Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setOuterBackgroundColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getBorderColour().asFloatArray();
                if(ImGui.colorEdit4("Border Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setBorderColour(new ColourProperty(inputArray));
                }


                inputArray = component.getStyling().getEnabledTextColour().asFloatArray();
                if(ImGui.colorEdit4("Enabled Text Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setEnabledTextColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getHoveredTextColour().asFloatArray();
                if(ImGui.colorEdit4("Hovered Text Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setHoveredTextColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getDisabledTextColour().asFloatArray();
                if(ImGui.colorEdit4("Disabled Text Colour", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setDisabledTextColour(new ColourProperty(inputArray));
                }

                ImGui.treePop();
            }
        }

        ImGui.endChild();

        //// Viewport \\\\
        ImGui.sameLine();
        ImGui.beginChild("Viewport", viewportFrameBuffer.width + 12, viewportFrameBuffer.height + 12);
        if(!viewportFrameBuffer.error){
            int prevFrameBuffer = GL40.glGetInteger(GL40.GL_FRAMEBUFFER_BINDING);
            GlStateManager.bindFramebuffer(GL40.GL_FRAMEBUFFER, viewportFrameBuffer.frameBufferID);
            GlStateManager.viewport(0, 0, viewportFrameBuffer.width, viewportFrameBuffer.height);
            GL40.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set background color to black and opaque]

            RenderSystem.clear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT | GL40.GL_ACCUM_BUFFER_BIT | GL40.GL_STENCIL_BUFFER_BIT, Minecraft.IS_RUNNING_ON_MAC);
            RenderSystem.matrixMode(GL40.GL_PROJECTION);
            RenderSystem.loadIdentity();
            RenderSystem.ortho(0.0D, viewportFrameBuffer.width, viewportFrameBuffer.height, 0.0D, 1000.0D, 3000.0D);
            RenderSystem.matrixMode( GL40.GL_MODELVIEW);
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
            RenderHelper.setupGui3DDiffuseLighting();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.defaultBlendFunc();
            GL40.glEnable(GL40.GL_BLEND);
            GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);


            widget.render(x, y, partialTicks);
            GlStateManager.bindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, prevFrameBuffer);
        }


        ImGui.imageButton(viewportFrameBuffer.textureID, viewportFrameBuffer.width, viewportFrameBuffer.height, 0 ,1, 1, 0, 1 , 0,0,0, 1F);
        widget.isViewportActive = ImGui.isItemActive();
        widget.isViewportHovered = ImGui.isItemHovered();
        widget.bounds.x = ImGui.getItemRectMinX();
        widget.bounds.y = ImGui.getItemRectMinY();
        ImGui.endChild();

        ImGui.sameLine();

        ImGui.end();
    }

    public boolean createSelectableButton(EnumButtonIcons icon, int width, int height, boolean selected){
        ImGui.pushID(icon.name());
        ImGui.pushStyleColor(ImGuiCol.Button, selected ? ScreenUtils.transparent_green_button.rgba : ScreenUtils.transparent_disabled_button.rgba);
        boolean active = ImGui.imageButton(buttonTexture.getGlTextureId(), width, height, icon.getUVLeft()/256F, icon.getUVTop()/256F, (icon.getUVLeft() + icon.getWidth())/256F, (icon.getUVTop() + icon.getHeight())/256F);
        if(ImGui.isItemHovered()){
            ImGui.setTooltip(icon.name());
        }
        ImGui.popStyleColor();
        ImGui.popID();
        return active;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(widget.isViewportHovered){
            return widget.mouseScrolled(mouseX, mouseY, amount);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }


    public void onSettingChanged(Object setting, Object settingObj){
        displayGSI.onSettingChanged(setting, settingObj);
    }

    public void onCursorStyleChanged() {
        ///updates the font height - this should really be automatic, but this works for now.
        displayGSI.onSettingChanged(GlyphStyleAttributes.FONT_HEIGHT, GSIDesignSettings.glyphStyle.fontHeight);
    }

    @Override
    public void tick() {
        super.tick();
        GSIDesignSettings.tickCursorCounter();
        DesignInterfaces.tick();
    }
}
