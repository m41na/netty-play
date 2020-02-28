package works.hop.web.server;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class TheSun {

    static final int port = 8090;

    public static void main(String[] args) {
        AtomicLong count = new AtomicLong(0);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/", httpExchange ->
            {
                CompletableFuture.supplyAsync(() -> "Hello, World!".getBytes(StandardCharsets.UTF_8)).handle((res, th) -> {
                    if (th != null) {
                        try {
                            byte[] response = th.getMessage().getBytes(StandardCharsets.UTF_8);
                            httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                            httpExchange.sendResponseHeaders(500, response.length);

                            OutputStream out = httpExchange.getResponseBody();
                            out.write(response);
                            out.close();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                            httpExchange.sendResponseHeaders(200, res.length);

                            OutputStream out = httpExchange.getResponseBody();
                            out.write(res);
                            out.close();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return true;
                }).thenAccept(done -> count.incrementAndGet());
            });

            server.start();
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }
}
