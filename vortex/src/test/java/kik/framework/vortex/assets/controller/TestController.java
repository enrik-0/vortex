package kik.framework.vortex.assets.controller;

import kik.framework.vortex.assets.service.TestService;

@Controller
@RequestMapping("/tests")
@CrossOrigin
public class TestController {

    @Autowired
    public TestService testService;

    @PutMapping("/execute")
    public void executeTest(@RequestBody Map<String, Object> testData) {
        // execute
    }

    @PostMapping("/analyze")
    public void analyzeTestResults(@RequestBody Map<String, Object> testData) {
        // analyze
    	}

    @GetMapping("/status")
    public void checkStatus(@RequestParam boolean testId, @RequestParam String output) {
        // status
    }

    @DeleteMapping("/cleanup")
    public void cleanupTestEnvironment(@RequestParam boolean testToken, @RequestParam int cleanupTime) {
        // clean
    }
}
