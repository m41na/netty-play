package works.hop.wait.notify;

import java.util.LinkedList;
import java.util.Queue;

public class NotifyQueue<T> {

    private int max;
    private Queue<T> queue;
    private Object lock = new Object();

    public NotifyQueue(int max) {
        this.max = max;
        this.queue = new LinkedList<>();
    }

    public void put(T t) {
        synchronized(lock) {
            try {
                while (queue.size() == max) {
                    lock.wait();
                }
                queue.add(t);
                lock.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public T take() {
        synchronized (lock) {
            try {
                while (queue.size() == 0) {
                    lock.wait();
                }
                T item = queue.remove();
                lock.notifyAll();
                return item;
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
                return null;
            }
        }
    }

    public int size() {
        return queue.size();
    }
}
