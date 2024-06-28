package vortex.http;

import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URLClassLoader;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.PackageLoader;
import vortex.properties.kinds.Server;
import vortex.utils.MappingUtils;

public final class ServerHttp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHttp.class);
    private static final int DEFAULT_PORT = 80;

    private static HttpServer server;

    private ServerHttp() {

    }

    public static void runServer()
	    throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException {

	runServer(mapToPort());
    }

    public static void runServer(int port)
	    throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException {
	runServer(port, Handler.class);
    }

    /**
     * Initialize the server at the port
     * 
     * @param port default port 80
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws UriException
     * @throws InitiateServerException
     */
    public static void runServer(int port, Class<? extends HttpHandler> handler)
	    throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException {
	int threadNumber;
	AnnotationManager.getInstance();
	try {
	    threadNumber = (Integer) MappingUtils.map(Server.THREAD_NUMBER.value(), Integer.class);
	} catch (IllegalArgumentException | NullPointerException e) {
	    threadNumber = Runtime.getRuntime().availableProcessors();

	}
	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNumber);
	server = HttpServer.create();
	server.bind(new InetSocketAddress(port), 0);
	server.setExecutor(executor);
	server.createContext((String) Server.CONTEXT_PATH.value(), handler.getConstructor().newInstance());
	server.start();
    }

    public static void runServer(URLClassLoader loader) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, UriException, InitiateServerException {
	PackageLoader.getInstance().setLoader(loader);
	runServer(mapToPort());

    }

    public static void stopServer() {
	stopServer(10);
    }

    /**
     * Close the socket and wait until all connections end or delay.
     * 
     * @param delay maximum time to wait to stop the server default 10
     */
    public static void stopServer(int delay) {
	server.stop(delay);
    }

    private static int mapToPort() {
	int result = DEFAULT_PORT;
	try {
	    Object port = Server.PORT.value();
	    result = ((Integer) MappingUtils.map(port, Integer.class)).intValue();
	} catch (Exception e) {
	}
	return result;
    }
}
