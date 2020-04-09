package sonar.logistics.server.data.api.methods.types;

import net.minecraft.tileentity.TileEntity;
import sonar.logistics.server.data.api.IEnvironment;

public interface IMethodTileEntity<O, T extends TileEntity> {

    O invoke(IEnvironment environment, T tile);

}
