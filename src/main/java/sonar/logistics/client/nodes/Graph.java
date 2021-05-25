package sonar.logistics.client.nodes;

import imgui.ImVec2;
import imgui.extension.nodeditor.flag.NodeEditorPinKind;
import sonar.logistics.client.gsi.style.properties.ColourProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

///Based on: https://github.com/thedmd/imgui-node-editor/blob/master/examples/blueprints-example/blueprints-example.cpp
public class Graph {

    public enum PinType{
        Flow,
        Bool,
        Int,
        Float,
        String,
        Object,
        Function,
        Delegate,
        Inventory,
        ItemStack,
        Item,
        NBT
    };

    public enum PinKind {
        Input,
        Output;

        public int getKind(){
            return this == Output ? NodeEditorPinKind.Output : NodeEditorPinKind.Input;
        }
    };

    public enum NodeType {
        Blueprint,
        Simple,
        Tree,
        Comment,
        Houdini
    };

    public static class  Pin {
        public int pinID;
        public Node node;
        public String name;
        public PinType type;
        public PinKind kind;

        public Pin(int pinID, Node node, String name, PinType type, PinKind kind) {
            this.pinID = pinID;
            this.node = node;
            this.name = name;
            this.type = type;
            this.kind = kind;
        }
    };

    public static class Node {
        public int nodeID;
        public String name;
        public List<Pin> inputs;
        public List<Pin> outputs;
        public ColourProperty colour;
        public NodeType type;
        public ImVec2 size;

        public String state;
        public String savedState;

        public Node(int nodeID, String name, List<Pin> inputs, List<Pin> outputs, NodeType type, @Nullable ColourProperty colour, @Nullable ImVec2 size) {
            this.nodeID = nodeID;
            this.name = name;
            this.inputs = inputs;
            this.outputs = outputs;
            this.type = type;
            this.colour = colour == null ? new ColourProperty(255, 255, 255) : colour;
            this.size = size == null ? new ImVec2(0, 0) : size;
        }

    }

    public static class Link {
        public int linkID;

        public int startPinID;
        public int endPinID;

        public ColourProperty colour;

        public Link(int linkID, int startPinID, int endPinID, @Nullable ColourProperty colour) {
            this.linkID = linkID;
            this.startPinID = startPinID;
            this.endPinID = endPinID;
            this.colour = colour == null ? new ColourProperty(255, 255, 255) : colour;
        }
    };


}
