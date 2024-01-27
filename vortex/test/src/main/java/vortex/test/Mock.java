package vortex.test;

import java.io.IOException;

import vortex.http.ServerHttp;

public final class Mock {

	private static Mock instance;
	
	public static Mock getInstance() throws IOException{
		if(instance == null) {
			instance = new Mock();
		}
		return instance;
	}

	private Mock() throws IOException {
		ServerHttp.runServer(8080);
	}
	
	public static void stop() {
		ServerHttp.stopServer();
	}


}
