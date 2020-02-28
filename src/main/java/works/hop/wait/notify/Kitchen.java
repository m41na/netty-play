package works.hop.wait.notify;

public class Kitchen implements Runnable {

    private Restaurant.Orders orders;

    public Kitchen(Restaurant.Orders orders) {
        this.orders = orders;
    }

    public void receive(Order order) {
        System.out.println("producing order for " + order.getFood());
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println("Thread " + name + " has started");
        while(true){
            Order order = orders.take();
            System.out.println("Received order for " + order.getFood() + ". Queue size is now " + orders.size());
            if("End".equals(order.getFood())){
                break;
            }
        }
    }

    public void accept(String food) {
        orders.put(new Order(food));
    }
}
