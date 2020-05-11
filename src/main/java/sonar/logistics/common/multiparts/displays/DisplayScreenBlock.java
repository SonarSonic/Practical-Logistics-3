package sonar.logistics.common.multiparts.displays;

import sonar.logistics.common.multiparts.base.IMultipartBlock;
import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.util.PL3Shapes;

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
