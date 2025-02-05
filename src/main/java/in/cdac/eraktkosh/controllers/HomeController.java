package in.cdac.eraktkosh.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class HomeController {

	@GetMapping
	public String getMessage() {

		return "Hello";
	}

	@GetMapping("/twos")
	public String savedata(@RequestBody Users users) {
		System.out.println(users.getName());
		return users.getName().toString();
	}

	@PostMapping("/one")
	public String getData(@RequestParam String name, @RequestParam String address) {
		System.out.println(name);
		return address;
	}

	@GetMapping("/deepanshu")
	public String hello() {
		return "hiii deepanshu";

	}

}
