package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eraktkosh/faq")
public class FaqController {
	
	@Autowired
	private FaqService faqService;
	
	@Autowired
	private EmailService emailService;
	
	public FaqController(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping("/list")
    public List<FaqDTO> getFaqs() {
        return faqService.getFaqList();
    }
	
	 @PostMapping("/submit-question")
     public ResponseEntity<String> submitQuestion(@RequestBody SubmitQuestionRequestFaqDTO request) {
        emailService.sendUserQuestionToAdmin(request.getEmail(), request.getQuestion());
        return ResponseEntity.ok("Question submitted successfully!");
	    }
}
