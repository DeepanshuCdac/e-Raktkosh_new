package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.CampDonorRegisterRequestDTO;
import in.cdac.eraktkosh.services.CampDonorRegisterRequestService;

@RestController
@RequestMapping("/eraktkosh/CampDonorRegistration")
public class CampDonorRegisterRequestController {

	@Autowired
	private CampDonorRegisterRequestService campRegistrationService;

	@PostMapping("/register")
	public ResponseEntity<String> registerDonor(@RequestBody CampDonorRegisterRequestDTO request) {
		try {
			campRegistrationService.registerCampDonor(request);
			return ResponseEntity.ok("Donor registered successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Registration failed: " + e.getMessage());
		}
	}

}
