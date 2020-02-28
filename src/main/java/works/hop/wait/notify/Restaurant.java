package works.hop.wait.notify;

public class Restaurant {

    private Orders orders = new Orders(10);

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        Kitchen kitchen = new Kitchen(restaurant.orders);
        new Thread(kitchen, "kitchen").start();

        Waiter waiter1 = new Waiter(restaurant.orders);
        new Thread(waiter1, "waiter1").start();

        Waiter waiter2 = new Waiter(restaurant.orders);
        new Thread(waiter2, "waiter2").start();
    }

    static class Orders extends NotifyQueue<Order> { //could also extend SignalQueue

        public Orders(int max) {
            super(max);
        }
    }
}
