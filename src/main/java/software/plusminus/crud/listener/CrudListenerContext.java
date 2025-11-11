package software.plusminus.crud.listener;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.plusminus.listener.Joinpoint;
import software.plusminus.listener.ListenerContext;

@AllArgsConstructor
@Service
public class CrudListenerContext {

    private ListenerContext listenerContext;

    public <T> void onRead(T object) {
        listenerContext.listen(object, ReadListener.class, ReadListener::onRead,
                Joinpoint.DEFAULT, CrudJoinpoint.BEFORE, CrudJoinpoint.AFTER);
    }

    public <T> void beforeCreate(T object) {
        listenerContext.listen(object, CreateListener.class, CreateListener::onCreate, CrudJoinpoint.BEFORE);
    }

    public <T> void afterCreate(T object) {
        listenerContext.listen(object, CreateListener.class, CreateListener::onCreate, CrudJoinpoint.AFTER);
    }

    public <T> void beforeUpdate(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onUpdate, CrudJoinpoint.BEFORE);
    }

    public <T> void afterUpdate(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onUpdate, CrudJoinpoint.AFTER);
    }

    public <T> void beforePatch(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onPatch, CrudJoinpoint.BEFORE);
    }

    public <T> void afterPatch(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onPatch, CrudJoinpoint.AFTER);
    }

    public <T> void beforeDelete(T object) {
        listenerContext.listen(object, DeleteListener.class, DeleteListener::onDelete, CrudJoinpoint.BEFORE);
    }

    public <T> void afterDelete(T object) {
        listenerContext.listen(object, DeleteListener.class, DeleteListener::onDelete, CrudJoinpoint.AFTER);
    }
}
