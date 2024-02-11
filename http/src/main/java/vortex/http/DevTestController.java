package vortex.http;

import java.util.Map;

import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.CrossOrigin;
import vortex.annotate.annotations.DeleteMapping;
import vortex.annotate.annotations.GetMapping;
import vortex.annotate.annotations.PostMapping;
import vortex.annotate.annotations.PutMapping;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestMapping;
import vortex.annotate.annotations.RequestParam;

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
