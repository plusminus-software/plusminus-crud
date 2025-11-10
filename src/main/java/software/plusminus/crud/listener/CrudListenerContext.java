package software.plusminus.crud.listener;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.plusminus.listener.Joinpoint;
import software.plusminus.listener.ListenerContext;

@AllArgsConstructor
@Service
public class CrudListenerContext {

    private ListenerContext listenerContext;

    public <T> void afterRead(T object) {
        listenerContext.listen(object, ReadListener.class, ReadListener::onRead, Joinpoint.DEFAULT);
    }

    public <T> void beforeCreate(T object) {
        listenerContext.listen(object, CreateListener.class, CreateListener::onCreate, WriteJoinpoint.BEFORE);
    }

    public <T> void afterCreate(T object) {
        listenerContext.listen(object, CreateListener.class, CreateListener::onCreate, WriteJoinpoint.AFTER);
    }

    public <T> void beforeUpdate(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onUpdate, WriteJoinpoint.BEFORE);
    }

    public <T> void afterUpdate(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onUpdate, WriteJoinpoint.AFTER);
    }

    public <T> void beforePatch(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onPatch, WriteJoinpoint.BEFORE);
    }

    public <T> void afterPatch(T object) {
        listenerContext.listen(object, UpdateListener.class, UpdateListener::onPatch, WriteJoinpoint.AFTER);
    }

    public <T> void beforeDelete(T object) {
        listenerContext.listen(object, DeleteListener.class, DeleteListener::onDelete, WriteJoinpoint.BEFORE);
    }

    public <T> void afterDelete(T object) {
        listenerContext.listen(object, DeleteListener.class, DeleteListener::onDelete, WriteJoinpoint.AFTER);
    }
}
