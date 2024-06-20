package vortex.http.test.controller;

import java.util.List;
import java.util.Map;

import vortex.annotate.components.Controller;
import vortex.annotate.controller.CrossOrigin;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.method.mapping.GetMapping;
import vortex.annotate.method.mapping.PostMapping;
import vortex.annotate.method.mapping.PutMapping;
import vortex.annotate.method.parameter.RequestBody;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.exchange.ResponseStatusException;
import vortex.http.status.HttpStatus;

@Controller
@RequestMapping("/users")
public class DevUserController {

    @PutMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> data) {
	// do stuff
	return data;
    }

    @PostMapping("/login")
    public void login(@RequestBody List<Map<String, Integer>> data) {
	// do stuff
	data.stream().forEach(map -> {
	    map.forEach((s, i) -> {
		System.out.printf(String.format("key %s \n", s));
		System.out.printf(String.format("value %d \n ", i));
	    });
	});
    }

    @GetMapping("/logged")
    public void islogged(@RequestParam String username) {
	// do stuff
    }

    @PostMapping("/BodyTest")
    public void bodyTest() {
    }

    @GetMapping("/getWBody")
    public void getWBody(@RequestBody Map<String, Object> data) {

    }

    @PostMapping("/erease")
    public Map<String, Object> delete(@RequestParam String token, @RequestBody Map<String, Object> data,
	    @RequestParam int years) throws ResponseStatusException {
	String result = "";
	result += token + years;
	data.forEach((s, o) -> {
	    // do somethig
	});
	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can´t do it");

    }

}
