package kik.framework.vortex.assets.controller;

import java.util.Map;

import vortex.annotate.annotations.Autowired;
import vortex.annotate.components.Controller;
import vortex.annotate.controller.CrossOrigin;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.DeleteMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.mapping.PostMapping;
import vortex.annotate.method.mapping.PutMapping;
import vortex.annotate.method.parameter.RequestBody;
import vortex.annotate.method.parameter.RequestParam;
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
