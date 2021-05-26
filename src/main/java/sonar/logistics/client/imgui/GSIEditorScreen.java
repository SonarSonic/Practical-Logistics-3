package sonar.logistics.client.imgui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import imgui.flag.*;
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
import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gsi.style.properties.LengthProperty;
import sonar.logistics.client.gsi.style.properties.Unit;
import sonar.logistics.client.gui.DesignInterfaces;
import sonar.logistics.client.gui.EnumLineBreakGlyphTypes;
import sonar.logistics.client.gui.GSIDesignSettings;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.nodes.NodeGraphUtils;
import sonar.logistics.networking.PL3PacketHandler;
import sonar.logistics.networking.packets.GeneralPacket;
import sonar.logistics.networking.packets.GeneralPackets;
import sonar.logistics.server.address.Address;
import sonar.logistics.server.address.BlockAddress;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.server.data.methods.Method;
import sonar.logistics.server.data.methods.MethodCategory;
import sonar.logistics.server.data.types.sources.SourceData;
import sonar.logistics.util.MathUtils;

import java.util.*;
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
        this.displayGSI.interactionHandler.setInteractionType(GSIInteractionHandler.InteractionType.GUI_EDITING);
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
        /* TODO REMOVE ME
        nodeBuilder = new NodeGraphUtils();
        DefaultNode getInventory = new DefaultNode(nodeBuilder.nextNodeID(), "getInventory", new ArrayList<>(), new ArrayList<>(), Graph.NodeType.Method, null, null);
        getInventory.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), getInventory, "Inventory", Graph.PinType.Inventory, Graph.PinKind.Output));
        nodeBuilder.nodes.add(getInventory);

        DefaultNode getItem = new DefaultNode(nodeBuilder.nextNodeID(), "getItem", new ArrayList<>(), new ArrayList<>(), Graph.NodeType.Method, null, null);
        getItem.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), getItem, "Inventory", Graph.PinType.Inventory, Graph.PinKind.Input));
        getItem.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), getItem, "Slot", Graph.PinType.Int, Graph.PinKind.Input));
        getItem.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), getItem, "ItemStack", Graph.PinType.ItemStack, Graph.PinKind.Output));
        nodeBuilder.nodes.add(getItem);

        DefaultNode readItemStack = new DefaultNode(nodeBuilder.nextNodeID(), "readItemStack", new ArrayList<>(), new ArrayList<>(), Graph.NodeType.Method, null, null);
        readItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), readItemStack, "ItemStack", Graph.PinType.ItemStack, Graph.PinKind.Input));
        readItemStack.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), readItemStack, "Item", Graph.PinType.Item, Graph.PinKind.Output));
        readItemStack.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), readItemStack, "Count", Graph.PinType.Int, Graph.PinKind.Output));
        readItemStack.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), readItemStack, "NBT", Graph.PinType.NBT, Graph.PinKind.Output));
        nodeBuilder.nodes.add(readItemStack);


        DefaultNode createItemStack = new DefaultNode(nodeBuilder.nextNodeID(), "createItemStack", new ArrayList<>(), new ArrayList<>(), Graph.NodeType.Method, null, null);
        createItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), createItemStack, "Item", Graph.PinType.Item, Graph.PinKind.Input));
        createItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), createItemStack, "Count", Graph.PinType.Int, Graph.PinKind.Input));
        createItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), createItemStack, "NBT", Graph.PinType.NBT, Graph.PinKind.Input));
        createItemStack.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), createItemStack, "ItemStack", Graph.PinType.ItemStack, Graph.PinKind.Output));
        nodeBuilder.nodes.add(createItemStack);


        DefaultNode addItemStack = new DefaultNode(nodeBuilder.nextNodeID(), "addItemStack", new ArrayList<>(), new ArrayList<>(), Graph.NodeType.Method, null, null);
        addItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), addItemStack, "Inventory", Graph.PinType.Item, Graph.PinKind.Input));
        addItemStack.inputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), addItemStack, "ItemStack", Graph.PinType.Int, Graph.PinKind.Input));
        addItemStack.outputs.add(new Graph.DataPin(nodeBuilder.nextNodeID(), addItemStack, "Boolean", Graph.PinType.Bool, Graph.PinKind.Output));
        nodeBuilder.nodes.add(addItemStack);

         */
    }


    @Override
    public void renderImGui(int x, int y, float partialTicks) {
        super.renderImGui(x, y, partialTicks);

        ImGui.begin("Display Editor", ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize);
        float[] inputArray;

        //// Component Settings \\\\

        ImGui.beginMenuBar();
        //ImGui.menuItem("File");
        //ImGui.menuItem("Reset");
        ImGui.endMenuBar();

        ImGui.beginChild("Components", 350, 768 + 50, true, ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.pushFont(OPEN_SANS_24PX);
        ImGui.text("Components");
        ImGui.popFont();


        ImGui.sameLine(0, 20);
        if(ImGui.button(FontAwesome.Plus + " New")){
            ImGui.openPopup("Types");
        }


        if(ImGui.beginPopup("Types")){

            ImGui.button(FontAwesome.ExternalLink + "  Data", 140, 30);
            ImGui.button(FontAwesome.FileWordO + "  Title", 140, 30);
            ImGui.button(FontAwesome.FileText + "  Text", 140, 30);
            ImGui.button(FontAwesome.FileImageO + "  Image", 140, 30);
            if(ImGui.button(FontAwesome.MousePointer + " Button", 140, 30)){

            }

            ImGui.button(FontAwesome.Sliders + " Slider", 140, 30);
            ImGui.endPopup();
        }
        /*
        ImGui.button(FontAwesome.ExternalLink, 30, 30);
        ImGui.sameLine();
        ImGui.button(FontAwesome.FileWordO, 30, 30);
        ImGui.sameLine();
        ImGui.button(FontAwesome.FileText);
        ImGui.sameLine();
        ImGui.button(FontAwesome.FileImageO);
        ImGui.sameLine();
        ImGui.button(FontAwesome.MousePointer);
        ImGui.sameLine();
        ImGui.button(FontAwesome.Sliders);

        if(ImGui.beginPopup("##componenttypes")){

            ImGui.button(FontAwesome.ExternalLink + "  Data", 140, 30);
            ImGui.button(FontAwesome.FileWordO + "  Title", 140, 30);
            ImGui.button(FontAwesome.FileText + "  Text", 140, 30);
            ImGui.button(FontAwesome.FileImageO + "  Image", 140, 30);
            if(ImGui.button(FontAwesome.MousePointer + " Button", 140, 30)){

            }

            ImGui.button(FontAwesome.Sliders + " Slider", 140, 30);
            ImGui.endPopup();
        }

        ImGui.sameLine();
        ImGui.spacing();
        ImGui.spacing();

         */

        Component component = displayGSI.getSelectedComponent();
        if(!displayGSI.components.isEmpty()){

            if(ImGui.beginListBox("##Components", 334, 100)){
                Component delete = null;
                for(Component c : displayGSI.components){
                    boolean isSelected = c == component;
                    String displayName = c.getClass().getSimpleName();
                    if(!c.isVisible){
                        displayName += " (Invisible)";
                    }
                    if(ImGui.selectable(displayName, isSelected)){
                        displayGSI.setFocused(c);
                    }
                    if(isSelected){
                        ImGui.setItemDefaultFocus();
                    }
                    if(ImGui.beginPopupContextItem()){
                        if(ImGui.selectable("Select")){
                            displayGSI.setFocused(c);
                        }
                        if(ImGui.selectable("Duplicate (TODO)")){
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

        ImGui.pushFont(OPEN_SANS_24PX);
        ImGui.text("Component Settings");
        ImGui.popFont();

        ImGui.beginChild("componentsettings", 334, 350);

        if(component == null){
            ImGui.text("No component selected");
        }else{
            ImGui.text(component.getClass().getSimpleName());
            ImGui.spacing();
            if(ImGui.checkbox("Visible", component.isVisible)){
                component.isVisible = !component.isVisible;
            }
            ImGui.spacing();
            inputArray = new float[]{(float)component.getStyling().getXPos().value, (float)component.getStyling().getYPos().value};
            ImGui.pushItemWidth(200);
            if(ImGui.dragFloat2("Position", inputArray, 0.005F, 0, 1, "%.2f")){
                float maxX = 1F - component.getStyling().getWidth().getValue(1.0F);
                float maxY = 1F - component.getStyling().getHeight().getValue(1.0F);
                component.getStyling().setXPos(new LengthProperty(Unit.PERCENT, Math.min(maxX, inputArray[0])));
                component.getStyling().setYPos(new LengthProperty(Unit.PERCENT, Math.min(maxY, inputArray[1])));
                component.rebuild();
            }

            inputArray = new float[]{(float)component.getStyling().getWidth().value, (float)component.getStyling().getHeight().value};
            if(ImGui.dragFloat2("Size", inputArray, 0.005F, 0, 1, "%.2f")){
                float maxWidth = 1F - component.getStyling().getXPos().getValue(1.0F);
                float maxHeight = 1F - component.getStyling().getYPos().getValue(1.0F);
                component.getStyling().setWidth(new LengthProperty(Unit.PERCENT, Math.min(maxWidth, inputArray[0])));
                component.getStyling().setHeight(new LengthProperty(Unit.PERCENT, Math.min(maxHeight, inputArray[1])));
                component.rebuild();
            }
            ImGui.spacing();
            inputArray = new float[]{(float)component.getStyling().getMarginWidth().value*16, (float)component.getStyling().getMarginHeight().value*16};
            if(ImGui.dragFloat2("Margin", inputArray, 0.005F, 0, 1, "%.2f")){
                component.getStyling().setMarginWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                component.getStyling().setMarginHeight(new LengthProperty(Unit.PIXEL, inputArray[1]/16F));
                component.rebuild();
            }

            inputArray = new float[]{(float)component.getStyling().getPaddingWidth().value*16, (float)component.getStyling().getPaddingHeight().value*16};
            if(ImGui.dragFloat2("Padding", inputArray, 0.005F, 0, 1, "%.2f")){
                component.getStyling().setPaddingWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                component.getStyling().setPaddingHeight(new LengthProperty(Unit.PIXEL, inputArray[1]/16F));
                component.rebuild();
            }

            inputArray = new float[]{(float)component.getStyling().getBorderWidth().value*16, (float)component.getStyling().getBorderHeight().value*16};
            if(ImGui.dragFloat2("Border", inputArray, 0.005F, 0, 1, "%.2f")){
                component.getStyling().setBorderWidth(new LengthProperty(Unit.PIXEL, inputArray[0]/16F));
                component.getStyling().setBorderHeight(new LengthProperty(Unit.PIXEL, inputArray[1]/16F));
                component.rebuild();
            }
            ImGui.popItemWidth();

            ImGui.spacing();
            if(ImGui.treeNode("Colours")){

                inputArray = component.getStyling().getInnerBackgroundColour().asFloatArray();
                if(ImGui.colorEdit4("Inner Background", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setInnerBackgroundColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getOuterBackgroundColour().asFloatArray();
                if(ImGui.colorEdit4("Outer Background", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setOuterBackgroundColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getBorderColour().asFloatArray();
                if(ImGui.colorEdit4("Border", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setBorderColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getEnabledTextColour().asFloatArray();
                if(ImGui.colorEdit4("Enabled Text", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setEnabledTextColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getHoveredTextColour().asFloatArray();
                if(ImGui.colorEdit4("Hovered Text", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setHoveredTextColour(new ColourProperty(inputArray));
                }

                inputArray = component.getStyling().getDisabledTextColour().asFloatArray();
                if(ImGui.colorEdit4("Disabled Text", inputArray, ImGuiColorEditFlags.NoInputs)){
                    component.getStyling().setDisabledTextColour(new ColourProperty(inputArray));
                }

                ImGui.treePop();
            }
        }
        ImGui.endChild();

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.pushFont(OPEN_SANS_24PX);
        ImGui.text("Selected Data");
        ImGui.popFont();

        ImGui.sameLine(0, 20);
        if(ImGui.button(FontAwesome.Plus + " Add")){
            ImGui.openPopup("Add Data Wizard");
        }
        renderDataCreationWizard(x, y, partialTicks);

        if (ImGui.beginTable("source-data", 1, ImGuiTableFlags.Sortable | ImGuiTableFlags.RowBg | ImGuiTableFlags.BordersOuter | ImGuiTableFlags.ScrollY | ImGuiTableFlags.ScrollX, 334, 230)){

            List<SourceData> sources = ClientDataCache.INSTANCE.getOrRequestNetworkSources(displayGSI.display.getNetworkID(), false);

            Map<SourceData, Map<DataAddress, IData>> dataMap = new HashMap<>();

            for(DataAddress dataAddress : displayGSI.display.getDataWatcher().getWatchingAddressList()){
                IData data = ClientDataCache.INSTANCE.dataMap.get(dataAddress);
                Optional<SourceData> sourceData = sources.stream().filter(s -> s.address.equals(dataAddress.source)).findFirst();
                if(data != null && sourceData.isPresent()){
                    dataMap.putIfAbsent(sourceData.get(), new HashMap<>());
                    dataMap.get(sourceData.get()).put(dataAddress, data);
                }else if(!sourceData.isPresent()){
                    ClientDataCache.INSTANCE.requestedSources = -1;
                }
            }

            for(Map.Entry<SourceData, Map<DataAddress, IData>> entry : dataMap.entrySet()){
                ImGui.tableNextRow(ImGuiTableRowFlags.Headers, 18);

                ImGui.tableSetColumnIndex(0);
                ImGui.text(entry.getKey().stack.getDisplayName().getFormattedText());

                for(Map.Entry<DataAddress, IData> dataEntry : entry.getValue().entrySet()){
                    ImGui.tableNextRow(ImGuiTableRowFlags.None, 18);
                    ImGui.tableSetColumnIndex(0);
                    ImGui.textColored(dataEntry.getKey().method.getDataType().subType.getSubTypeColour(), dataEntry.getKey().method.getDataType().getRegistryName());
                    ImGui.sameLine(60, 0);
                    ImGui.textColored(ScreenUtils.light_grey.getARGB(), dataEntry.getKey().method.getMethodName() + " ");
                    ImGui.sameLine(190, 0);
                    ImGui.text(dataEntry.getValue().toString());
                    //ImGui.tableSetColumnIndex(2);
                }
            }
            ImGui.endTable();
        }

        ImGui.endChild();

        ImGui.sameLine();

        //// NODE EDITOR \\\\
         /*


          */

        ImGui.beginChild("Viewport", viewportFrameBuffer.width + 12, viewportFrameBuffer.height + 12  + 50);
        renderViewport(x, y, partialTicks);
        ImGui.endChild();
        ImGui.end();
    }

    public SourceData selectedSourceData = null;
    public List<Method> selectedMethods = new ArrayList<>();

    public void renderDataCreationWizard(int x, int y, float partialTicks) {
        if(ImGui.beginPopup("Add Data Wizard")){

            /*
            for(MethodCategory category : MethodCategory.categories){
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
             */
            ImGui.pushFont(OPEN_SANS_24PX);
            ImGui.text("Select a data source");
            ImGui.sameLine();
            ImGui.textColored(selectedSourceData == null ? ScreenUtils.red_button.getARGB() : ScreenUtils.green_button.getARGB(), selectedSourceData == null ? FontAwesome.Times : FontAwesome.Check);
            ImGui.popFont();

            ImGui.spacing();
            if (ImGui.beginTable("network-sources", 4, ImGuiTableFlags.Sortable | ImGuiTableFlags.SizingFixedFit | ImGuiTableFlags.RowBg | ImGuiTableFlags.BordersOuter | ImGuiTableFlags.ScrollY | ImGuiTableFlags.PadOuterX, 670, 200)){

                ImGui.tableSetupColumn("icon", ImGuiTableColumnFlags.WidthFixed, 36);
                ImGui.tableSetupColumn("name", ImGuiTableColumnFlags.WidthFixed, 200);
                ImGui.tableSetupColumn("address", ImGuiTableColumnFlags.WidthFixed, 250);
                ImGui.tableSetupColumn("type", ImGuiTableColumnFlags.WidthFixed, 250);

                List<SourceData> sources = ClientDataCache.INSTANCE.getOrRequestNetworkSources(displayGSI.display.getNetworkID(), false);
                int index = 0;
                for(SourceData source : sources){
                    boolean isSourceSelected = selectedSourceData == source;
                    ImGui.tableNextRow(ImGuiTableRowFlags.None, 36);
                    for (int column = 0; column < 4; column++){
                        ImGui.tableSetColumnIndex(column);
                        switch (column){
                            case 0:
                                if(ImGui.selectable("##" + index + ":" + "source", isSourceSelected, ImGuiSelectableFlags.DontClosePopups | ImGuiSelectableFlags.SpanAllColumns, 0, 36)){
                                    if(isSourceSelected){
                                        selectedSourceData = null;
                                        selectedMethods.clear();
                                    }else{
                                        selectedSourceData = source;
                                    }
                                }
                                ImGui.sameLine(0, 0);
                                itemStackImage(source.stack);
                                break;
                            case 1:
                                ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY() + 7);
                                ImGui.text(source.stack.getDisplayName().getFormattedText());
                                break;
                            case 2:
                                ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY() + 7);
                                ImGui.text(source.address.toString());
                                break;
                            case 3:
                                String type = "Unknown";
                                if(source.address instanceof BlockAddress){
                                    type = "Block";
                                }
                                ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY() + 7);
                                ImGui.text(type);
                                break;
                        }
                    }
                    index++;
                }
                ImGui.endTable();
            }

            ImGui.spacing();
            ImGui.spacing();
            ImGui.spacing();

            ImGui.pushFont(OPEN_SANS_24PX);
            ImGui.text("Select method(s)");
            ImGui.sameLine();
            ImGui.textColored(selectedMethods.isEmpty() ? ScreenUtils.red_button.getARGB() : ScreenUtils.green_button.getARGB(), selectedMethods.isEmpty() ? FontAwesome.Times : FontAwesome.Check);
            ImGui.popFont();
            ImGui.spacing();
            ImGui.spacing();

            ImGui.pushStyleColor(ImGuiCol.TableHeaderBg, 200, 200, 200, 50);
            if (ImGui.beginTable("source-data", 3, ImGuiTableFlags.Sortable | ImGuiTableFlags.RowBg | ImGuiTableFlags.BordersOuter | ImGuiTableFlags.ScrollY | ImGuiTableFlags.PadOuterX, 670, 300)){
                if(selectedSourceData != null){
                    Map<MethodCategory, Map<Method, IData>> methodMap = ClientDataCache.INSTANCE.getOrRequestAvailableMethods(displayGSI.display.getNetworkID(), selectedSourceData.address, false);

                    ImGui.tableSetupColumn("name", ImGuiTableColumnFlags.WidthFixed, 250);
                    ImGui.tableSetupColumn("type", ImGuiTableColumnFlags.WidthFixed, 250);

                    for(Map.Entry<MethodCategory, Map<Method, IData>> entry : methodMap.entrySet()){
                        ImGui.tableNextRow(ImGuiTableRowFlags.Headers, 30);
                        ImGui.tableSetColumnIndex(0);
                        ImGui.pushFont(OPEN_SANS_24PX);
                        ImGui.text(entry.getKey().getID());
                        ImGui.popFont();
                        for(Map.Entry<Method, IData> methodEntry : entry.getValue().entrySet()){
                            ImGui.tableNextRow(ImGuiTableRowFlags.None, 24);
                            boolean isSelected = selectedMethods.contains(methodEntry.getKey());
                            for (int column = 0; column < 3; column++){
                                ImGui.tableSetColumnIndex(column);
                                switch (column){
                                    case 0:
                                        ImGui.pushID(methodEntry.getKey().getRegistryName());
                                        if(ImGui.selectable(methodEntry.getKey().getMethodName(), isSelected,ImGuiSelectableFlags.DontClosePopups | ImGuiSelectableFlags.SpanAllColumns, 0, 24)){
                                            if(isSelected){
                                                selectedMethods.remove(methodEntry.getKey());
                                            }else{
                                                selectedMethods.add(methodEntry.getKey());
                                            }
                                        }
                                        ImGui.popID();
                                        break;
                                    case 1:
                                        ImGui.text(String.valueOf(methodEntry.getValue()));
                                        break;
                                    case 2:
                                        ImGui.textColored(methodEntry.getKey().getDataType().subType.getSubTypeColour(), methodEntry.getKey().getDataType().getRegistryName());
                                        break;
                                }
                            }
                        }
                    }
                }
                ImGui.endTable();
            }
            ImGui.popStyleColor();

            ImGui.spacing();

            ImGui.pushFont(OPEN_SANS_24PX);
            ImGui.pushStyleColor(ImGuiCol.Button, ScreenUtils.transparent_red_button.getARGB());
            if(ImGui.button("Cancel " + FontAwesome.Times, 662/2F, 36)){
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor();
            ImGui.pushStyleColor(ImGuiCol.Button, ScreenUtils.transparent_green_button.getARGB());
            ImGui.sameLine();

            if(ImGui.button("Confirm " + FontAwesome.Check, 662/2F, 36)){
                if(selectedSourceData != null && !selectedMethods.isEmpty()){
                    List<DataAddress> dataAddressList = new ArrayList<>();
                    for(Method method : selectedMethods){
                        dataAddressList.add(Address.createDataAddress(selectedSourceData.address, method));
                    }
                    PL3PacketHandler.INSTANCE.sendToServer(new GeneralPacket(GeneralPackets.Types.DATA_ADDRESS_SELECTION, displayGSI.display.getAddress(), true, dataAddressList));
                }
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor();
            ImGui.popFont();
            ImGui.endPopup();
        }
    }

    public void renderEditor(int x, int y, float partialTicks) {

    }

    public void renderNodeEditor(int x, int y, float partialTicks) {
        NodeGraphUtils.render(displayGSI.nodeGraph);
    }

    public void renderViewport(int x, int y, float partialTicks) {
        float[] inputArray;

        ImGui.beginGroup();
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
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 10);
        inputArray = GSIDesignSettings.selectedColour.asFloatArray();
        if (ImGui.colorEdit4("text_colour", inputArray, ImGuiColorEditFlags.NoInputs | ImGuiColorEditFlags.NoLabel)) {
            GSIDesignSettings.selectedColour = new ColourProperty(inputArray);
        }
        ImGui.popStyleVar();
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 10, 7);

        ImGui.sameLine();
        ImGui.spacing();

        ImGui.sameLine();

        ImGui.pushFont(OPEN_SANS_24PX);
        ImGui.pushID("fontsize");
        ImGui.pushItemWidth(80);
        int[] fontSize = new int[]{GSIDesignSettings.getPixelFontHeight()};

        if (ImGui.dragInt("##fontsize", fontSize, 0.1F, 1, 128, "%.2f px")) {
            GSIDesignSettings.setFontHeight(fontSize[0]);
        }
        if (ImGui.isItemHovered()) {
            ImGui.setTooltip("Font Size");
        }
        ImGui.popID();

        ImGui.sameLine(0, 90);
        inputArray = new float[]{widget.scaleMultiplier};
        if(ImGui.dragFloat("##Scale", inputArray, 0.01F, 0.01F, 10F, "%.2f x")){
            widget.scaleMultiplier = inputArray[0];
        }

        ImGui.sameLine();
        if(ImGui.button(FontAwesome.SearchMinus)){
            widget.scaleMultiplier = MathUtils.clamp((float)(widget.scaleMultiplier - 0.1F), 0.1F, 10F);
        }
        ImGui.sameLine();
        if(ImGui.button(FontAwesome.SearchPlus)){
            widget.scaleMultiplier = MathUtils.clamp((float)(widget.scaleMultiplier + 0.1F), 0.1F, 10F);
        }
        ImGui.popItemWidth();
        ImGui.popFont();
        ImGui.popStyleVar();
        ImGui.endGroup();

        ///// MAIN VIEWPORT \\\\\\

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
