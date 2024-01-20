package vortex.http.test.controller;

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
import vortex.annotate.assets.service.DevUserService;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatus;
import vortex.http.elements.ResponseStatusException;

@Controller
@RequestMapping("/users")
@CrossOrigin
public class DevUserController {

	@Autowired
	public DevUserService userService;

	@PutMapping("/register")
	public void register(@RequestBody Map<String, Object> data) {
		// do stuff
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
	public ResponseStatus<Map<String, Object>> delete(@RequestParam String token,
			@RequestBody Map<String, Object> data, @RequestParam int years) throws ResponseStatusException {
		String result = "";
		result += token + years;
		print(token);
		print(years);
		data.forEach((s, o) -> {
			print(s);
			print(o);
		});
		throw new ResponseStatusException(HttpStatus.OK, "te cagaste encima");
		/*ResponseStatus<Map<String,Object>> response = new ResponseStatus<Map<String, Object>>(HttpStatus.OK, data);
		response.setHeader("token", "envergadura");
		response.setHeader("numero", 2);
		return response;*/
		
	}
	public void print(Object message) {
		System.err.println(message);
	}

}
