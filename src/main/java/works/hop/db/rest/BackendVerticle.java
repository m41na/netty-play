package works.hop.db.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class BackendVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> promise) {
        promise.complete();
    }

    private Future<Void> prepareDatabase() {
        Promise<Void> promise = Promise.promise();
        // (...)
        return promise.future();
    }
    private Future<Void> startHttpServer() {
        Promise<Void> promise = Promise.promise();
        // (...)
        return promise.future();
    }
}
