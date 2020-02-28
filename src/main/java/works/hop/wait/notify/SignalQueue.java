package works.hop.wait.notify;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SignalQueue<T> {

    private int max;
    private Queue<T> queue;
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public SignalQueue(int max) {
        this.max = max;
        this.queue = new LinkedList<>();
    }

    public void put(T t) {
        lock.lock();
        try {
            while (queue.size() == max) {
                notFull.await();
            }
            queue.add(t);
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            while (queue.size() == 0) {
                notEmpty.await();
            }
            T item = queue.remove();
            notFull.signalAll();
            return item;
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
            return null;
        } finally {
            lock.lock();
        }
    }

    public int size() {
        return queue.size();
    }
}
