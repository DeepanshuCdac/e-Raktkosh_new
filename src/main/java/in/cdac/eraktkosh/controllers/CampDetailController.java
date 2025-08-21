package in.cdac.eraktkosh.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.CampDetailDTO;
import in.cdac.eraktkosh.services.CampDetailService;

@RestController
@RequestMapping("/eraktkosh/camps")
public class CampDetailController {

	@Autowired
	private CampDetailService campDetailService;

	@GetMapping("/details")
	public List<CampDetailDTO> getCamps(@RequestParam("stateCode") int stateCode,
			@RequestParam(value = "districtCode", required = false) Integer districtCode,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {

		LocalDate today = LocalDate.now();
		String todayDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		if (startDate == null || startDate.isEmpty()) {
			startDate = todayDate;
		}
		if (endDate == null || endDate.isEmpty()) {
			endDate = todayDate;
		}

		return campDetailService.getCamps(stateCode, districtCode, startDate, endDate);
	}

	@GetMapping("/hospitalCode")
	public List<CampDetailDTO> getCampsByHospitalCode(@RequestParam("hospitalCode") Integer hospitalCode) {
		return campDetailService.getCampsByHospitalCode(hospitalCode);
	}

}
