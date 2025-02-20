package in.cdac.eraktkosh.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.hazelcast.map.IMap;

import in.cdac.eraktkosh.config.OTP_CONFIG;
import in.cdac.eraktkosh.repository.UserRegisterRepository;
import in.cdac.eraktkosh.utility.SendMessageToUser;

@Service
public class UserRegisterService {

	private final UserRegisterRepository userRepository;
	private final OTP_CONFIG otpConfig;
	private static final int OTP_EXPIRATION_TIME = 5;

	public UserRegisterService(UserRegisterRepository userRepository, OTP_CONFIG otpConfig) {
		this.userRepository = userRepository;
		this.otpConfig = otpConfig;
	}

	public int checkMobileExists(String mobileNo) {
		return userRepository.checkMobileExists(mobileNo);
	}

	public int getOtpCount(String mobileNo) {
		return userRepository.getOtpCount(mobileNo);
	}

	public String generateOtp(String mobileNo) {
		int mobileExists = checkMobileExists(mobileNo);

		// If mobile is already registered
		if (mobileExists != 0) {
			return "Mobile number already registered.";
		}

		int otpCount = getOtpCount(mobileNo);

		// Enforce OTP request limit (max 5 attempts)
		if (otpCount >= 5) {
			return "OTP request limit reached.";
		}

		// Generate OTP
		String otp = String.valueOf(new Random().nextInt(900000) + 100000);

		// Store OTP in Hazelcast
		IMap<String, String> otpMap = otpConfig.getHazelcastInstance().getMap(OTP_CONFIG.otpCache);
		otpMap.put(mobileNo, otp, OTP_EXPIRATION_TIME, TimeUnit.MINUTES);

		System.out.println("Generated OTP: " + otp);
		System.out.println("OTP Count for Mobile: " + mobileNo + " is " + (otpCount + 1));

		// Store OTP count in DB
		userRepository.insertOtp(mobileNo);
		// Store OTP Log in DB.
		userRepository.insertOtpLog(mobileNo, otp);

		String contactNo = "*******" + mobileNo.substring(mobileNo.length() - 3);
		String message = "Your eRaktKosh OTP for username " + contactNo + " is: " + otp
				+ ". Please do not share your OTP with anyone.";

		// Send OTP to user
		SendMessageToUser.SendOTP(message, mobileNo);

		return otp;
	}

	public boolean validateOtp(String mobileNo, String otp) {
		IMap<String, String> otpMap = otpConfig.getHazelcastInstance().getMap(OTP_CONFIG.otpCache);
		String storedOtp = otpMap.get(mobileNo);

		if (storedOtp != null && storedOtp.equals(otp)) {
			otpMap.remove(mobileNo);
			return true;
		}
		return false;
	}

	// Save user details after OTP validation
	public boolean saveUserDetails(Map<String, String> userDetails) {
		return userRepository.saveUserDetails(userDetails);
	}

	// Captcha generation...
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
}
