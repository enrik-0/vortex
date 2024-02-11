package kik.framework.vortex.server;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public final class ServerHttp {

	private static byte DEFAULT_PORT = 80;


	private static HttpServer server;

	private ServerHttp() {

	}
	public static void runServer()
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
	}
	public static void runServer(int port)
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
	}

	/**
	 * Initialize the server at the port
	 * 
	 * @param port
	 *            default port 80
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void runServer(int port, Class<? extends HttpHandler> handler)
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		server = HttpServer.create();
		server.bind(new InetSocketAddress(port), 0);
		server.createContext("/", handler.getConstructor().newInstance());
		server.setExecutor(null);
		server.start();

	}
	public static void stopServer() {
		stopServer(10);
	}
	/**
	 * Close the socket and wait until all connections end or delay.
	 * 
	 * @param delay
	 *            maximum time to wait to stop the server default 10
	 */
	public static void stopServer(int delay) {
		server.stop(delay);

	}

}
