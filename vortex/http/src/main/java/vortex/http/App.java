package vortex.http;

import java.io.IOException;

import vortex.annotate.manager.AnnotationManager;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		AnnotationManager.getInstance();
		ServerHttp.runServer(8080);
	}
}
