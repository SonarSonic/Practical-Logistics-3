/*
package sonar.logistics.multiparts.displays.old.gsi.interaction.actions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import sonar.logistics.PL3;
import sonar.logistics.multiparts.displays.old.gsi.interaction.DisplayScreenClick;
import sonar.logistics.utils.network.EnumSyncType;

import java.net.URI;

public class ClickHyperlink implements IDisplayAction {
	
	public String hyperlink = "";
	
	public ClickHyperlink(){}
	
	public ClickHyperlink(String hyperlink){
		this.hyperlink = hyperlink;
	}
	
	@Override
	public CompoundNBT read(CompoundNBT tag, EnumSyncType syncType){
		hyperlink = tag.getString("hyperlink");
		return tag;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag, EnumSyncType syncType){
		tag.putString("hyperlink", hyperlink);
		return tag;
	}

	@Override
	public int doAction(DisplayScreenClick click, PlayerEntity player, double subClickX, double subClickY) {
        try
        {	URI uri = new URI(hyperlink);
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke(null);
            oclass.getMethod("browse", URI.class).invoke(object, uri);

			player.sendMessage(new StringTextComponent("Opened: " + hyperlink));
        }
        catch (Throwable throwable1)
        {
            Throwable throwable = throwable1.getCause();
            PL3.LOGGER.error("Couldn't open link: {}", throwable == null ? "<UNKNOWN>" : throwable.getMessage());
            
        }
		return -1;
	}

	public static final String REGISTRY_NAME = "hyperlink_action";

	@Override
	public String getRegisteredName() {
		return REGISTRY_NAME;
	}

}
*/
