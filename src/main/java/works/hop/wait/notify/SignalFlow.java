package works.hop.wait.notify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SignalFlow<REQUEST, RESPONSE> {

    private final String name;
    private final Map<String, CompletableFuture<RESPONSE>> callbacks = new HashMap<>();
    private final SignalQueue<REQUEST> requests = new SignalQueue<>(10);
    private final SignalQueue<RESPONSE> results = new SignalQueue<>(10);

    public SignalFlow(String name) {
        this.name = name;
    }
}
