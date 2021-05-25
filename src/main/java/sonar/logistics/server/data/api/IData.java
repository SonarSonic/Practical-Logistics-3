package sonar.logistics.server.data.api;

public interface IData {

    default void preUpdate(){}

    default void postUpdate(){}

    /**called after the packet is sent*/
    default void onUpdated(){}

    default boolean hasUpdated(){
        return true;
    }

    String toString();

}