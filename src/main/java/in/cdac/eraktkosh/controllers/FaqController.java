package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.FaqDTO;
import in.cdac.eraktkosh.services.FaqService;

@RestController
@RequestMapping("/eraktkosh/faq")
public class FaqController {
	
	@Autowired
	private FaqService faqService;
	
	@GetMapping("/list")
    public List<FaqDTO> getFaqs() {
        return faqService.getFaqList();
    }

}
