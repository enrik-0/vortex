package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import vortex.http.status.Series;

public class AA {

	public AA() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		ServerHttp.runServer(8080);

	}

}
