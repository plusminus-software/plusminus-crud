package software.plusminus.crud.fixtures;

import lombok.Getter;
import org.springframework.stereotype.Component;
import software.plusminus.crud.CrudAction;
import software.plusminus.crud.listener.WriteListener;

@Component
public class TestWriteListener implements WriteListener<TestEntity> {

    @Getter
    private CrudAction lastAction;

    @Override
    public void onWrite(TestEntity object, CrudAction action) {
        this.lastAction = action;
    }

    public void reset() {
        lastAction = null;
    }
}
