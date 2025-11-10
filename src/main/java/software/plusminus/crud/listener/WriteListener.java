package software.plusminus.crud.listener;

import software.plusminus.crud.CrudAction;

public interface WriteListener<T> extends CreateListener<T>, UpdateListener<T>, DeleteListener<T> {

    @Override
    default void onCreate(T object) {
        onWrite(object, CrudAction.CREATE);
    }

    @Override
    default void onUpdate(T object) {
        onWrite(object, CrudAction.UPDATE);
    }

    @Override
    default void onPatch(T object) {
        onWrite(object, CrudAction.PATCH);
    }

    @Override
    default void onDelete(T object) {
        onWrite(object, CrudAction.DELETE);
    }

    void onWrite(T object, CrudAction action);

}
