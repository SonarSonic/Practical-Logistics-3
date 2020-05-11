package sonar.logistics.common.blocks.hammer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import sonar.logistics.common.blocks.PL3Blocks;
import sonar.logistics.common.blocks.base.SyncableTile;
import sonar.logistics.util.network.EnumSyncType;
import sonar.logistics.util.recipes.SimpleRecipeV1;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForgingHammerTile extends SyncableTile implements ITickableTileEntity, INamedContainerProvider {

    public static int processSpeed = 100;
    public static int coolDownSpeed = 100;

    public ForgingHammerInventory inventory = new ForgingHammerInventory(this);

    public boolean invChanged = true, canProcess = false;
    public int progress, coolDown;

    public ForgingHammerTile() {
        super(PL3Blocks.FORGING_HAMMER_TILE);
    }

    public void tick() {
        if(world.isRemote){
            return;
        }
        int prevProgress = progress;
        int prevCoolDown = coolDown;
        if(invChanged){
            invChanged = false;
            boolean simulate = doProcess(true);
            if(simulate != canProcess) {
                canProcess = simulate;
            }
        }
         if(coolDown > 0){
            if(coolDown >= coolDownSpeed){
                coolDown = 0;
                spawnParticles(new RedstoneParticleData(28 / 100F, 96 / 100F, 115 / 100F, 1));
            }else{
                coolDown++;
            }
        }else if(canProcess){
            if(progress >= processSpeed){
                coolDown = 1;
                progress = 0;
                doProcess(false);
            }else {
                progress += 1;
            }
        }else if(progress != 0){
            progress-=1;
        }
        if(prevProgress != progress || prevCoolDown != coolDown){
            queueMarkDirty();
            queueSyncPacket();
        }
        flushQueues();
    }

    public void spawnParticles(IParticleData data){
        if(world.isRemote) {
            world.addParticle(data, getPos().getX() + 0.35, getPos().getY() + 0.9, getPos().getZ() + 0.35, 0.0D, 0.0D, 0.0D);
            world.addParticle(data, getPos().getX() + 1 - 0.35, getPos().getY() + 0.9, getPos().getZ() + 0.35, 0.0D, 0.0D, 0.0D);
            world.addParticle(data, getPos().getX() + 0.35, getPos().getY() + 0.9, getPos().getZ() + 1 - 0.35, 0.0D, 0.0D, 0.0D);
            world.addParticle(data, getPos().getX() + 1 - 0.35, getPos().getY() + 0.9, getPos().getZ() +  1 - 0.35, 0.0D, 0.0D, 0.0D);

        }
    }

    public boolean doProcess(boolean simulate) {
        if(inventory.getStackInSlot(0).isEmpty()){
            return false;
        }
        SimpleRecipeV1 recipe = ForgingHammerRecipes.INSTANCE.getMatchingRecipe(inventory.getStackInSlot(0));
        if (recipe == null) {
            return false;
        }
        if(!simulate){
            inventory.getStackInSlot(0).shrink(1);
            inventory.onContentsChanged(0);
        }
        return inventory.insertItemInternal(1, recipe.getOutput(0), simulate).isEmpty();
    }

    @Override
    public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
        CompoundNBT invTag = tag.getCompound("inv");
        ((INBTSerializable<CompoundNBT>) inventory).deserializeNBT(invTag);
        progress = tag.getInt("progress");
        coolDown = tag.getInt("coolDown");
        return super.read(tag, syncType);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
        CompoundNBT invTag = ((INBTSerializable<CompoundNBT>) inventory).serializeNBT();
        tag.put("inv", invTag);
        tag.putInt("progress", progress);
        tag.putInt("coolDown", coolDown);
        return super.write(tag, syncType);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> inventory).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ForgingHammerContainer(i, playerInventory, getPos(), getWorld());
    }
}
