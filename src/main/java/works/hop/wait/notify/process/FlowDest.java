package works.hop.wait.notify.process;

public class FlowDest implements Runnable, FlowHandler<FlowHandler.Signal<char[]>> {

    private final FlowSignal<Signal<char[]>, Signal<char[]>> signals;

    public FlowDest(FlowSignal signals) {
        this.signals = signals;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println("Thread '" + name + "' has started");

        Signal event;
        while ((event = signals.accept()) != null) {
            //System.out.println("new event accepted");
            onSignal(event);
            //System.out.println("new event handled");
        }
        System.out.println(Thread.currentThread().getName() + " has completed");
    }

    @Override
    public void onSignal(Signal<char[]> signal) {
        //do some work
        String resp = new String(signal.getBody()).toUpperCase();
        signals.reply(newSignal(signal.getKey(), resp.toCharArray()));
    }
}
