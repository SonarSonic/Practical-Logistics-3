package sonar.logistics.client.nodes;

import imgui.ImGui;
import imgui.extension.nodeditor.NodeEditor;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImLong;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.client.imgui.FontAwesome;
import sonar.logistics.client.imgui.ImGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class NodeGraph {

    public int nodeID = 1;
    public List<Graph.Node> nodes = new ArrayList<>();
    public List<Graph.Link> links = new ArrayList<>();

    public Graph.Pin newLinkPin = null;

    public int nextNodeID(){
        return nodeID++;
    }

    public void render(){
        NodeEditor.setCurrentEditor(ImGuiScreen.nodeEditorContext);

        NodeEditor.begin("My Editor");

        // Render the nodes
        for(Graph.Node node : nodes){
            NodeEditor.beginNode(node.nodeID);
            ImGui.textColored(120, 180, 255, 255, node.name);
            ImGui.beginGroup();
            for(Graph.Pin inputPin : node.inputs){
                NodeEditor.beginPin(inputPin.pinID, inputPin.kind.getKind());
                ImGui.textColored(0.25F, 1F, 0.25F, 1F, FontAwesome.CaretRight);
                NodeEditor.endPin();
                ImGui.sameLine();
                ImGui.text(inputPin.type.name());
            }

            ImGui.endGroup();
            ImGui.text("");
            ImGui.sameLine(0, 80);
            ImGui.beginGroup();
            for(Graph.Pin outputPin : node.outputs){
                ImGui.text(outputPin.type.name());
            }
            ImGui.endGroup();
            ImGui.sameLine();
            ImGui.beginGroup();
            for(Graph.Pin outputPin : node.outputs){
                NodeEditor.beginPin(outputPin.pinID, outputPin.kind.getKind());
                ImGui.textColored(0.25F, 0.25F, 1F, 1F, FontAwesome.CaretRight);
                NodeEditor.endPin();
            }
            ImGui.endGroup();
            NodeEditor.endNode();
        }

        // Render the links
        for(Graph.Link link : links){
            NodeEditor.link(link.linkID, link.startPinID, link.endPinID);
        }

        // Handle link creation
        if(NodeEditor.beginCreate(255, 255, 255, 255, 2.0F)){
            ImLong startPinId = new ImLong(0);
            ImLong endPinId = new ImLong(0);

            if(NodeEditor.queryNewLink(startPinId, endPinId)){
                Graph.Pin startPin =  findPin((int)startPinId.get());
                Graph.Pin endPin =  findPin((int)endPinId.get());

                newLinkPin = startPin != null ? startPin : endPin;

                if (startPin != null && startPin.kind == Graph.PinKind.Input){
                    Graph.Pin swapPin = startPin;
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
                    }else if (isRecursiveLink(startPin, endPin)){
                        ImGuiScreen.floatingLabel(FontAwesome.Times + " Recursive Link", 255, 32, 32, 180);
                        NodeEditor.rejectNewItem(1, 0F, 0F, 1, 4.0F);
                    }else{
                        ImGuiScreen.floatingLabel(FontAwesome.Check + " Create Link", 32, 128, 32, 180);
                        if(NodeEditor.acceptNewItem(0.5F, 1, 0.5F, 1, 4.0F)){
                            Graph.Link link = new Graph.Link(nextNodeID(), startPin.pinID, endPin.pinID, null);
                            unlinkPin(endPin.pinID); //remove existing links to input pin
                            links.add(link);
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
                if (NodeEditor.acceptDeletedItem()){
                    Graph.Link link = findLink((int)deletedLinkId.get());
                    links.remove(link);
                }
                // You may reject link deletion by calling:
                // ed::RejectDeletedItem();
            }

            ImLong deletedNodeId = new ImLong(0);
            while (NodeEditor.queryDeletedNode(deletedNodeId)){
                if (NodeEditor.acceptDeletedItem()){
                    Graph.Node node = findNode((int)deletedNodeId.get());
                    nodes.remove(node);
                }
            }
        }
        NodeEditor.endDelete(); // Wrap up deletion action

        NodeEditor.end();
    }

    public Graph.Node findNode(int nodeID){
        for (Graph.Node node : nodes){
            if(node.nodeID == nodeID){
                return node;
            }
        }
        return null;
    }

    public Graph.Link findLink(int linkID){
        for (Graph.Link link : links){
            if(link.linkID == linkID){
                return link;
            }
        }
        return null;
    }

    public Graph.Pin findPin(int pinID){
        for(Graph.Node node : nodes){
            for(Graph.Pin inputPin : node.inputs){
                if(inputPin.pinID == pinID){
                    return inputPin;
                }
            }
            for(Graph.Pin outputPin : node.outputs){
                if(outputPin.pinID == pinID){
                    return outputPin;
                }
            }
        }
        return null;
    }

    public List<Graph.Link> getLinks(int pinID){
        List<Graph.Link> connectedLinks = new ArrayList<>();
        for(Graph.Link link : links){
            if(link.startPinID == pinID || link.endPinID == pinID){
                connectedLinks.add(link);
            }
        }
        return connectedLinks;
    }

    public void unlinkPin(int pinID){
        links.removeAll(getLinks(pinID));
    }

    public boolean isPinLinked(int pinID){
        for(Graph.Link link : links){
            if(link.startPinID == pinID || link.endPinID == pinID){
                return true;
            }
        }
        return false;
    }

    public boolean isRecursiveLink(Graph.Pin startPin, Graph.Pin endPin){
        return recursiveNodeCheck(new ArrayList<>(), endPin.node, startPin.node);
    }

    private boolean recursiveNodeCheck(List<Integer> checking, Graph.Node invalid, Graph.Node toCheck){
        for(Graph.Pin inputs : toCheck.inputs){
            for(Graph.Link link : getLinks(inputs.pinID)){
                Graph.Pin startPin = findPin(link.startPinID);
                if(startPin != null){
                    if(startPin.node == invalid){
                        return true;
                    }
                    if(!checking.contains(startPin.node.nodeID)){
                        checking.add(startPin.node.nodeID);
                        if(recursiveNodeCheck(checking, invalid, startPin.node)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean canCreateLink(Graph.Pin pinA, Graph.Pin pinB){
        if(pinA == null || pinB == null || pinA.kind == pinB.kind || pinA.type != pinB.type || pinA.node == pinB.node){
            return false;
        }
        return true;
    }

}
