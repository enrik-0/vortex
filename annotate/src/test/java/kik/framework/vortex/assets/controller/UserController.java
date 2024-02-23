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
import kik.framework.vortex.assets.service.UserService;

@Controller
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	
	@Autowired
	public UserService userService;
	
	
	@PutMapping("/register")
	public void register(@RequestBody Map<String, Object> data) {
		//do stuff
	}
	
	@PostMapping("/login")
	public void login(@RequestBody Map<String, Object> data) {
		//do stuff
	}
	@GetMapping("/logged")
	public void islogged(@RequestParam String username) {
		//do stuff
	}
	
	@DeleteMapping("/erease")
	public void delete(@RequestParam String token, @RequestBody Map<String, Object> data, @RequestParam int years) {
		//do stuff
	}

}
