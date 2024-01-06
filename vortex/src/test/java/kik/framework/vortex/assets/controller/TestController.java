package kik.framework.vortex.assets.controller;

import java.util.Map;

import kik.framework.vortex.annotations.Autowired;
import kik.framework.vortex.annotations.Controller;
import kik.framework.vortex.annotations.CrossOrigin;
import kik.framework.vortex.annotations.DeleteMapping;
import kik.framework.vortex.annotations.GetMapping;
import kik.framework.vortex.annotations.PostMapping;
import kik.framework.vortex.annotations.PutMapping;
import kik.framework.vortex.annotations.RequestBody;
import kik.framework.vortex.annotations.RequestMapping;
import kik.framework.vortex.annotations.RequestParam;
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
