package in.cdac.eraktkosh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallBackController {

	@RequestMapping("/beta")
	public String redirect() {
		return "forward:/index.html";
	}
}
