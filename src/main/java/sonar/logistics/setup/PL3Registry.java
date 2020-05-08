package sonar.logistics.setup;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import sonar.logistics.PL3;
import sonar.logistics.blocks.PL3Blocks;
import sonar.logistics.blocks.hammer.ForgingHammerContainer;
import sonar.logistics.blocks.host.MultipartHostBlock;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.blocks.host.NetworkedHostTile;
import sonar.logistics.blocks.ores.SapphireOreBlock;
import sonar.logistics.blocks.hammer.ForgingHammerBlock;
import sonar.logistics.blocks.hammer.ForgingHammerTile;
import sonar.logistics.client.design.gui.GSIDesignContainer;
import sonar.logistics.items.crafting.*;
import sonar.logistics.multiparts.cable.DataCableBlock;
import sonar.logistics.multiparts.displays.DisplayScreenBlock;
import sonar.logistics.multiparts.displays.LargeDisplayScreenBlock;
import sonar.logistics.multiparts.displays.MiniDisplayScreenBlock;
import sonar.logistics.multiparts.utils.MultipartBlockItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PL3Registry {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("practicallogistics3") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(PL3Blocks.SAPPHIRE_ORE);
        }
    };

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        PL3.LOGGER.info("Starting: Block Registry Event");

        event.getRegistry().register(new SapphireOreBlock());
        event.getRegistry().register(new ForgingHammerBlock());


        event.getRegistry().register(new MultipartHostBlock());

        Block.Properties multipart_props = Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f);
        event.getRegistry().register(new DataCableBlock(multipart_props).setRegistryName("data_cable"));
        event.getRegistry().register(new MiniDisplayScreenBlock(multipart_props).setRegistryName("mini_display_screen"));
        event.getRegistry().register(new DisplayScreenBlock(multipart_props).setRegistryName("display_screen"));
        event.getRegistry().register(new LargeDisplayScreenBlock(multipart_props).setRegistryName("large_display_screen"));

        PL3.LOGGER.info("Finishing: Block Registry Event");
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        PL3.LOGGER.info("Starting: Item Registry Event");

        Item.Properties props =  new Item.Properties().group(ITEM_GROUP);

        ///BLOCKS
        event.getRegistry().register(new BlockItem(PL3Blocks.SAPPHIRE_ORE, props).setRegistryName("sapphire_ore"));
        event.getRegistry().register(new BlockItem(PL3Blocks.FORGING_HAMMER_BLOCK, props).setRegistryName("forging_hammer"));

        event.getRegistry().register(new MultipartBlockItem(PL3Blocks.DATA_CABLE, props).setRegistryName("data_cable"));
        event.getRegistry().register(new MultipartBlockItem(PL3Blocks.MINI_DISPLAY_SCREEN, props).setRegistryName("mini_display_screen"));
        event.getRegistry().register(new MultipartBlockItem(PL3Blocks.DISPLAY_SCREEN, props).setRegistryName("display_screen"));
        event.getRegistry().register(new MultipartBlockItem(PL3Blocks.LARGE_DISPLAY_SCREEN, props).setRegistryName("large_display_screen"));

        ///ITEMS
        event.getRegistry().register(new SapphireGemItem());
        event.getRegistry().register(new SapphireDustItem());
        event.getRegistry().register(new StonePlateItem());
        event.getRegistry().register(new EtchedPlateItem());
        event.getRegistry().register(new SignallingPlateItem());
        event.getRegistry().register(new WirelessPlateItem());

        PL3.LOGGER.info("Finishing: Item Registry Event");
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(ForgingHammerTile::new, PL3Blocks.FORGING_HAMMER_BLOCK).build(null).setRegistryName("forging_hammer"));
        event.getRegistry().register(TileEntityType.Builder.create(NetworkedHostTile::new, PL3Blocks.MULTIPART_HOST_BLOCK).build(null).setRegistryName("multipart_host"));
    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new ForgingHammerContainer(windowId, inv, pos, PL3.proxy.getClientWorld());
            }).setRegistryName("forging_hammer"));

        event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new GSIDesignContainer(inv)).setRegistryName("gsi_container"));
    }
}