package software.plusminus.crud.listener;

import software.plusminus.crud.CrudAction;

public interface CrudListener<T> extends ReadListener<T>, WriteListener<T> {

    @Override
    default void onWrite(T object, CrudAction action) {
        onAction(object, action);
    }

    @Override
    default void onRead(T object) {
        onAction(object, CrudAction.READ);
    }

    void onAction(T object, CrudAction action);
}
