package vortex.http.test.controller;

import java.util.Map;

import vortex.annotate.annotations.Controller;
import vortex.annotate.annotations.PostMapping;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestMapping;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.ResponseStatus;

@Controller
@RequestMapping("/map")
public class MapTest {

	@PostMapping("/integer")
	public Map<String, Integer> integer1(
			@RequestBody Map<String, Integer> data) {
		return data;
	}
	@PostMapping("/integerResponse")
	public ResponseStatus<Map<String, Integer>> integer1Response(
			@RequestBody Map<String, Integer> data) {
		return new ResponseStatus<>(HttpStatus.CREATED, data);
	}

	@PostMapping("/string")
	public Map<String, String> string(@RequestBody Map<String, String> data) {
		return data;
	}

	@PostMapping("/stringResponse")
	public ResponseStatus<Map<String, String>> stringResponse(
			@RequestBody Map<String, String> data) {
		return new ResponseStatus<Map<String, String>>(HttpStatus.CREATED, data);
	}
	@PostMapping("/boolean")
	public Map<String, Boolean> bool(@RequestBody Map<String, Boolean> data) {
		return data;
	}
	@PostMapping("/booleanResponse")
	public ResponseStatus<Map<String, Boolean>> boolResponse(
			@RequestBody Map<String, Boolean> data) {
		return new ResponseStatus<Map<String, Boolean>>(HttpStatus.CREATED, data);
	}
	@PostMapping("/mix")
	public Map<String, Object> mix(@RequestBody Map<String, Object> data) {
		return data;
	}
	@PostMapping("/mixResponse")
	public ResponseStatus<Map<String, Object>> mixResponse(
			@RequestBody Map<String, Object> data) {
		return new ResponseStatus<Map<String, Object>>(HttpStatus.CREATED, data);
	}

	@PostMapping("/maps")
	public Map<String, Object> maps(@RequestBody Map<String, Object> data) {
		return data;
	}
	@PostMapping("/mapsResponse")
	public ResponseStatus<Map<String, Object>> mapsResponse(
			@RequestBody Map<String, Object> data) {
		return new ResponseStatus<Map<String, Object>>(HttpStatus.CREATED, data);
	}
}
