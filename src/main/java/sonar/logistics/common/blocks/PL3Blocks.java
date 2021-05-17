package sonar.logistics.common.blocks;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sonar.logistics.client.gui.GSIDesignContainer;
import sonar.logistics.common.blocks.hammer.ForgingHammerBlock;
import sonar.logistics.common.blocks.hammer.ForgingHammerContainer;
import sonar.logistics.common.blocks.hammer.ForgingHammerTile;
import sonar.logistics.common.blocks.host.MultipartHostBlock;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.blocks.ores.SapphireOreBlock;
import sonar.logistics.common.multiparts.cable.DataCableBlock;
import sonar.logistics.common.multiparts.displays.DisplayScreenBlock;
import sonar.logistics.common.multiparts.displays.LargeDisplayScreenBlock;
import sonar.logistics.common.multiparts.displays.MiniDisplayScreenBlock;
import sonar.logistics.common.multiparts.reader.ReaderBlock;

@ObjectHolder("practicallogistics3")
public class PL3Blocks {

    @ObjectHolder("sapphire_ore")
    public static final SapphireOreBlock SAPPHIRE_ORE = null;

    @ObjectHolder("forging_hammer")
    public static final ForgingHammerBlock FORGING_HAMMER_BLOCK = null;

    @ObjectHolder("forging_hammer")
    public static final TileEntityType<ForgingHammerTile> FORGING_HAMMER_TILE = null;

    @ObjectHolder("forging_hammer")
    public static final ContainerType<ForgingHammerContainer> FORGING_HAMMER_CONTAINER = null;


    ///// MULTIPARTS

    @ObjectHolder("multipart_host")
    public static final MultipartHostBlock MULTIPART_HOST_BLOCK = null;

    @ObjectHolder("multipart_host")
    public static final TileEntityType<NetworkedHostTile> MULTIPART_HOST_TILE = null;

    @ObjectHolder("data_cable")
    public static final DataCableBlock DATA_CABLE = null;

    @ObjectHolder("reader")
    public static final ReaderBlock READER_BLOCK = null;

    @ObjectHolder("mini_display_screen")
    public static final MiniDisplayScreenBlock MINI_DISPLAY_SCREEN = null;

    @ObjectHolder("display_screen")
    public static final DisplayScreenBlock DISPLAY_SCREEN = null;

    @ObjectHolder("large_display_screen")
    public static final LargeDisplayScreenBlock LARGE_DISPLAY_SCREEN = null;

    ///// GSI

    @ObjectHolder("gsi_container")
    public static final ContainerType<GSIDesignContainer> GSI_DESIGN_CONTAINER = null;

}
