package sonar.logistics.server.data.methods;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

import java.util.ArrayList;
import java.util.List;

//TODO DROP CATEGORIES, MAKE IT WORK THEM OUT???
public class MethodCategory {

    public static final List<MethodCategory> categories = new ArrayList<>();

    public static MethodCategory BLOCKS;
    public static MethodCategory ENTITIES;
    public static MethodCategory WORLD;
    public static MethodCategory INVENTORIES;
    public static MethodCategory FLUIDS;
    public static MethodCategory ENERGY;
    public static MethodCategory MACHINES;
    public static MethodCategory NETWORK;

    static{
        BLOCKS = registerCategory("blocks", PL3.MODID + ":textures/item/signalling_plate.png");
        ENTITIES = registerCategory("entities", PL3.MODID + ":textures/item/signalling_plate.png");
        WORLD = registerCategory("world", PL3.MODID + ":textures/item/wireless_plate.png");
        INVENTORIES = registerCategory("inventories", PL3.MODID + ":textures/item/signalling_plate.png");
        FLUIDS = registerCategory("fluids", PL3.MODID + ":textures/item/signalling_plate.png");
        ENERGY = registerCategory("energy", PL3.MODID + ":textures/item/transceiver.png");
        MACHINES = registerCategory("machines", PL3.MODID + ":textures/item/etched_plate.png");
        NETWORK = registerCategory("networking", PL3.MODID + ":textures/item/wireless_monitor.png");
    }

    public static MethodCategory registerCategory(String id, String icon){
        return registerCategory(new MethodCategory(id, new ResourceLocation(icon)));
    }

    public static MethodCategory registerCategory(MethodCategory category){
        categories.add(category);
        return category;
    }

    public static void clear(){
        categories.clear();
    }




    //// METHOD CATEGORY IMPL \\\\

    public final String id;
    public final ResourceLocation icon;

    public MethodCategory(String id, ResourceLocation icon){
        this.id = id;
        this.icon = icon;
    }

    public String getID() {
        return id;
    }

    public ResourceLocation getIconLocation() {
        return icon;
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MethodCategory){
            MethodCategory category = (MethodCategory) obj;
            return category.getID().equals(getID());
        }
        return super.equals(obj);
    }
}
