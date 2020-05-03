package sonar.logistics.server.cables;

public interface ICableCache<C extends ICableCache> {

    void deleteCache();

    void shrinkCache();

    void mergeCache(C merging);

    void changeCacheID(int globalID);

}
