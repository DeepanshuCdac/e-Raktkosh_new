package in.cdac.eraktkosh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.dto.BloodAvailabilityEmailDTO;
import in.cdac.eraktkosh.services.BloodAvailabilityService;
import in.cdac.eraktkosh.services.EmailService;

@RestController
@RequestMapping("/eraktkosh/blood-availability")
public class BloodAvailabilityController {

    @Autowired
    private BloodAvailabilityService bloodAvailabilityService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<BloodAvailabilityDTO> fetchBloodAvailability(
            @RequestParam Integer stateCode,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(required = false) Integer componentId,
            @RequestParam(required = false) Integer bloodGroupId,
            @RequestParam(required = false) List<Integer> hospitalCodes) {

        return bloodAvailabilityService.fetchBloodAvailability(
                stateCode, districtId, componentId, bloodGroupId, hospitalCodes
        );
    }
    
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody BloodAvailabilityEmailDTO request) {
        try {
            boolean sent = emailService.sendBloodAvailabilityEmail(
                request.getEmail(),
                request.getHospitalCode(),
                request.getStateCode()
            );
            if (sent) {
                return ResponseEntity.ok("Email sent successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found or email sending failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal error: " + e.getMessage());
        }
    }

}
