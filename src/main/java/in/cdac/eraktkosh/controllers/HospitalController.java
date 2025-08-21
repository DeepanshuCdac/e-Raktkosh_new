package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.HospitalDTO;
import in.cdac.eraktkosh.services.HospitalService;

@RestController
@RequestMapping("/eraktkosh/hospitals")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@GetMapping("/by-state-district")
	public List<HospitalDTO> getHospitals(@RequestParam int stateCode, @RequestParam int districtId) {
		return hospitalService.getHospitalsByStateAndDistrict(stateCode, districtId);
	}
}
