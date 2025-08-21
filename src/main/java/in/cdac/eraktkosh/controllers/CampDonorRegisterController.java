package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.CampDonorRegisterDTO;
import in.cdac.eraktkosh.services.CampDonorRegisterService;

@RequestMapping("/eraktkosh/get")
@RestController
public class CampDonorRegisterController {

	@Autowired
	private CampDonorRegisterService donorService;

	@GetMapping("/{mobile}")
	public ResponseEntity<List<CampDonorRegisterDTO>> getDonorByMobile(@PathVariable String mobile) {
		List<CampDonorRegisterDTO> donors = donorService.getDonorDetails(mobile);
		return ResponseEntity.ok(donors);
	}
}
