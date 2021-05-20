package sonar.logistics.server.data.methods.categories;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

import java.util.ArrayList;
import java.util.List;

public class DataCategories {

    public static final List<DataCategory> categories = new ArrayList<>();

    public static DataCategory BLOCKS;
    public static DataCategory ENTITIES;
    public static DataCategory WORLD;
    public static DataCategory INVENTORIES;
    public static DataCategory FLUIDS;
    public static DataCategory ENERGY;
    public static DataCategory MACHINES;
    public static DataCategory NETWORK;

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

    public static DataCategory registerCategory(String id, String icon){
        return registerCategory(new DataCategory(id, new ResourceLocation(icon)));
    }

    public static DataCategory registerCategory(DataCategory category){
        categories.add(category);
        return category;
    }

    public static void clear(){
        categories.clear();
    }

}
