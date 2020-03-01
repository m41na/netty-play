package works.hop.wait.notify.threads;

public interface FlowHandler<T extends FlowHandler.Signal> {

    void onSignal(T signal);

    interface Signal<T> {
        String getKey();
        T getBody();
    }

    default <R>Signal<R> newSignal(String key, R body){
        return new Signal<R>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public R getBody() {
                return body;
            }
        };
    }
}
