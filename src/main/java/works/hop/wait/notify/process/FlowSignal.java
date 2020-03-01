package works.hop.wait.notify.process;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FlowSignal<REQUEST extends FlowHandler.Signal, RESPONSE extends FlowHandler.Signal> {

    private final Map<String, Consumer<RESPONSE>> callbacks = new HashMap<>();
    private final FileQueue requests = new FileQueue(1);
    private final ObjectMapper mapper = new ObjectMapper();

    public FlowSignal() throws IOException {
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    }

    public void dispatch(FlowHandler handler, String key, REQUEST request) {
        callbacks.put(key, response -> handler.onSignal(response));
        requests.put(request.getBody().toString().toCharArray());
    }

    public void reply(RESPONSE response) {
        callbacks.get(response.getKey()).accept(response);
    }

    public REQUEST accept() {
        try (CharArrayReader reader = new CharArrayReader(requests.take())) {
            return (REQUEST) mapper.readValue(reader, SignalImpl.class);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    private static class SignalImpl implements FlowHandler.Signal<char[]> {

        private final String key;
        private final char[] body;

        @JsonCreator
        public SignalImpl(@JsonProperty("key") String key, @JsonProperty("body") char[] body) {
            this.key = key;
            this.body = body;
        }

        @Override
        public String getKey() {
            return null;
        }

        @Override
        public char[] getBody() {
            return null;
        }
    }
}
