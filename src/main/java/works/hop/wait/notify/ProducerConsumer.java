package works.hop.wait.notify;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {

    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    public static void producer() throws InterruptedException {
        Random rand = new Random();
        while (true) {
            queue.put(rand.nextInt(100));
        }
    }

    public static void consumer() throws InterruptedException {
        Random rand = new Random();
        while (true) {
            Thread.sleep(100);
            if (rand.nextInt(10) % 2 == 0) {
                Integer val = queue.take();
                System.out.println("Taken value " + val + ". Queue size is now " + queue.size());
            }
        }
    }
}
