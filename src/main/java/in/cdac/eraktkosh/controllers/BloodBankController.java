package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.BloodBankDTO;
import in.cdac.eraktkosh.services.BloodBankService;

@RestController
@RequestMapping("/eraktkosh/bloodbanks")
public class BloodBankController {
	
	 @Autowired
	    private BloodBankService service;

	 @GetMapping
	 public List<BloodBankDTO> getBloodBanks(
	         @RequestParam("stateCode") int stateCode,
	         @RequestParam(value = "districtCode", required = false) Integer districtCode) {
	     
	     return service.getBloodBanksByStateAndDistrict(stateCode, districtCode != null ? districtCode : 0);
	 }


}
