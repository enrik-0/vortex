package vortex.http.test.controller;

import java.util.List;
import java.util.Map;
import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.CrossOrigin;
import vortex.annotate.annotations.GetMapping;
import vortex.annotate.annotations.PostMapping;
import vortex.annotate.annotations.PutMapping;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestMapping;
import vortex.annotate.annotations.RequestParam;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatusException;

@Controller
@RequestMapping("/users")
@CrossOrigin
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
	
	
	

	@PostMapping("/erease")
	public Map<String, Object> delete(@RequestParam String token,
			@RequestBody Map<String, Object> data, @RequestParam int years) throws ResponseStatusException {
		String result = "";
		result += token + years;
		data.forEach((s, o) -> {
			//do somethig
		});
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can´t do it");
		
	}

}
