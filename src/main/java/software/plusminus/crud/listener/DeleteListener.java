package software.plusminus.crud.listener;

public interface DeleteListener<T> extends AbstractWriteAdvice<T> {

    void onDelete(T object);

}
