package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.DonorResponseDTO;
import in.cdac.eraktkosh.services.DonorResponseService;

@RestController
@RequestMapping("/api/response")
public class DonorResponseController {

	@Autowired
	private DonorResponseService service;

	@PostMapping
	public ResponseEntity<String> updateResponse(@RequestBody DonorResponseDTO dto) {
		try {
			service.handleDonorResponse(dto);
			return ResponseEntity.ok("Response saved");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving response");
		}
	}

}