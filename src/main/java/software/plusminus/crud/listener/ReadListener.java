package software.plusminus.crud.listener;

import software.plusminus.listener.Listener;

public interface ReadListener<T> extends Listener<T> {

    default void onSingleRead(T object) {
        onRead(object);
    }

    void onRead(T object);

}
