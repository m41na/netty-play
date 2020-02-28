package works.hop.db.queue;

import works.hop.db.entity.EventEntity;

import java.util.Observable;
import java.util.Observer;

public abstract class QuObserver implements Observer {

    public static EventEntity EXIT = new EventEntity(null, null, null);

    public abstract void receive(EventEntity event);

    public abstract void complete();

    @Override
    public void update(Observable obs, Object arg) {
        if (arg != null) {
            receive((EventEntity)arg);
        }
    }
}
