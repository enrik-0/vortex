package kik.framework.vortex.assets.controller;

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
