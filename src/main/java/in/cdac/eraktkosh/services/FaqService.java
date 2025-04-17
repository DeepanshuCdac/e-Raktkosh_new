package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.FaqDTO;
import in.cdac.eraktkosh.repository.FaqRepository;

@Service
public class FaqService {
	
	@Autowired
	private FaqRepository faqRepo;
	
	 public List<FaqDTO> getFaqList() {
	        return faqRepo.getAllFaqs();
	    }

}
