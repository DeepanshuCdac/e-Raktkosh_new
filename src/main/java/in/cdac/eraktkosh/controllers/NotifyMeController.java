package in.cdac.eraktkosh.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.NotifyMeDTO;
import in.cdac.eraktkosh.services.NotifyMeService;

@RestController
@RequestMapping("/eraktkosh/notify_me")
public class NotifyMeController {

	@Autowired
	private NotifyMeService service;

	@PostMapping("/bloodsearch")
	public ResponseEntity<String> saveNotification(@RequestBody @Valid NotifyMeDTO request) {
		try {
			String response = service.insertNotification(request);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body("Validation Failed: " + ex.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
		}
	}
}
