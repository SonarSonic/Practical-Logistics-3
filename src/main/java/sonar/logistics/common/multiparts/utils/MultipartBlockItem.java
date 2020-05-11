package sonar.logistics.common.multiparts.utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import sonar.logistics.PL3;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.blocks.host.MultipartHostHelper;
import sonar.logistics.common.blocks.host.MultipartHostTile;
import sonar.logistics.common.blocks.host.NetworkedHostTile;
import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.cable.DataCableBlock;

public class MultipartBlockItem extends BlockItem {

    IMultipartBlock multipart;

    public MultipartBlockItem(IMultipartBlock multipart, Properties props) {
        super(multipart.getDefaultBlock(), props);
        this.multipart = multipart;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext itemContext) {
        if(itemContext.getWorld().isRemote){
            return ActionResultType.SUCCESS;
        }
        MultipartItemUseContext context = new MultipartItemUseContext(itemContext);
        ActionResultType internal = tryPlacement(context.useInternal(true));
        if(internal.isSuccessOrConsume()){
            return internal;
        }
        return tryPlacement(context.useInternal(false));
    }

    public ActionResultType tryPlacement(MultipartItemUseContext context){
        MultipartHostTile tile = MultipartHostHelper.getMultipartHostTile(context.getWorld(), context.getPos());
        BlockState state = multipart.getDefaultBlock().getStateForPlacement(context);
        EnumMultipartSlot slot = multipart.getMultipartSlotFromState(state);
        if(tile == null){
            ActionResultType convert = convertCableToMultipart(context);
            if(!convert.isSuccessOrConsume()) {
                ActionResultType result = super.onItemUse(context);
                if (!result.isSuccessOrConsume()) {
                    return result;
                }
                if (!multipart.requiresMultipartHost()) { //for cables a block is placed so no need for a multipart.
                    return ActionResultType.SUCCESS;
                }
            }
            tile = MultipartHostHelper.getMultipartHostTile(context.getWorld(), context.getPos());
        }
        if(tile != null && !tile.isSlotTaken(slot) && tile.doAddMultipart(new MultipartEntry(tile, multipart, slot))){
            context.getItem().shrink(1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;

    }

    @Override
    protected BlockState getStateForPlacement(BlockItemUseContext context) {
        if(!multipart.requiresMultipartHost()){
            return multipart.getDefaultBlock().getStateForPlacement(context);
        }
        return PL3Blocks.MULTIPART_HOST_BLOCK.getDefaultState();
    }

    public ActionResultType convertCableToMultipart(MultipartItemUseContext context){
        if(multipart == PL3Blocks.DATA_CABLE){
            return ActionResultType.FAIL;
        }
        BlockState currentState = context.getWorld().getBlockState(context.getPos());
        if(currentState.getBlock() instanceof DataCableBlock){
            context.getWorld().setBlockState(context.getPos(), PL3Blocks.MULTIPART_HOST_BLOCK.getDefaultState(), 3);
            NetworkedHostTile tile = MultipartHostHelper.getNetworkedHostTile(context.getWorld(), context.getPos());
            if(tile != null) {
                tile.doAddMultipart(new MultipartEntry(tile, PL3Blocks.DATA_CABLE, EnumMultipartSlot.CENTRE));
                tile.connectNetwork(); //TileEntity.onload() was probably already called, and this cable, with already have been added to the net if we're converting.
                return ActionResultType.SUCCESS;
            }else{
                PL3.LOGGER.error("Failed to get MultipartHostTile when converting cable to multipart! This is a bug!");
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

}
