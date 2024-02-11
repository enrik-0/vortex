package vortex.http.test.controller;

import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.GetMapping;
import vortex.annotate.annotations.RequestMapping;
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
