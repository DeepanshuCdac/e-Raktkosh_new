package in.cdac.eraktkosh.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hazelcast.map.IMap;

import in.cdac.eraktkosh.config.OTP_CONFIG;
import in.cdac.eraktkosh.entity.RegisterEntity;
import in.cdac.eraktkosh.repository.RegisterRepository;

@Service
public class RegisterService {

	@Autowired
	private RegisterRepository registerRepository;

//    @Autowired
//    private HazelcastInstance hazelcastInstance;

	@Autowired
	private OTP_CONFIG otp_config;

	private static final int OTP_EXPIRATION_TIME = 5; // * 60 * 1000 5 minutes

	/**
	 * Generates a random CAPTCHA text with alphanumeric characters.
	 *
	 * @return CAPTCHA text
	 */
	public String generateCaptchaText() {
		int length = 7; // Length of CAPTCHA text
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
		StringBuilder captchaText = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			captchaText.append(characters.charAt(index));
		}
		return captchaText.toString();
	}

	/**
	 * Generates a CAPTCHA image based on the given text.
	 *
	 * @param captchaText CAPTCHA text
	 * @return BufferedImage representing the CAPTCHA
	 */
	public BufferedImage generateCaptchaImage(String captchaText) {
		int width = 150;
		int height = 50;

		// Create an image with RGB color
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		// Set background color and fill the image
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		// Draw random lines for noise
		Random random = new Random();
		graphics.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i < 15; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			graphics.drawLine(x1, y1, x2, y2);
		}

		// Set font and color for the CAPTCHA text
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		graphics.setColor(Color.BLACK);

		// Draw the CAPTCHA text
		int x = 0;
		int y = 35;
		for (char c : captchaText.toCharArray()) {
			graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			graphics.drawString(String.valueOf(c), x, y);
			x += 20; // Spacing between characters
		}

		graphics.dispose();
		return image;
	}

	/**
	 * Generates a random OTP with numeric characters and updates OTP count. If the
	 * OTP count exceeds the limit, an appropriate message is returned.
	 *
	 * @param mobileNumber Mobile number for which the OTP is generated
	 * @return Generated OTP or error message
	 */
	public String generateOtpWithCount(String mobileNumber) {
		int otpCount = 0;

		try {
			otpCount = registerRepository.getOtpCount(mobileNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (otpCount >= 5) {
			return "OTP limit exceeded";
		}

		// Generate the OTP
		int length = 6; // Length of the OTP
		String numbers = "0123456789";
		StringBuilder otp = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(numbers.length());
			otp.append(numbers.charAt(index));
		}

		// Update the OTP count in the database
		try {
			registerRepository.insertOtpCount(mobileNumber);
			IMap<String, String> otpMap = otp_config.getHazelcastInstance().getMap("otpMap");
			otpMap.put(mobileNumber, otp.toString(), OTP_EXPIRATION_TIME, TimeUnit.MINUTES);

		} catch (Exception e) {
			e.printStackTrace();
			return "Error updating OTP count";
		}

		return otp.toString();
	}

	/**
	 * RegisterUser If the otp entered is correct.
	 */
	public String registerUser(RegisterEntity user) {
		int rowsAffected = registerRepository.RegisterNewDonor(user);
		if (rowsAffected > 0) {
			System.out.println("User registered");
			return "User registered successfully!";
		} else {
			return "User already exist!";
		}
	}

	/* validate Register Otp */

	public ResponseEntity<?> validateOpt(String mobileNumber, String otp, RegisterEntity user) {
		String res = "Vaild OTP";
		try {

			IMap<String, String> otpMap = otp_config.getHazelcastInstance().getMap("otpMap");
			String storedOtp = otpMap.get(mobileNumber);

			// Validate OTP
			if (storedOtp == null) {
				res = "OTP not found for the given mobile number.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP not found for the given mobile number.");
			}
			if (!storedOtp.equals(otp)) {
				res = "Invalid OTP.";
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
			}

			res = registerUser(user);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in validateOpt");
		}
		return ResponseEntity.status(HttpStatus.OK).body(res);

	}

}
