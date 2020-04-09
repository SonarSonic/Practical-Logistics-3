package sonar.logistics.server.data.api.methods.types;

import net.minecraft.entity.Entity;
import sonar.logistics.server.data.api.IEnvironment;

public interface IMethodEntity<O, E extends Entity> {

    O invoke(IEnvironment environment, E tile);

}
