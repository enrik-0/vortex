package vortex.annotate.assets.controller;

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

@Controller
@RequestMapping("/users")
@CrossOrigin
public class DevUserController {
	
	@Autowired
	public DevUserService userService;
	
	
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
