package sonar.logistics.server.data.types.changes;

public interface IChangeable {

    void preUpdate();

    void postUpdate();

    EnumChangeable getChange();
}
