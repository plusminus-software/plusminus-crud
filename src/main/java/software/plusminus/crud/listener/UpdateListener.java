package software.plusminus.crud.listener;

public interface UpdateListener<T> extends AbstractWriteAdvice<T> {

    void onUpdate(T object);

    void onPatch(T object);

}
