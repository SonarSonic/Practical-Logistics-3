/*
package sonar.logistics.multiparts.displays.old.info.elements;


import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@ASMDisplayElement(id = ItemStackElement.REGISTRY_NAME, modid = PL2Constants.MODID)
public class ItemStackElement extends AbstractDisplayElement {

	public StoredItemStack stack;

	public ItemStackElement(StoredItemStack stack) {
		super();
		this.stack = stack;
	}

	public void render() {
		scale(1, 1 , 0.1); //compresses the items on the z axis
		GL11.glRotated(180, 0, 1, 0); // flips the items
		GL11.glScaled(-1, 1, 1);
		RenderHelper.renderItemIntoGUI(getItem(), 0, 0);
		GlStateManager.translate(0, 0, 2);
		GlStateManager.depthMask(false);
		RenderHelper.renderStoredItemStackOverlay(getItem(), 0, 0, 0, "" + stack.stored, false);
		GlStateManager.depthMask(true);
	}

	@Override
	public Object getClientEditGui(TileAbstractDisplay obj, Object origin, World world, EntityPlayer player) {
		//FIXME
		return null;
	}
	
	public StoredItemStack getStoredItem() {
		return stack;
	}

	public StoredItemStack setStoredItem(StoredItemStack item) {
		return stack = item;
	}

	public ItemStack getItem() {
		return stack.getItemStack();
	}

	public ItemStack setItem(ItemStack item) {
		return stack.item = item;
	}

	public long getCount() {
		return stack.stored;
	}

	public long setCount(long stored) {
		return stack.stored = stored;
	}

	@Override
	public String getRepresentiveString() {
		return getItem().getDisplayName() + " - " + getCount();
	}

	@Override
	public int[] createUnscaledWidthHeight() {
		return new int[] { 16, 16 };
	}

	@Override
	public void readData(NBTTagCompound nbt, SyncType type) {
		super.readData(nbt, type);
		stack = new StoredItemStack();
		stack.readData(nbt, type);		
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt, SyncType type) {
		super.writeData(nbt, type);
		stack.writeData(nbt, type);
		return nbt;		
	}

	public static final String REGISTRY_NAME = "s_item";

	@Override
	public String getRegisteredName() {
		return REGISTRY_NAME;
	}

}
*/
