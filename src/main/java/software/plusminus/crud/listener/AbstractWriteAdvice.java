package software.plusminus.crud.listener;

import software.plusminus.listener.Joinpoint;
import software.plusminus.listener.Listener;

public interface AbstractWriteAdvice<T> extends Listener<T> {

    @Override
    default Joinpoint joinpoint() {
        return CrudJoinpoint.BEFORE;
    }
}
