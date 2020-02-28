package works.hop.db.queue;

import works.hop.db.entity.EventEntity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QuApp {

    private static QuApp instance = null;
    private final BlockingQueue<EventEntity> eventsQueue;
    private final QuProducer producer;
    private final QuConsumer consumer;

    private QuApp() {
        this.eventsQueue = new LinkedBlockingQueue<>();
        this.producer = new QuProducer(eventsQueue);
        this.consumer = new QuConsumer(eventsQueue);

        //Use a thread to run the consumer so that the eventsQueue queue may block in that thread, and not the main thread
        new Thread(consumer).start();
        System.out.println("consumer thread has started");
    }

    public void produce(EventEntity event){
        this.producer.produce(event);
    }

    public void subscribe(QuObserver observer) {
        consumer.addObserver(observer);
    }

    public void unsubscribe(QuObserver observer){
        consumer.deleteObserver(observer);
    }

    public QuApp getInstance() {
        if (instance == null) {
            instance = new QuApp();
        }
        return instance;
    }
}
