package sonar.logistics.client.nodes;

import net.minecraft.nbt.CompoundNBT;
import sonar.logistics.server.address.DataAddress;
import sonar.logistics.server.data.DataManager;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSourceNode implements Graph.INode {

    public int nodeID;
    public DataAddress address;
    public List<Graph.DataPin> inputPins;
    public List<Graph.DataPin> outputPins;

    public DataSourceNode(){
        this.inputPins = new ArrayList<>();
        this.outputPins = new ArrayList<>();
    }

    public DataSourceNode(DataAddress dataAddress){
        this();
        this.address = dataAddress;
    }

    @Override
    public void updateOutputs(Map<Integer, Object> inputs, Graph.IPinResultCache resultCache) {
        resultCache.cachePinOutput(getSourcePinID(), address, DataManager.getData(address)); //TODO THIS WILL BREAK CLIENT SIDE!!!
    }

    public int getSourcePinID(){
        return outputPins.get(0).pinID;
    }

    @Override
    public void setupNodePins() {
        this.outputPins.add(new Graph.DataPin(this, "Data", address.method.getDataType(), Graph.PinKind.Output));
    }

    @Override
    public void destroyNodePins() {}


    @Override
    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    @Override
    public int getNodeID() {
        return nodeID;
    }

    @Override
    public String getNodeName() {
        return address.method.getMethodName();
    }

    @Override
    public List<Graph.DataPin> getInputPins() {
        return inputPins;
    }

    @Override
    public List<Graph.DataPin> getOutputPins() {
        return outputPins;
    }

    @Override
    public Graph.NodeType getNodeType() {
        return Graph.NodeType.Source;
    }

    @Override
    public CompoundNBT read(CompoundNBT nbt, EnumSyncType syncType) {
        nodeID = nbt.getInt("nodeID");
        address = (DataAddress) Registries.getAddressRegistry().read(nbt, "address", EnumSyncType.SAVE);

        //pins
        setupNodePins();
        NodeGraphUtils.readPinIDs(nbt, "inputPins", inputPins);
        return nbt;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt, EnumSyncType syncType) {
        nbt.putInt("nodeID", nodeID);
        Registries.getAddressRegistry().write(address, nbt, "address", EnumSyncType.SAVE);

        //pins
        NodeGraphUtils.writePinIDs(nbt, "inputPins", inputPins);
        return nbt;
    }
}
