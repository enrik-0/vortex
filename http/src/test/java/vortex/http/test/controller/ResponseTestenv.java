package vortex.http.test.controller;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.http.elements.ResponseStatus;

@Controller
@RequestMapping("/responseStatus")
public class ResponseTestenv {

	@GetMapping("/addHeader")
	public ResponseStatus<String> addHeader(){
		var response = new ResponseStatus<String>();

		response.setHeader("headerTest", "test1");
		return response;
		
	}

}
