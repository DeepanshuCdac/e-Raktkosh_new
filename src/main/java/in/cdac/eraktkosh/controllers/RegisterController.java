package in.cdac.eraktkosh.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.entity.RegisterEntity;
import in.cdac.eraktkosh.services.RegisterService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("eraktkosh")
public class RegisterController {

	@Autowired
	private RegisterService userService;

	// 1.. OTP generation endpoint
	@PostMapping("/generateOtpDonorRegistration")
	public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
		Map<String, String> response = new HashMap<>();

		String mobileNumber = request.get("mobileNumber");
		if (mobileNumber == null || mobileNumber.isEmpty()) {
			response.put("message", "Mobile number is required.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			String otpResponse = userService.generateOtpWithCount(mobileNumber);

			if ("OTP limit exceeded".equals(otpResponse)) {
				response.put("message", otpResponse);
				return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
			}

			response.put("otp", otpResponse);
			System.out.println("Otp == " + otpResponse);
			response.put("message", " OTP for registration " + otpResponse);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error generating OTP.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 2.. CAPTCHA generation end point
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

			// Prepare response
			response.put("captchaImage", "data:image/png;base64," + base64Captcha);
			response.put("captchaText", captchaText);

		} catch (IOException e) {
			e.printStackTrace();
			response.put("error", "Failed to generate CAPTCHA.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	// 3..Registeration endpoint
	@PostMapping("/donor/register")
	public ResponseEntity<String> registerUser(@RequestBody RegisterEntity user) {
		String response = userService.registerUser(user);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/donor/registerValidate")
	public ResponseEntity<?> registerValidate(@RequestBody String requestData) {
		JSONObject json = new JSONObject(requestData);
		RegisterEntity user = new RegisterEntity();

		String otp = json.optString("otp", "");
		String mobile_no = json.optString("mobileNo", "");

		if (otp == null || otp.isEmpty()) {
			return new ResponseEntity<>("OTP is required", HttpStatus.UNAUTHORIZED);
		}
		if (mobile_no == null || mobile_no.isEmpty()) {
			return new ResponseEntity<>("Mobile Number is required", HttpStatus.UNAUTHORIZED);
		}

		user.setUserId(json.optString("userId", ""));
		String userid = user.getUserId();
		if (userid == null || userid.isEmpty()) {
			return new ResponseEntity<>("User ID is required", HttpStatus.UNAUTHORIZED);
		}

		user.setMobileNo(json.optString("mobileNo", null));
		user.setFirstName(json.optString("firstName", null));
		user.setLastName(json.optString("lastName", null));
		user.setBloodGroupCode(Integer.parseInt(json.optString("bloodGroupCode", null)));
		user.setPassword(json.optString("password", null));
		user.setFirstLogin(Integer.parseInt(json.optString("firstLogin", null)));
		user.setDemographics(Integer.parseInt(json.optString("demographics", null)));
		user.setIsValid(Integer.parseInt(json.optString("isValid", "0")));
		user.setStateCode(Integer.parseInt(json.optString("stateCode", null)));
		user.setDistrictCode(Integer.parseInt(json.optString("districtCode", null)));
		user.setPinCode(json.optString("pinCode", null));
		user.setEmailId(json.optString("emailId", null));
		user.setGenderCode(json.optString("genderCode", null));
		user.setAddress(json.optString("address", null));
		user.setFatherName(json.optString("fatherName", null));
		user.setAllBlood(Integer.parseInt(json.optString("allBlood", null)));
		user.setRepository(Integer.parseInt(json.optString("repository", null)));
		user.setRegistrationMode(Integer.parseInt(json.optString("registrationMode", null)));

		return userService.validateOpt(mobile_no, otp, user);
	}

}
