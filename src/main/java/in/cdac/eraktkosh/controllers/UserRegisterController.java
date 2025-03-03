package in.cdac.eraktkosh.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.services.UserRegisterService;

@RestController
@RequestMapping("/eraktkosh")
public class UserRegisterController {

	private final UserRegisterService userService;

	public UserRegisterController(UserRegisterService userService) {
		this.userService = userService;
	}

	// 1.. CAPTCHA generation end point
	@GetMapping("/generateCaptchaforRegistration")
	public ResponseEntity<Map<String, String>> generateCaptcha() {
		Map<String, String> response = new HashMap<>();

		try {
			// Generate captcha text
			String captchaText = userService.generateCaptchaText();

			// Generate captcha image
			BufferedImage captchaImage = userService.generateCaptchaImage(captchaText);

			// Log captcha text to console for debugging
			System.out.println("Generated CAPTCHA Text: " + captchaText);

			// Convert CAPTCHA image to Base64
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(captchaImage, "png", baos);
			String base64Captcha = Base64.getEncoder().encodeToString(baos.toByteArray());

			response.put("captchaImage", "data:image/png;base64," + base64Captcha);
			response.put("captchaText", captchaText);

		} catch (IOException e) {
			e.printStackTrace();
			response.put("error", "Failed to generate CAPTCHA.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/generateOtp")
	public Map<String, Object> generateOtp(@RequestBody Map<String, String> requestBody) {
		String mobileNo = requestBody.get("mobileNo");

		// Prepare response object
		Map<String, Object> response = new HashMap<>();
		response.put("mobileNo", mobileNo);

		int mobileExists = userService.checkMobileExists(mobileNo);
		int otpCount = userService.getOtpCount(mobileNo);

		response.put("mobileNoExist", mobileExists);
		response.put("otpCount", otpCount);

		// If mobile number already registered
		if (mobileExists > 0) {
			response.put("message", "Mobile number already registered");
			return response;
		}

		// If OTP limit exceeded
		if (otpCount >= 5) {
			response.put("message", "OTP limit reached");
			return response;
		}

		// Generate OTP
		String otp = userService.generateOtp(mobileNo);
		response.put("otp", otp);
		response.put("message", "OTP sent successfully");

		return response;
	}

	@PostMapping("/validateOtpAndRegister")
	public Map<String, Object> validateOtpAndRegister(@RequestBody Map<String, String> requestBody) {
		String mobileNo = requestBody.get("mobileNo");
		String otp = requestBody.get("otp");

		// Prepare response object
		Map<String, Object> response = new HashMap<>();
		response.put("mobileNo", mobileNo);

		// Validate OTP
		if (!userService.validateOtp(mobileNo, otp)) {
			response.put("message", "Invalid OTP");
			return response;
		}

		// Save user details
		boolean isRegistered = userService.saveUserDetails(requestBody);
		response.put("message", isRegistered ? "User registered successfully" : "User registration failed");

		return response;
	}

}
