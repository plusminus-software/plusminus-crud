package software.plusminus.crud.listener;

import software.plusminus.listener.Listener;

public interface ReadListener<T> extends Listener<T> {

    void onRead(T object);

}
