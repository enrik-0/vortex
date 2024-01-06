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
import kik.framework.vortex.assets.service.DevUserService;

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
