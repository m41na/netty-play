package works.hop.db.queue;

import works.hop.db.entity.EventEntity;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;

public class QuConsumer extends Observable implements Runnable {

    private final BlockingQueue<EventEntity> queue;

    public QuConsumer(BlockingQueue<EventEntity> q) {
        this.queue = q;
    }

    public void accept(EventEntity event) {
        setChanged();
        notifyObservers(event);
    }

    @Override
    public void run() {
        while (true) {
            try {
                EventEntity input = queue.take();
                if (input.body.equals("!q")) {
                    accept(QuObserver.EXIT);
                    break;
                }
                accept(input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
