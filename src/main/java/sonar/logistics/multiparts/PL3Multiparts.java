package sonar.logistics.multiparts;

import net.minecraft.util.ResourceLocation;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.multiparts.base.IMultipartBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PL3Multiparts {

    public static List<IMultipartBlock> MULTIPART_BLOCKS = new ArrayList<>();

    public static void register(){
        MULTIPART_BLOCKS.add(PL3Blocks.MINI_DISPLAY_SCREEN);
        MULTIPART_BLOCKS.add(PL3Blocks.DISPLAY_SCREEN);
        MULTIPART_BLOCKS.add(PL3Blocks.LARGE_DISPLAY_SCREEN);
        MULTIPART_BLOCKS.add(PL3Blocks.DATA_CABLE);
    }

    @Nullable
    public static IMultipartBlock getMultipart(ResourceLocation loc){
        for(IMultipartBlock multipart : MULTIPART_BLOCKS){
            if(multipart.getMultipartRegistryName().equals(loc)){
                return multipart;
            }
        }
        return null;
    }



}
