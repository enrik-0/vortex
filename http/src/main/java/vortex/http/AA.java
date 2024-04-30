package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


import vortex.annotate.components.Controller;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.status.Series;
import vortex.properties.kinds.Server;


@Controller
@RequestMapping("/a")
public class AA {

	public AA() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
    ServerHttp.runServer();

	}
	
	@GetMapping("/prueba")
	public void a(@RequestParam int b) {
	    
	    
	}

}
