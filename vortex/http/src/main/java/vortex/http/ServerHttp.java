package vortex.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpServer;
import vortex.annotate.annotations.HttpMethod;
import vortex.http.exceptions.BodyException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class ServerHttp {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestManager.class);

	private static HttpServer server;
	public static void runServer(int puerto) throws IOException {
		server = HttpServer.create();
		server.bind(new InetSocketAddress(puerto), 0);
		server.createContext("/", new Handler());
		server.setExecutor(null);
		server.start();
	}

	static class Handler implements HttpHandler {
		@Override
		public void handle(HttpExchange request) throws IOException {
			// Obtener la ruta y el método de la solicitud
			String ruta = request.getRequestURI().getPath();
			String metodo = request.getRequestMethod();
			switch (HttpMethod.valueOf(metodo)) {
				case GET :
					RequestManager.getInstance().getHandle(request);
					break;
				case DELETE :
					break;
				case POST :
					RequestManager.getInstance().postHandle(request);
					break;
				case PUT :
					break;
			}
			request.sendResponseHeaders(200, 0);
			request.close();
			server.stop(0);
		}
	}
}
