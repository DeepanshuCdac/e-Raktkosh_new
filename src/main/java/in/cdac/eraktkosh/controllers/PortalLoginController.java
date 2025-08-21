package in.cdac.eraktkosh.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.UpdateDonorDTO;
import in.cdac.eraktkosh.entity.PortalLoginEntity;
import in.cdac.eraktkosh.repository.EraktkoshPortalLoginRepository;
import in.cdac.eraktkosh.services.PortalLoginService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("eraktkosh")
public class PortalLoginController {

	@Autowired
	PortalLoginService portalLoginService;

	@Autowired
	EraktkoshPortalLoginRepository eraktkoshPortalLoginRepository;

	@Autowired
	HttpSession session;

	// 1. Original endpoint for generating both OTP and Captcha
	@PostMapping("/generateOTP")
	public ResponseEntity<Map<String, String>> generateOTP(@RequestBody PortalLoginEntity portalLogin) {
		Map<String, String> response1 = new HashMap<>();
		String mobile_no = portalLogin.getMobileno();
		String response = null;

		try {
			// Generate OTP
			response = portalLoginService.generateOtp(mobile_no);

			// Generate CAPTCHA
			String captcha = portalLoginService.generateCaptchaText();
			BufferedImage captchaImage = portalLoginService.generateCaptchaImage(captcha);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(captchaImage, "png", baos);
			String base64Captcha = Base64.getEncoder().encodeToString(baos.toByteArray());

			// Return OTP and CAPTCHA
			response1.put("captchaImage", "data:image/png;base64," + base64Captcha);
			response1.put("captchaText", captcha);
			response1.put("OtpData", response);

		} catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(response1, new HttpHeaders(), HttpStatus.OK);
	}

	// 2. New endpoint for regenerating only the OTP
	@PostMapping("/regenerateOtp")
	public ResponseEntity<Map<String, String>> regenerateOtp(@RequestBody PortalLoginEntity portalLogin) {
		Map<String, String> response = new HashMap<>();
		String mobile_no = portalLogin.getMobileno();

		try {
			// Generate OTP only
			String otpResponse = portalLoginService.generateOtp(mobile_no);
			response.put("OtpData", otpResponse);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	// 3. New endpoint for regenerating only the CAPTCHA
	@PostMapping("/regenerateCaptcha")
	public ResponseEntity<Map<String, String>> regenerateCaptcha() {
		Map<String, String> response = new HashMap<>();

		try {
			// Generate CAPTCHA only
			String captcha = portalLoginService.generateCaptchaText();
			BufferedImage captchaImage = portalLoginService.generateCaptchaImage(captcha);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(captchaImage, "png", baos);
			String base64Captcha = Base64.getEncoder().encodeToString(baos.toByteArray());

			// Return CAPTCHA
			response.put("captchaImage", "data:image/png;base64," + base64Captcha);
			response.put("captchaText", captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	// 4. Validate OTP and CAPTCHA ----- login
	@PostMapping("/validate")
	public ResponseEntity<?> validate(@RequestBody String requestData, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		JSONObject jsonObject = new JSONObject(requestData);

		// Extract values
		String inputOtp = jsonObject.getString("otp");
		String inputCaptcha = jsonObject.getString("captcha");
		String mobileNo = jsonObject.getString("mobile_no");
		System.out.println();
		return portalLoginService.validate(inputOtp, inputCaptcha, mobileNo);
	}

	// 5. New endpoint for fetching donor details ------ after login first details

	@PostMapping("/fetchDonorDetails")
	public ResponseEntity<?> fetchDonorDetails(@RequestBody Map<String, String> request) {
		String mobile_no = request.get("mobile_no");

		if (mobile_no == null || mobile_no.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Mobile number is required.");
		}

		ResponseEntity<?> donorDetails = portalLoginService.fetchUserDetails(mobile_no);

		if (donorDetails == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No donor details found for the given mobile number.");
		}

		return ResponseEntity.ok(donorDetails);
	}

//        6. Endpoint for fetching donor certificate.  -----  certificate

	@PostMapping("/fetchCertificateDetails")
	public ResponseEntity<?> getPreviousDonationDetailsByMobile(@RequestBody Map<String, String> request) {
		String mobileno = request.get("mobileno");
		if (mobileno == null || mobileno.isEmpty()) {
			return ResponseEntity.badRequest().body("Mobile number is required");
		}
		return portalLoginService.fetchPreviousDonationDetails(mobileno);
	}

//	7. endpoint for updating donor details in manage profile section...
	@PostMapping("/update")
	public ResponseEntity<String> updateDonor(@RequestBody UpdateDonorDTO donor) {
		boolean updated = portalLoginService.updateDonor(donor);
		if (updated) {
			return ResponseEntity.ok("Donor details updated successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No donor found with the provided mobile number.");
		}
	}
}
