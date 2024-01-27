package vortex.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpServer;

import vortex.annotate.manager.AnnotationManager;

public final class ServerHttp {

	private static byte DEFAULT_PORT = 80;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerHttp.class);

	private static HttpServer server;
	
	private ServerHttp() {
		
	}
	public static void runServer() throws IOException {
		runServer(DEFAULT_PORT);
	}
	public static void runServer(int port) throws IOException {
		AnnotationManager.getInstance();
		server = HttpServer.create();
		server.bind(new InetSocketAddress(port), 0);
		server.createContext("/", new Handler(server));
		server.setExecutor(null);
		server.start();
	}
	public static void stopServer() {
		stopServer(0);
	}
	public static void stopServer(int delay){
		server.stop(delay);
		
	}

}
