package works.hop.wait.notify.threads;

public class FlowDest implements Runnable, FlowHandler<FlowHandler.Signal<String>> {

    private final FlowSignal<FlowHandler.Signal, FlowHandler.Signal> signals;

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
    public void onSignal(Signal<String> signal) {
        //do some work
        String resp = signal.getBody().toUpperCase();
        signals.reply(newSignal(signal.getKey(), resp));
    }
}
