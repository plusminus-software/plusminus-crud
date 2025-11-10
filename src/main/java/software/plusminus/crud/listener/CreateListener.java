package software.plusminus.crud.listener;

public interface CreateListener<T> extends AbstractWriteAdvice<T> {

    void onCreate(T object);

}
