package works.hop.wait.notify;

import org.rapidoid.log.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Waiter implements Runnable {

    private Restaurant.Orders orders;

    public Waiter(Restaurant.Orders orders) {
        this.orders = orders;
    }

    public void produce(Order order) {
        System.out.println("producing order for " + order.getFood());
        orders.put(order);
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println("Thread " + name + " has started");

        String[] foods = {
                "sushi",
                "burger",
                "toast",
                "fries",
                "steak",
                "End"
        };

        for (String food : foods) {
            // Thread.sleep() to mimic heavy server-side processing
            try {
                produce(new Order(food));
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
                System.out.println("awoken from sleep");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.error("Thread interrupted", e);
            }
        }
        System.out.println(Thread.currentThread().getName() + " has completed");
    }
}
