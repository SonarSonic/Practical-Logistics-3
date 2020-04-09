package sonar.logistics.server.data.api.methods.types;

import net.minecraft.block.Block;
import sonar.logistics.server.data.api.IEnvironment;

public interface IMethodBlock<O, B extends Block> {

    O invoke(IEnvironment environment, B block);

}
