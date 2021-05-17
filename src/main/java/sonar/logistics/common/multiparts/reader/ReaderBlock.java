package sonar.logistics.common.multiparts.reader;

import sonar.logistics.common.multiparts.base.MultipartEntry;
import sonar.logistics.common.multiparts.base.MultipartTile;
import sonar.logistics.common.multiparts.base.OrientatedMultipart;
import sonar.logistics.util.PL3Shapes;

public class ReaderBlock extends OrientatedMultipart {

    public ReaderBlock(Properties props) {
        super(props, PL3Shapes.READER_ROTATED_VOXELS);
    }

    @Override
    public boolean hasMultipartTile(MultipartEntry entry){
        return true;
    }

    @Override
    public MultipartTile createMultipartTile(MultipartEntry entry){
        return new ReaderTile(entry);
    }

}
