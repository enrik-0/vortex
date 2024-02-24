package vortex.test;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.exchange.ResponseStatus;
import vortex.http.status.HttpStatus;

@Controller
@RequestMapping("/test")
public class testController {

	@GetMapping("/number")
	public long number(@RequestParam long number) {
		return number;
	}
	
	@GetMapping("/ResponseNumber")
	public ResponseStatus<Long> responseStatusNumber(@RequestParam long number){
		return new ResponseStatus<Long>(HttpStatus.CREATED, number);
	}
	
	@GetMapping("/floating")
	public double floating(@RequestParam double number) {
		return number;
	}
	
	@GetMapping("/ResponseNumberFloat")
	public ResponseStatus<Double> responseStatusFloatingNumber(@RequestParam double number){
		return new ResponseStatus<Double>(HttpStatus.CREATED, number);
	}
	
	@GetMapping("/decision")
	public boolean decision(@RequestParam boolean decision) {
		return decision;
	}
	
	@GetMapping("/ResponseDecision")
	public ResponseStatus<Boolean> responseStatusDecision(@RequestParam boolean decision){
		return new ResponseStatus<Boolean>(HttpStatus.CREATED, decision);
	}
	
	@GetMapping("/string")
	public String string(@RequestParam String string) {
		return string;
	}
	
	@GetMapping("/ResponseString")
	public ResponseStatus<String> responseStatusDecision(@RequestParam String string){
		return new ResponseStatus<String>(HttpStatus.CREATED, string);
	}
}
