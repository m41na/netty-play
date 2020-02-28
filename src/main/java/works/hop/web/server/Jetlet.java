package works.hop.web.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Jetlet {

    public static Server createServer(int port) {
        Server server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(HelloServlet.class, "/*");

        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = 8090;
        Server server = createServer(port);

        server.start();

        server.join();
    }

    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            final AsyncContext async = request.startAsync();
            async.start(() -> {
                try {
                    response.getWriter().println("Hello World!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                async.complete();
            });
        }
    }
}
