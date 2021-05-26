package sonar.logistics.client.nodes;

import imgui.ImVec2;
import imgui.extension.nodeditor.flag.NodeEditorPinKind;
import sonar.logistics.client.gsi.style.properties.ColourProperty;
import sonar.logistics.client.gui.ScreenUtils;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.data.types.DataType;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.util.network.INBTSyncable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

///Based on: https://github.com/thedmd/imgui-node-editor/blob/master/examples/blueprints-example/blueprints-example.cpp
public class Graph {

    /*
    public enum PinType{
        Boolean,
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

     */

    public enum PinKind {
        Input,
        Output;

        public int getKind(){
            return this == Output ? NodeEditorPinKind.Output : NodeEditorPinKind.Input;
        }
    };

    public enum NodeType {
        Source,
        Method,
        Component;
    };

    public static class DataPin {
        public int pinID;
        public INode node;
        public String name;
        public DataType type;
        public PinKind kind;

        public DataPin(INode node, String name, DataType type, PinKind kind) {
            this.pinID = -1;
            this.node = node;
            this.name = name;
            this.type = type;
            this.kind = kind;
        }
    }

    public interface INode extends INBTSyncable {

        ImVec2 defaultSize = new ImVec2(0, 0);

        void setupNodePins();

        void destroyNodePins();

        void setNodeID(int nodeID);

        int getNodeID();

        String getNodeName();

        List<Graph.DataPin> getInputPins();

        List<Graph.DataPin> getOutputPins();

        Graph.NodeType getNodeType();

        default void updateOutputs(Map<Integer, Object> inputs, IPinResultCache resultCache){

        }

        default ColourProperty getNodeColour(){
            return ScreenUtils.white;
        }

        default ImVec2 getNodeSize(){
            return defaultSize;
        }


    }

    @FunctionalInterface
    public interface IPinResultCache{

        void cachePinOutput(int pinID, @Nullable DataAddress address, IData data);

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
