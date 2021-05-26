package sonar.logistics.client.nodes;

import imgui.ImGui;
import imgui.extension.nodeditor.NodeEditor;
import imgui.type.ImLong;
import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.client.imgui.FontAwesome;
import sonar.logistics.client.imgui.ImGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class NodeGraphUtils {

    public static void render(AdvancedNodeGraph nodeGraph){
        NodeEditor.setCurrentEditor(ImGuiScreen.nodeEditorContext);
        NodeEditor.begin("My Editor");

        // Render the nodes
        for(Graph.INode node : nodeGraph.getNodes().values()){
            NodeEditor.beginNode(node.getNodeID());
            ImGui.textColored(120, 180, 255, 255, node.getNodeName());
            ImGui.beginGroup();
            for(Graph.DataPin inputPin : node.getInputPins()){
                NodeEditor.beginPin(inputPin.pinID, inputPin.kind.getKind());
                ImGui.textColored(inputPin.type.subType.getSubTypeColour(), FontAwesome.CaretRight);
                NodeEditor.endPin();
                ImGui.sameLine();
                ImGui.text(inputPin.type.getRegistryName());
            }

            ImGui.endGroup();
            ImGui.text("");
            ImGui.sameLine(0, 80);
            ImGui.beginGroup();
            for(Graph.DataPin outputPin : node.getOutputPins()){
                ImGui.text(outputPin.type.getRegistryName());
            }
            ImGui.endGroup();
            ImGui.sameLine();
            ImGui.beginGroup();
            for(Graph.DataPin outputPin : node.getOutputPins()){
                NodeEditor.beginPin(outputPin.pinID, outputPin.kind.getKind());
                ImGui.textColored(outputPin.type.subType.getSubTypeColour(), FontAwesome.CaretRight);
                NodeEditor.endPin();
            }
            ImGui.endGroup();
            NodeEditor.endNode();
        }

        // Render the links
        for(Graph.Link link : nodeGraph.getLinks().values()){
            NodeEditor.link(link.linkID, link.startPinID, link.endPinID);
        }

        // Handle link creation
        if(NodeEditor.beginCreate(255, 255, 255, 255, 2.0F)){
            ImLong startPinId = new ImLong(0);
            ImLong endPinId = new ImLong(0);

            if(NodeEditor.queryNewLink(startPinId, endPinId)){
                Graph.DataPin startPin =  findPin(nodeGraph, (int)startPinId.get());
                Graph.DataPin endPin =  findPin(nodeGraph, (int)endPinId.get());

                //newLinkPin = startPin != null ? startPin : endPin;

                if (startPin != null && startPin.kind == Graph.PinKind.Input){
                    Graph.DataPin swapPin = startPin;
                    startPin = endPin;
                    endPin = swapPin;
                }

                if (startPin != null && endPin != null){
                    if (endPin == startPin){
                        NodeEditor.rejectNewItem(1, 0, 0, 1, 2.0F);
                    }
                    else if (endPin.kind == startPin.kind){
                        ImGuiScreen.floatingLabel(FontAwesome.Times + " Incompatible Pin Kind", 128, 32, 32, 180);
                        NodeEditor.rejectNewItem(1, 0, 0, 1, 2.0F);
                    }
                    else if (endPin.node == startPin.node){
                        ImGuiScreen.floatingLabel(FontAwesome.Times + " Cannot connect to self", 128, 32, 32, 180);
                        NodeEditor.rejectNewItem(1, 0, 0, 1, 1.0F);
                    }
                    else if (endPin.type != startPin.type){
                        ImGuiScreen.floatingLabel(FontAwesome.Times + " Incompatible Data Type", 128, 32, 32, 180);
                        NodeEditor.rejectNewItem(1, 0.5F, 0.5F, 1, 1.0F);
                    }else if (isRecursiveLink(nodeGraph, startPin, endPin)){
                        ImGuiScreen.floatingLabel(FontAwesome.Times + " Recursive Link", 255, 32, 32, 180);
                        NodeEditor.rejectNewItem(1, 0F, 0F, 1, 4.0F);
                    }else{
                        ImGuiScreen.floatingLabel(FontAwesome.Check + " Create Link", 32, 128, 32, 180);
                        if(NodeEditor.acceptNewItem(0.5F, 1, 0.5F, 1, 4.0F)){
                            Graph.Link link = new Graph.Link(nodeGraph.nextNodeID(), startPin.pinID, endPin.pinID, null);
                            if(unlinkPin(nodeGraph, endPin.pinID)){ //remove existing links to input pin
                                if(nodeGraph.canAddLink(link)){
                                    nodeGraph.addLink(link);
                                }
                            }
                        }
                    }

                }
            }
        }
        NodeEditor.endCreate();

        // Handle deletion action
        if (NodeEditor.beginDelete())
        {
            // There may be many links marked for deletion, let's loop over them.
            ImLong deletedLinkId = new ImLong(0);
            ImLong startPinId = new ImLong(0);
            ImLong endPinId = new ImLong(0);
            while (NodeEditor.queryDeletedLink(deletedLinkId, startPinId, endPinId)){
                // If you agree that link can be deleted, accept deletion.
                Graph.Link link = findLink(nodeGraph, (int)deletedLinkId.get());
                if (nodeGraph.canRemoveLink(link) && NodeEditor.acceptDeletedItem()){
                    nodeGraph.removeLink(link);
                }
                // You may reject link deletion by calling:
                // ed::RejectDeletedItem();
            }

            ImLong deletedNodeId = new ImLong(0);
            while (NodeEditor.queryDeletedNode(deletedNodeId)){
                Graph.INode node = findNode(nodeGraph, (int)deletedNodeId.get());
                if (nodeGraph.canRemoveNode(node) && NodeEditor.acceptDeletedItem()){
                    nodeGraph.removeNode(node);
                }
            }
        }
        NodeEditor.endDelete(); // Wrap up deletion action

        NodeEditor.end();
    }

    public static Graph.INode findNode(AdvancedNodeGraph graph, int nodeID){
        return graph.getNodes().get(nodeID);
    }

    public static Graph.Link findLink(AdvancedNodeGraph graph, int linkID){
        return graph.getLinks().get(linkID);
    }

    public static Graph.DataPin findPin(AdvancedNodeGraph graph, int pinID){
        return graph.getPinMap().get(pinID);
    }

    public static List<Graph.Link> getLinks(AdvancedNodeGraph graph, int pinID){
        List<Graph.Link> connectedLinks = new ArrayList<>();
        for(Graph.Link link : graph.getLinks().values()){
            if(link.startPinID == pinID || link.endPinID == pinID){
                connectedLinks.add(link);
            }
        }
        return connectedLinks;
    }

    public static boolean unlinkPin(AdvancedNodeGraph graph, int pinID){
        boolean failed = false;
        for(Graph.Link link : getLinks(graph, pinID)){
            if(graph.canRemoveLink(link)){
                graph.removeLink(link);
            }else{
                failed = true;
            }
        }
        return !failed;
    }

    public static boolean isPinLinked(AdvancedNodeGraph graph, int pinID){
        for(Graph.Link link : graph.getLinks().values()){
            if(link.startPinID == pinID || link.endPinID == pinID){
                return true;
            }
        }
        return false;
    }

    public static boolean isRecursiveLink(AdvancedNodeGraph graph, Graph.DataPin startPin, Graph.DataPin endPin){
        return recursiveNodeCheck(graph, new ArrayList<>(), endPin.node, startPin.node);
    }

    private static boolean recursiveNodeCheck(AdvancedNodeGraph graph, List<Integer> checking, Graph.INode invalid, Graph.INode toCheck){
        for(Graph.DataPin inputs : toCheck.getInputPins()){
            for(Graph.Link link : getLinks(graph, inputs.pinID)){
                Graph.DataPin startPin = findPin(graph, link.startPinID);
                if(startPin != null){
                    if(startPin.node == invalid){
                        return true;
                    }
                    if(!checking.contains(startPin.node.getNodeID())){
                        checking.add(startPin.node.getNodeID());
                        if(recursiveNodeCheck(graph, checking, invalid, startPin.node)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean canCreateLink(Graph.DataPin pinA, Graph.DataPin pinB){
        if(pinA == null || pinB == null || pinA.kind == pinB.kind || pinA.type != pinB.type || pinA.node == pinB.node){
            return false;
        }
        return true;
    }

    public static void writePinIDs(CompoundNBT nbt, String key, List<Graph.DataPin> dataPins){
        if(dataPins.size() != 0){
            int[] pinIDArray = new int[dataPins.size()];
            for(int i = 0; i < pinIDArray.length; i ++){
                pinIDArray[i] = dataPins.get(i).pinID;
            }
            nbt.putIntArray(key, pinIDArray);
        }
    }

    public static void readPinIDs(CompoundNBT nbt, String key, List<Graph.DataPin> dataPins){
        if(nbt.contains(key)){
            int[] pinIDArray = nbt.getIntArray(key);
            for(int i = 0; i < pinIDArray.length; i ++){
                if(i < dataPins.size()){
                    dataPins.get(i).pinID = pinIDArray[i];
                }
            }
        }
    }


}
