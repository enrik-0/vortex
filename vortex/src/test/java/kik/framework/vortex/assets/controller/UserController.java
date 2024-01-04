package kik.framework.vortex.assets.controller;


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
	public void login(@RequesBody Map<String, Object> data) {
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
