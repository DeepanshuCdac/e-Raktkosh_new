package in.cdac.eraktkosh.controllers;

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
    public List<CampDetailDTO> getCamps(
            @RequestParam("stateCode") int stateCode,
            @RequestParam(value = "districtCode", required = false) Integer districtCode,
            @RequestParam(value = "campDate", required = false) String campDate) {
        
        return campDetailService.getCamps(stateCode, districtCode, campDate);
    }
}
