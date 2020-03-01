package works.hop.wait.notify.threads;

import works.hop.wait.notify.SignalQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FlowSignal<REQUEST extends FlowHandler.Signal, RESPONSE extends FlowHandler.Signal> {

    private final Map<String, Consumer<RESPONSE>> callbacks = new HashMap<>();
    private final SignalQueue<REQUEST> requests = new SignalQueue<>(10);

    public void dispatch(FlowHandler handler, String key, REQUEST request){
        callbacks.put(key, response -> handler.onSignal(response));
        requests.put(request);
    }

    public void reply(RESPONSE response){
        callbacks.get(response.getKey()).accept(response);
    }

    public REQUEST accept(){
        return requests.take();
    }
}
