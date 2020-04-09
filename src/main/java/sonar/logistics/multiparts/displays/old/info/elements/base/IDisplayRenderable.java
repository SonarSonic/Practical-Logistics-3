package sonar.logistics.multiparts.displays.old.info.elements.base;

import net.minecraft.util.math.Vec3d;

public interface IDisplayRenderable {

	/**updates any render properties, before render() is called*/
	void updateRender();

	/**renders the object*/
	void render();

	/** set the maximum scaling for the object*/
	Vec3d setMaxScaling(double[] scaling);

}
