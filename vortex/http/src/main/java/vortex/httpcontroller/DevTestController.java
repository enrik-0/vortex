package vortex.httpcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.Autowired;
import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.CrossOrigin;
import vortex.annotate.annotations.DeleteMapping;
import vortex.annotate.annotations.GetMapping;
import vortex.annotate.annotations.PostMapping;
import vortex.annotate.annotations.PutMapping;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestMapping;
import vortex.annotate.annotations.RequestParam;
import vortex.annotate.assets.service.DevTestService;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatus;

/**
 * 
 */
@Controller
@RequestMapping("/test")
@CrossOrigin("http://localhost:4200")
public class DevTestController {

	@Autowired
	private DevTestService testService;

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
	@GetMapping("/buenas")
	public String buenas() {
		List<HashMap<String, Object>> list = new ArrayList<>();
		HashMap<String, Object> map1 = new HashMap();
		map1.put("años", 2);
		map1.put("cierto", true);
		HashMap<String, Object> map2 = new HashMap();
		map2.put("no es cierto", false);
		map2.put("map", map1);
		list.add(map1);
		list.add(map2);

		return "123";
		//return new ResponseStatus<>(HttpStatus.CREATED, true);
	}
}
