package works.hop.wait.notify.process;

import java.io.IOException;

public class FlowMain {

    public static void main(String[] args) throws IOException {
        FlowSignal<FlowHandler.Signal<char[]>, FlowHandler.Signal<char[]>> signal = new FlowSignal<>();
        FlowHandler<FlowHandler.Signal<char[]>> dest = new FlowDest(signal);
        FlowHandler<FlowHandler.Signal<char[]>> src = new FlowSrc(signal);

        Thread source = new Thread((Runnable) src, "source");
        //Thread source2 = new Thread((Runnable)src, "source2");
        Thread client = new Thread((Runnable) dest, "client");

        source.start();
        //source2.start();
        client.start();
    }
}
