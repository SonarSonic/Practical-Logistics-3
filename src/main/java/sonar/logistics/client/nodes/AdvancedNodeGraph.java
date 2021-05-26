package sonar.logistics.client.nodes;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.server.ServerDataCache;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.address.PinOutputAddress;
import sonar.logistics.server.data.api.IData;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.network.INBTSyncable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO MARK DIRTY
public class AdvancedNodeGraph implements INBTSyncable {

    public int nodeGraphID;

    public Map<Integer, Graph.INode> nodeMap = new HashMap<>();
    public Map<Integer, Graph.DataPin> pinMap = new HashMap<>();
    public Map<Integer, Graph.Link> linkMap = new HashMap<>();

    public Map<DataAddress, DataSourceNode> sourceNodeMap = new HashMap<>();

    // computed values
    public Map<Integer, DataAddress> pinDataMap = new HashMap<>();
    public Map<Integer, IData> internalData = new HashMap<>();

    public boolean hasChanged = true;

    public AdvancedNodeGraph(){}

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        nodeGraphID = nbt.getInt("graphID");

        for(Graph.INode node : nodeMap.values()){
            //TODO
        }

        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putInt("graphID", nodeGraphID);
        return nbt;
    }

    public void processNodeGraph(){
        pinDataMap.clear();
        internalData.clear();
        List<Integer> processedNodes = new ArrayList<>();

        for(Graph.INode node : nodeMap.values()){
            recursiveNodeProcess(processedNodes, node);
        }
    }

    private void recursiveNodeProcess(List<Integer> processedNodes, Graph.INode startNode){
        Map<Integer, Object> inputValues = new HashMap<>();
        for(Graph.DataPin inputs : startNode.getInputPins()){
            for(Graph.Link link : NodeGraphUtils.getLinks(this, inputs.pinID)){
                Graph.DataPin startPin = NodeGraphUtils.findPin(this, link.startPinID);
                if(startPin != null){
                    if(!processedNodes.contains(startPin.node.getNodeID())){
                        processedNodes.add(startPin.node.getNodeID());
                        recursiveNodeProcess(processedNodes, startPin.node);
                    }
                }
            }
        }
        ///NODE OUTPUT
        startNode.updateOutputs(inputValues, this::cachePinOutput);
        processedNodes.add(startNode.getNodeID());
    }

    private void cachePinOutput(int pinID, @Nullable DataAddress address, IData data){
        if(address == null){
            internalData.put(pinID, data);
            pinDataMap.put(pinID, new PinOutputAddress(getNodeGraphID(), pinID));
        }else{
            pinDataMap.put(pinID, address);
        }
    }

    public DataAddress getPinAddress(int pinID){
        return pinDataMap.get(pinID);
    }

    public IData getInternalPinData(int pinID) {
        return internalData.get(pinID);
    }

    public int getNodeGraphID() {
        if(nodeGraphID == 0){
            nodeGraphID = ServerDataCache.INSTANCE.getNextIdentity();
        }
        return nodeGraphID;
    }

    public Map<Integer, Graph.INode> getNodes() {
        return nodeMap;
    }

    public Map<Integer, Graph.DataPin> getPinMap() {
        return pinMap;
    }

    public Map<Integer, Graph.Link> getLinks() {
        return linkMap;
    }

    public int nextNodeID() {
        return ServerDataCache.INSTANCE.getNextIdentity();
    }

    public void setupDataSourceNode(DataAddress dataAddress){
        if(!sourceNodeMap.containsKey(dataAddress)){
            DataSourceNode sourceNode = new DataSourceNode(dataAddress);
            sourceNodeMap.put(dataAddress, sourceNode);
            loadNode(sourceNode);
        }
    }

    public void destroyDataSourceNode(DataAddress dataAddress){
        DataSourceNode sourceNode = sourceNodeMap.get(dataAddress);
        if(sourceNode != null){
            destroyNode(sourceNode);
        }
    }

    public void setupNode(Graph.INode node){
        node.setNodeID(nextNodeID());
        node.setupNodePins();
        loadNode(node);
    }

    public void destroyNode(Graph.INode node){
        node.setNodeID(-1);
        node.destroyNodePins();
        unloadNode(node);
    }

    public void loadNode(Graph.INode node){
        nodeMap.put(node.getNodeID(), node);
        for(Graph.DataPin inputPin : node.getInputPins()){
            if(inputPin.pinID <= 0){
                inputPin.pinID = nextNodeID();
            }
            pinMap.put(inputPin.pinID, inputPin);
        }
        for(Graph.DataPin outputPin : node.getOutputPins()){
            if(outputPin.pinID <= 0){
                outputPin.pinID = nextNodeID();
            }
            pinMap.put(outputPin.pinID, outputPin);
        }
    }

    public void unloadNode(Graph.INode node){
        nodeMap.remove(node.getNodeID());
        for(Graph.DataPin inputPin : node.getInputPins()){
            pinMap.remove(inputPin.pinID);
        }
        for(Graph.DataPin outputPin : node.getOutputPins()){
            pinMap.remove(outputPin.pinID);
        }
    }


    public boolean canAddLink(Graph.Link link){
        return true;
    }

    public void addLink(Graph.Link link){
        getLinks().put(link.linkID, link);
    }

    public boolean canRemoveLink(Graph.Link link){
        return true;
    }

    public void removeLink(Graph.Link link){
        getLinks().remove(link.linkID);
    }

    public boolean canAddNode(Graph.INode node){
        return true;
    }

    public void addNode(Graph.INode node){
        getNodes().put(node.getNodeID(), node);
    }

    public boolean canRemoveNode(Graph.INode node){
        return true;
    }

    public void removeNode(Graph.INode node){
        getNodes().remove(node.getNodeID());
    }

}
