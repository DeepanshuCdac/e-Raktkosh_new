package in.cdac.eraktkosh.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.FaqDTO;
import in.cdac.eraktkosh.dto.SubmitQuestionRequestFaqDTO;
import in.cdac.eraktkosh.services.EmailService;
import in.cdac.eraktkosh.services.FaqService;

@RestController
@RequestMapping("/eraktkosh")
public class FaqController {

	@Autowired
	private FaqService faqService;

	@Autowired
	private EmailService emailService;

	public FaqController(EmailService emailService) {
		this.emailService = emailService;
	}

	@GetMapping("/question/list")
	public List<FaqDTO> getFaqs() {
		return faqService.getFaqList();
	}

	@PostMapping("/faq/submit-question")
	public ResponseEntity<String> submitQuestion(@Valid @RequestBody SubmitQuestionRequestFaqDTO request) {
		// Validate email format
		if (!isValidEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body("Invalid email format");
		}

		// Validate question not empty
		if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Question cannot be empty");
		}

		try {
			emailService.sendUserQuestionToAdmin(request.getEmail(), request.getQuestion());
			return ResponseEntity.ok("Question submitted successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to submit question. Please try again later.");
		}
	}

	private boolean isValidEmail(String email) {
		return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	}
}
