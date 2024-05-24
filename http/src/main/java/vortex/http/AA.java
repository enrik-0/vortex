package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.parameter.Header;
import vortex.annotate.method.parameter.HttpRequest;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.exchange.Request;


@Controller
@RequestMapping("/a")
public class AA {

	public AA() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, UriException, InitiateServerException {
    ServerHttp.runServer();

	}
	
	@GetMapping("/muchas")
	public void a(@RequestParam int b) {
	    System.out.println("a");
	    
	}
	@GetMapping(uris = {"/pruebas"})
	public void b(@RequestParam int b, @Header("Accept-encoding") String e, @HttpRequest Request a) {
	    System.out.println("b");
	    
	}

}
