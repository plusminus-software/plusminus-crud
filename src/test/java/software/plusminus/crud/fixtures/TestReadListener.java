package software.plusminus.crud.fixtures;

import lombok.Getter;
import org.springframework.stereotype.Component;
import software.plusminus.crud.listener.ReadListener;

@Component
public class TestReadListener implements ReadListener<TestEntity> {

    @Getter
    private boolean triggered;

    @Override
    public void onRead(TestEntity object) {
        this.triggered = true;
    }

    public void reset() {
        triggered = false;
    }
}
