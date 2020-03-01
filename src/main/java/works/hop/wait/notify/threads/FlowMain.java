package works.hop.wait.notify.threads;

public class FlowMain {

    public static void main(String[] args) {
        FlowSignal<FlowHandler.Signal<String>, FlowHandler.Signal<String>> signal = new FlowSignal<>();
        FlowHandler<FlowHandler.Signal<String>> src = new FlowSrc(signal);
        FlowHandler<FlowHandler.Signal<String>> dest = new FlowDest(signal);

        Thread source = new Thread((Runnable)src, "source");
        Thread source2 = new Thread((Runnable)src, "source2");
        Thread client = new Thread((Runnable)dest, "client");

        source.start();
        source2.start();
        client.start();
    }
}
