package in.cdac.eraktkosh.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.utility.SendMessageToUser;

@Service
public class OtpServices {

	private ConcurrentHashMap<String, OtpData> otpStore = new ConcurrentHashMap<>();
	private static final String CAPTCHA_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
	private static final int CAPTCHA_LENGTH = 6;
	private static final int CAPTCHA_WIDTH = 200;
	private static final int CAPTCHA_HEIGHT = 50;

	private static class OtpData {
		String otp;
		String captcha;
		long expiresAt;

		OtpData(String otp, String captcha, long expiresAt) {
			this.otp = otp;
			this.captcha = captcha;
			this.expiresAt = expiresAt;
		}
	}

	public static class OtpCaptchaResponse {
		private String captchaImage;
		private String captchaText;
		private String otp;

		public OtpCaptchaResponse(String captchaImage, String captchaText, String otp) {
			this.captchaImage = captchaImage;
			this.captchaText = captchaText;
			this.otp = otp;
		}

		public String getCaptchaImage() {
			return captchaImage;
		}

		public String getCaptchaText() {
			return captchaText;
		}

		public String getOtp() {
			return otp;
		}
	}

	// 🔹 On first time (generate both)
	public OtpCaptchaResponse generateOtpAndCaptcha(String mobileNo) {
		String otp = generateRandomNumber(6);
		String captchaText = generateCaptchaText();
		String captchaImage = generateCaptchaImage(captchaText);

		long expiresAt = System.currentTimeMillis() + 5 * 60 * 1000;
		otpStore.put(mobileNo, new OtpData(otp, captchaText, expiresAt));

		sendOtpSms(mobileNo, otp);

		return new OtpCaptchaResponse(captchaImage, captchaText, otp);
	}

	// 🔹 On "Resend OTP"
	public String resendOtp(String mobileNo) {
		OtpData data = otpStore.get(mobileNo);
		if (data == null || System.currentTimeMillis() > data.expiresAt) {
			otpStore.remove(mobileNo);
			throw new RuntimeException("Session expired. Please refresh captcha and try again.");
		}

		String newOtp = generateRandomNumber(6);
		data.otp = newOtp;
		otpStore.put(mobileNo, data);

		sendOtpSms(mobileNo, newOtp);
		return newOtp;
	}

	// 🔹 On "Refresh CAPTCHA"
	public String refreshCaptcha(String mobileNo) {
		OtpData data = otpStore.get(mobileNo);
		if (data == null || System.currentTimeMillis() > data.expiresAt) {
			otpStore.remove(mobileNo);
			throw new RuntimeException("Session expired. Please resend OTP and try again.");
		}

		String newCaptcha = generateCaptchaText();
		data.captcha = newCaptcha;
		otpStore.put(mobileNo, data);

		return generateCaptchaImage(newCaptcha);
	}

	// 🔹 Validate both OTP and CAPTCHA together
	public boolean validateOtp(String mobileNo, String otp, String captcha) {
		OtpData data = otpStore.get(mobileNo);
		if (data == null)
			return false;

		if (System.currentTimeMillis() > data.expiresAt) {
			otpStore.remove(mobileNo);
			return false;
		}

		boolean valid = data.otp.equals(otp) && data.captcha.equalsIgnoreCase(captcha);
		if (valid)
			otpStore.remove(mobileNo);
		return valid;
	}

	// 🔸 Helper methods

	private void sendOtpSms(String mobileNo, String otp) {
		String maskedMobile = "*******" + mobileNo.substring(mobileNo.length() - 3);
		String msg = "Your eRaktKosh OTP for username " + maskedMobile + " is: " + otp
				+ ". Please do not share your OTP with anyone.";
		SendMessageToUser.SendOTP(msg, mobileNo);
	}

	private String generateRandomNumber(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder otpBuilder = new StringBuilder(length);
		String digits = "0123456789";
		for (int i = 0; i < length; i++) {
			otpBuilder.append(digits.charAt(random.nextInt(digits.length())));
		}
		return otpBuilder.toString();
	}

	private String generateCaptchaText() {
		SecureRandom random = new SecureRandom();
		StringBuilder captcha = new StringBuilder(CAPTCHA_LENGTH);
		for (int i = 0; i < CAPTCHA_LENGTH; i++) {
			captcha.append(CAPTCHA_CHARS.charAt(random.nextInt(CAPTCHA_CHARS.length())));
		}
		return captcha.toString();
	}

	private String generateCaptchaImage(String text) {
		try {
			BufferedImage image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image.createGraphics();

			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
			addNoise(g2d);
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Arial", Font.BOLD, 30));
			g2d.drawString(text, 30, 35);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate CAPTCHA image", e);
		}
	}

	private void addNoise(Graphics2D g2d) {
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < 50; i++) {
			int x1 = random.nextInt(CAPTCHA_WIDTH);
			int y1 = random.nextInt(CAPTCHA_HEIGHT);
			int x2 = random.nextInt(20) - 10 + x1;
			int y2 = random.nextInt(20) - 10 + y1;
			g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			g2d.drawLine(x1, y1, x2, y2);
		}
	}
}
