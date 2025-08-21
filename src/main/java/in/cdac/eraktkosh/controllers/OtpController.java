package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.OtpRequestDTO;
import in.cdac.eraktkosh.services.OtpServices;
import in.cdac.eraktkosh.services.OtpServices.OtpCaptchaResponse;

@RestController
@RequestMapping("/eraktkosh/otp")
public class OtpController {

	@Autowired
	private OtpServices otpService;

	// 🔹 Initial request: generate OTP + CAPTCHA
	@PostMapping("/generate")
	public ResponseEntity<?> generateOtpAndCaptcha(@RequestBody OtpRequestDTO request) {
		if (request.getMobileNo() == null || request.getMobileNo().length() != 10) {
			return ResponseEntity.badRequest().body("Invalid mobile number");
		}

		OtpCaptchaResponse response = otpService.generateOtpAndCaptcha(request.getMobileNo());
		return ResponseEntity.ok(response);
	}

	// 🔹 Resend OTP only
	@PostMapping("/resend")
	public ResponseEntity<?> resendOtp(@RequestBody OtpRequestDTO request) {
		if (request.getMobileNo() == null || request.getMobileNo().length() != 10) {
			return ResponseEntity.badRequest().body("Invalid mobile number");
		}

		try {
			String newOtp = otpService.resendOtp(request.getMobileNo());
			return ResponseEntity.ok("OTP resent successfully");
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	// 🔹 Refresh CAPTCHA only
	@PostMapping("/refresh-captcha")
	public ResponseEntity<?> refreshCaptcha(@RequestBody OtpRequestDTO request) {
		if (request.getMobileNo() == null || request.getMobileNo().length() != 10) {
			return ResponseEntity.badRequest().body("Invalid mobile number");
		}

		try {
			String newCaptchaImage = otpService.refreshCaptcha(request.getMobileNo());
			return ResponseEntity.ok(newCaptchaImage); // returns base64 image string
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	// 🔹 Validate OTP and CAPTCHA
	@PostMapping("/validate")
	public ResponseEntity<?> validateOtp(@RequestBody OtpRequestDTO request) {
		if (request.getMobileNo() == null || request.getOtp() == null || request.getCaptcha() == null) {
			return ResponseEntity.badRequest().body("Mobile number, OTP, and CAPTCHA are required");
		}

		boolean valid = otpService.validateOtp(request.getMobileNo(), request.getOtp(), request.getCaptcha());
		if (!valid) {
			return ResponseEntity.badRequest().body("Invalid or expired OTP/CAPTCHA");
		}

		return ResponseEntity.ok("OTP validated successfully");
	}
}
