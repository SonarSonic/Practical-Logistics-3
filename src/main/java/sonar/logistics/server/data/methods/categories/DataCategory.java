package sonar.logistics.server.data.methods.categories;

import net.minecraft.util.ResourceLocation;

public class DataCategory {

    public final String id;
    public final ResourceLocation icon;

    public DataCategory(String id, ResourceLocation icon){
        this.id = id;
        this.icon = icon;
    }

    public String getID() {
        return id;
    }

    public ResourceLocation getIconLocation() {
        return icon;
    }
}
