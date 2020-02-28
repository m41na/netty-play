package works.hop.db.queue;

import works.hop.db.entity.EventEntity;

import java.util.concurrent.BlockingQueue;

public class QuProducer {

    private final BlockingQueue<EventEntity> queue;

    public QuProducer(BlockingQueue<EventEntity> queue) {
        this.queue = queue;
    }

    public void produce(EventEntity event) {
        queue.add(event);
    }
}
