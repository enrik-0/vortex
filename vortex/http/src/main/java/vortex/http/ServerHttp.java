package vortex.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpServer;

public final class ServerHttp {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerHttp.class);

	private static HttpServer server;
	
	private ServerHttp() {
		
	}
	public static void runServer(int port) throws IOException {
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
