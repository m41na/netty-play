package works.hop.web.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Jetsy {

    public static Server createServer(int port)
    {
        Server server = new Server(port);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                httpServletResponse.setContentType("text/plain; charset=utf-8");
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);

                PrintWriter out = httpServletResponse.getWriter();
                out.println("Hello World!");
            }
        });
        return server;
    }

    public static void main(String[] args) throws Exception
    {
        int port = 8090;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
