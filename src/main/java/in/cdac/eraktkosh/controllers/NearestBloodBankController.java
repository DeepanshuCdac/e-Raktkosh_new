package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.NearestBloodBank;
import in.cdac.eraktkosh.services.NearestBloodBankService;

@RestController
@RequestMapping("/eraktkosh/bloodbank")
public class NearestBloodBankController {

	@Autowired
	private NearestBloodBankService nearestBloodBankService;

	@GetMapping("/nearest")
	public List<NearestBloodBank> getNearestHospitals(@RequestParam(required = true) Integer stateCode,
			@RequestParam(required = false, defaultValue = "-1") Integer districtCode) {
		if (stateCode == null) {
			throw new IllegalArgumentException("stateCode is required.");
		}
		return nearestBloodBankService.getNearestHospitals(stateCode, districtCode);
	}
}
