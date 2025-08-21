package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.SubscribeDonorDTO;
import in.cdac.eraktkosh.services.SubscribeDonorService;

@RestController
@RequestMapping("/eraktkosh/subscribe")
public class SubscribeDonorController {

	@Autowired
	private SubscribeDonorService subscribeDonorService;

	// Register a new user for subscription
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody SubscribeDonorDTO donorDTO) {
		String responseMessage = subscribeDonorService.registerUser(donorDTO);
		return ResponseEntity.ok(responseMessage);
	}

	@GetMapping("/unsubscribe")
	public ResponseEntity<String> unsubscribeUserViaLink(@RequestParam String email) {
		String response = subscribeDonorService.unsubscribeUser(email);

		String message;
		if ("Successfully unsubscribed".equals(response)) {
			message = "You have been successfully unsubscribed from e-Raktkosh notifications.";
		} else if ("User is already unsubscribed".equals(response)) {
			message = "You have already unsubscribed.";
		} else {
			message = "Sorry, we could not process your request. " + response;
		}

		String htmlResponse = "<html><head><title>Unsubscription Status</title>" + "<style>"
				+ "body { font-family: Arial, sans-serif; text-align: center; background-color: #f8f9fa; padding: 50px; }"
				+ ".container { background: white; padding: 20px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); border-radius: 10px; display: inline-block; }"
				+ "h2 { color: #d9534f; }" + "p { color: #333; font-size: 16px; }"
				+ ".home-link { display: inline-block; margin-top: 15px; padding: 10px 15px; background: #d9534f; color: white; text-decoration: none; border-radius: 5px; }"
				+ ".home-link:hover { background: #c9302c; }" + "</style></head><body>" + "<div class='container'>"
				+ "<h2>Unsubscription Status</h2>" + "<p>" + message + "</p>"
				+ "<a href='http://localhost:3000/beta#/publicPages/campSchedule' class='home-link'>Go to Homepage</a>"
				+ "</div></body></html>";

		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlResponse);
	}
}
