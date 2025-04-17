package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.services.BloodAvailabilityService;

@RestController
@RequestMapping("/eraktkosh/blood-availability")
public class BloodAvailabilityController {
	
	@Autowired
	private BloodAvailabilityService bloodAvailabilityService;

    @GetMapping
    public List<BloodAvailabilityDTO> fetchBloodAvailability(
            @RequestParam Integer stateCode,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer componentId,
            @RequestParam(required = false) Integer bloodGroupId) {
        return bloodAvailabilityService.fetchBloodAvailability(stateCode, districtId, componentId, bloodGroupId);
    }

}
