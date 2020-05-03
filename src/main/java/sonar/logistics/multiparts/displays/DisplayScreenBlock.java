package sonar.logistics.multiparts.displays;

import sonar.logistics.multiparts.base.OrientatedMultipart;
import sonar.logistics.multiparts.base.IMultipartBlock;
import sonar.logistics.multiparts.base.MultipartEntry;
import sonar.logistics.multiparts.base.MultipartTile;
import sonar.logistics.utils.PL3Shapes;

public class DisplayScreenBlock extends AbstractScreenBlock implements IMultipartBlock {

    public DisplayScreenBlock(Properties props) {
        super(props, PL3Shapes.DISPLAY_ROTATED_VOXELS);
    }

    @Override
    public boolean hasMultipartTile(MultipartEntry entry){
        return true;
    }

    @Override
    public MultipartTile createMultipartTile(MultipartEntry entry){
        return new DisplayScreenTile(entry, PL3Shapes.DISPLAY_SCREEN_SCALING);
    }

}
