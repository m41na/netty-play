package works.hop.wait.notify;

import java.util.Scanner;

public class WaitNotifyBasic {

    public static void main(String[] args) throws InterruptedException {
        final Processor processor = new Processor();

        Thread th1 = new Thread(() -> {
            try {
                processor.produce();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        });

        Thread th2 = new Thread(() -> {
            try {
                processor.consume();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        });

        th1.start();
        th2.start();

        th1.join();
        th2.join();
    }

    static class Processor {

        public void produce() throws InterruptedException {
            synchronized (this) {
                System.out.println("producer thread running");
                wait(); //can only be called in a synchronized block
                System.out.println("producer thread resumed");
            }
        }

        public void consume() throws InterruptedException {
            Thread.sleep(2000);
            Scanner scanner = new Scanner(System.in);
            synchronized (this) {
                System.out.println("waiting for return key");
                scanner.nextLine();
                System.out.println("return key pressed");
                notifyAll(); //can only be called in a synchronized block
            }
        }
    }
}
