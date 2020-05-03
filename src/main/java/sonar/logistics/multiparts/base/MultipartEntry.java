package sonar.logistics.multiparts.base;

import net.minecraft.block.BlockState;
import sonar.logistics.PL3;
import sonar.logistics.blocks.host.MultipartHostTile;
import sonar.logistics.multiparts.utils.EnumMultipartSlot;

import javax.annotation.Nullable;

/**used within a MultipartHostTile to store the instance of the Multipart + Multipart Tile + Multipart Renderer etc.*/
public class MultipartEntry {

    public final MultipartHostTile host;
    public final IMultipartBlock multipart;
    public final EnumMultipartSlot slot;

    private final MultipartTile tile;
    public final IMultipartRenderer<MultipartTile> renderer;
    private BlockState state;

    public MultipartEntry(MultipartHostTile host, IMultipartBlock multipart, EnumMultipartSlot slot){
        this.host = host;
        this.multipart = multipart;
        this.slot = slot;

        this.tile = hasMultipartTile() ? multipart.createMultipartTile(this) : null;
        this.renderer = PL3.proxy.getMultipartRenderer(tile);


    }

    public void updateRenderState(){
        this.state = multipart.getMultipartRenderState(host, this);
    }

    public void onPlaced(){
        multipart.onPlaced(host.getWorld(), getBlockState(), host.getPos());
    }

    public void onDestroyed(){
        multipart.onDestroyed(host.getWorld(), getBlockState(), host.getPos());
    }

    public void onMultipartAdded(MultipartEntry added){
        multipart.onMultipartAdded(host, this, added);
    }

    public void onMultipartRemoved(MultipartEntry removed){
        multipart.onMultipartRemoved(host, this, removed);
    }


    //// HELPER METHODS \\\\

    public MultipartHostTile getHost(){
        return host;
    }

    public BlockState getBlockState(){
        if(state == null){
            state = multipart.getMultipartStateFromSlot(slot);
        }
        return state;
    }



    //// MULTIPART TILE \\\\

    public boolean hasMultipartTile(){
        return multipart.hasMultipartTile(this);
    }

    @Nullable
    public MultipartTile getMultipartTile(){
        return tile;
    }



    //// NBT LOADING CHECKER \\\\\

    private boolean wasNBTLoaded;

    public void setWasNBTLoaded(boolean wasNBTLoaded){
        this.wasNBTLoaded = wasNBTLoaded;
    }

    public boolean wasNBTLoaded(){
        return wasNBTLoaded;
    }
}
