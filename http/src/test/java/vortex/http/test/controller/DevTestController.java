package vortex.http.test.controller;

import java.util.Map;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.CrossOrigin;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.DeleteMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.mapping.PostMapping;
import vortex.annotate.method.mapping.PutMapping;
import vortex.annotate.method.parameter.RequestBody;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatus;

/**
 * 
 */
@Controller
@RequestMapping("/test")
@CrossOrigin("http://localhost:4200")
public class DevTestController {


	@PutMapping("/execute")
	public void executeTest(@RequestBody Map<String, Object> testData) {
		// execute
	}

	@PostMapping("/analyze")
	public Map<String, Object> analyzeTestResults(@RequestBody Map<String, Object> testData) {
		return testData;
	}

	@GetMapping("/status")
	public String checkStatus(@RequestParam boolean testId, @RequestParam String output) {
		// status
		return "status";
	}

	@DeleteMapping("/cleanup")
	public void cleanupTestEnvironment(@RequestParam boolean testToken, @RequestParam int cleanupTime) {
		// clean
	}
}
