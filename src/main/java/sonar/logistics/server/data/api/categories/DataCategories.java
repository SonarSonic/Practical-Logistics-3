package sonar.logistics.server.data.api.categories;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.PL3;

import java.util.ArrayList;
import java.util.List;

public class DataCategories {

    public static final List<IDataCategory> categories = new ArrayList<>();

    public static IDataCategory BLOCKS;
    public static IDataCategory ENTITIES;
    public static IDataCategory WORLD;
    public static IDataCategory INVENTORIES;
    public static IDataCategory FLUIDS;
    public static IDataCategory ENERGY;
    public static IDataCategory MACHINES;
    public static IDataCategory NETWORK;

    static{
        BLOCKS = registerCategory("blocks", PL3.MODID + ":textures/items/signalling_plate.png");
        ENTITIES = registerCategory("entities", PL3.MODID + ":textures/items/signalling_plate.png");
        WORLD = registerCategory("world", PL3.MODID + ":textures/items/wireless_plate.png");
        INVENTORIES = registerCategory("inventories", PL3.MODID + ":textures/items/signalling_plate.png");
        FLUIDS = registerCategory("fluids", PL3.MODID + ":textures/items/signalling_plate.png");
        ENERGY = registerCategory("energy", PL3.MODID + ":textures/items/transceiver.png");
        MACHINES = registerCategory("machines", PL3.MODID + ":textures/items/etched_plate.png");
        NETWORK = registerCategory("networking", PL3.MODID + ":textures/items/wireless_monitor.png");
    }

    public static IDataCategory registerCategory(String id, String icon){
        return registerCategory(new DataCategory(id, new ResourceLocation(icon)));
    }

    public static IDataCategory registerCategory(IDataCategory category){
        categories.add(category);
        return category;
    }

    public static void clear(){
        categories.clear();
    }

}
