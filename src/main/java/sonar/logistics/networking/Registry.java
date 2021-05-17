package sonar.logistics.networking;

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
import sonar.logistics.PL3;
import sonar.logistics.client.gui.GSIDesignContainer;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.blocks.hammer.ForgingHammerBlock;
import sonar.logistics.common.blocks.hammer.ForgingHammerContainer;
import sonar.logistics.common.blocks.hammer.ForgingHammerTile;
import sonar.logistics.common.blocks.host.MultipartHostBlock;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.blocks.ores.SapphireOreBlock;
import sonar.logistics.common.items.crafting.*;
import sonar.logistics.common.multiparts.cable.DataCableBlock;
import sonar.logistics.common.multiparts.displays.DisplayScreenBlock;
import sonar.logistics.common.multiparts.displays.LargeDisplayScreenBlock;
import sonar.logistics.common.multiparts.displays.MiniDisplayScreenBlock;
import sonar.logistics.common.multiparts.reader.ReaderBlock;
import sonar.logistics.common.multiparts.utils.MultipartBlockItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

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

        Block.Properties multipartProps = Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f);
        event.getRegistry().register(new DataCableBlock(multipartProps).setRegistryName("data_cable"));
        event.getRegistry().register(new ReaderBlock(multipartProps).setRegistryName("reader"));
        event.getRegistry().register(new MiniDisplayScreenBlock(multipartProps).setRegistryName("mini_display_screen"));
        event.getRegistry().register(new DisplayScreenBlock(multipartProps).setRegistryName("display_screen"));
        event.getRegistry().register(new LargeDisplayScreenBlock(multipartProps).setRegistryName("large_display_screen"));

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
        event.getRegistry().register(new MultipartBlockItem(PL3Blocks.READER_BLOCK, props).setRegistryName("reader"));
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