package in.cdac.eraktkosh.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import in.cdac.eraktkosh.entity.PortalLoginEntity;
import in.cdac.eraktkosh.repository.EraktkoshPortalLoginRepository;
import in.cdac.eraktkosh.utility.SendMessageToUser;

@Service
public class PortalLoginService {
//	@Autowired
	//import com.hazelcast.core.HazelcastInstance;

	@Autowired
	EraktkoshPortalLoginRepository portalDonorRepository;

	@Autowired
	HttpSession session;
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	private static final int OTP_EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes
	private static final int OTP_LENGTH = 6; // Length of the OTP
	private static final String OTP_CHARS = "0123456789"; // OTP characters (numbers only)
	private static final int DAILY_OTP_LIMIT = 5; // Daily OTP limit

	public String generateOtp(String mobile_no) throws InvalidKeyException, NoSuchAlgorithmException {
		JSONObject finalResponse = new JSONObject();

		try {
			// Check if the user exists
			boolean userExists = isUserExists(mobile_no);
//	        String userNotExistMessage = "If you are a Registered User, you will get an OTP.";

			if (!userExists) {
				finalResponse.put("eRaktkosh", false);
				finalResponse.put("notRegisteredMessage", "If you are a Registered User, you will get an OTP.");
				return finalResponse.toString();
			}

			// Get OTP count and last OTP generation timestamp from DB
			int otpCount = Otpcount(mobile_no);
			System.out.println("Current OTP COUNT: " + otpCount);

			if (otpCount >= DAILY_OTP_LIMIT) {
				// Respond with a message indicating daily limit exceeded
				finalResponse.put("isUserExists", true);
				finalResponse.put("limitExceedMessage", "Daily Limit of OTP is Exceeded");
				finalResponse.put("otpCount", otpCount);
				return finalResponse.toString();
			}

			String lastOtpTimestamp = portalDonorRepository.getPreviousOtpTimeStamp(mobile_no);

			// Get the time difference between the last OTP generation and the current time
			long minutes = 0;
			if (lastOtpTimestamp != null) {
				LocalTime lastOtpTime = LocalTime.parse(lastOtpTimestamp);
				LocalTime currentTime1 = LocalTime.now();

				Duration duration = Duration.between(lastOtpTime, currentTime1);
				minutes = duration.toMinutes(); // Get minutes difference
			}

			if (lastOtpTimestamp == null || minutes >= 5) {
				// Generate a random 6-digit OTP
				SecureRandom random = new SecureRandom();
				StringBuilder otp = new StringBuilder(OTP_LENGTH);
				for (int i = 0; i < OTP_LENGTH; i++) {
					int index = random.nextInt(OTP_CHARS.length());
					otp.append(OTP_CHARS.charAt(index));
				}

				String msg = "Your eRaktKosh OTP for username ";
				String contactno = "*******" + mobile_no.substring(mobile_no.length() - 3);
				msg += contactno + " is: ";

				// Increment OTP count in DB before sending the OTP
				portalDonorRepository.insertOtpCount(mobile_no);
				otpCount++; // Update the count in the current context

				// Calculate OTP expiration time (current time + 5 minutes)
				long otpExpirationTime = System.currentTimeMillis() + OTP_EXPIRATION_TIME;

				// Send OTP message to user
				SendMessageToUser.SendOTP(msg + otp + ". Please do not share your OTP with anyone.", mobile_no);

				// Store OTP in Hazelcast with expiration time
				IMap<String, String> otpMap = hazelcastInstance.getMap("otpMap");
				otpMap.put(mobile_no, otp.toString(), OTP_EXPIRATION_TIME, TimeUnit.MINUTES);

				// Build success response
				finalResponse.put("otp", otp.toString());
				finalResponse.put("otpCount", otpCount);
				finalResponse.put("isUserExists", true);
				finalResponse.put("otpExpirationTime", otpExpirationTime);
				finalResponse.put("messageSuccess", "If you are a Registered User, you will get an OTP.");
			} else {
				// Respond if the time difference is less than 5 minutes
				finalResponse.put("isUserExists", true);
				finalResponse.put("errorMessage", "Try After Some time ......!");
				finalResponse.put("otpCount", otpCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
			finalResponse.put("error", "An error occurred while generating OTP.");
		}

		return finalResponse.toString();
	}

	// Simulated method to check if user exists
	public boolean isUserExists(String mobile_no) {
		System.out.println("Checking if user exists");
		return portalDonorRepository.getPortalDonorDtlByMobileNo(mobile_no);
	}

	// Simulated method to return OTP count for a user (from DB)
	public int Otpcount(String mobile_no) {
		return portalDonorRepository.getOtpCount(mobile_no);
	}

	// Simulated method to send OTP to the user
	public void SendMessageToUser(String message, String mobile_no) {
		// Implement actual SMS sending logic
		System.out.println("Sending OTP to: " + mobile_no + " Message: " + message);
	}

	public BufferedImage generateCaptchaImage(String captchaText) {
		int width = 100;
		int height = 40;

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		g2d.setFont(new Font("Roboto", Font.BOLD, 16));

		g2d.setColor(new Color(109, 112, 120));

		int letterSpacing = 5;
		int xPosition = 8;
		int yPosition = 25;

		for (char c : captchaText.toCharArray()) {
			g2d.drawString(String.valueOf(c), xPosition, yPosition);
			xPosition += g2d.getFontMetrics().charWidth(c) + letterSpacing;
		}

		g2d.drawLine(20, yPosition - 10, xPosition - letterSpacing, yPosition - 10);
		g2d.dispose();

		return bufferedImage;
	}

	public String generateCaptchaText() {
		Random random = new Random();
		String captchaChars = "abcdefghijklmnopqrstuvwxyz#$#!&*^ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder captchaText = new StringBuilder();

		// Generate a random 6-character captcha
		for (int i = 0; i < 6; i++) {
			captchaText.append(captchaChars.charAt(random.nextInt(captchaChars.length())));
		}

		// Store the captcha in Hazelcast without an expiration time
		IMap<String, String> captchaMap = hazelcastInstance.getMap("captchaMap");

		captchaMap.put(captchaText.toString(), captchaText.toString());

		return captchaText.toString();
	}

	public PortalLoginEntity fetchdetailsCamp(PortalLoginEntity PortalLoginEntity) {

		System.out.println("Inside fetch User camp details");
		PortalLoginEntity res = portalDonorRepository.fetchdetailsCamp(PortalLoginEntity);
		System.out.println("OTP COUNT" + res);
		return res;

	}

	public int getPreviousOtpTimestampFromDB(PortalLoginEntity PortalLoginEntity) {

		return 0;

	}

	public ResponseEntity<?> validate(String otp, String captcha, String mobileno) {
		System.out.println(session.getId());

		// Get the OTP and CAPTCHA maps from Hazelcast
		IMap<String, String> otpMap = hazelcastInstance.getMap("otpMap");
		IMap<String, String> captchaMap = hazelcastInstance.getMap("captchaMap");

		// Retrieve stored OTP and CAPTCHA from Hazelcast
		String storedOtp = otpMap.get(mobileno);
		String storedCaptcha = captchaMap.get(captcha); // Make sure you use the same key to retrieve the CAPTCHA

		// Validate OTP
		if (storedOtp == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP not found for the given mobile number.");
		}
		if (!storedOtp.equals(otp)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
		}

		// Validate CAPTCHA
		if (storedCaptcha == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CAPTCHA not found for the given mobile number.");
		}
		if (!storedCaptcha.equals(captcha)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid CAPTCHA.");
		}

		// If both OTP and CAPTCHA are valid, fetch user details
		return fetchUserDetails(mobileno);
	}

	// New method to fetch user details
	public ResponseEntity<?> fetchUserDetails(String mobileno) {
		PortalLoginEntity portalLoginEntity = portalDonorRepository.fetchDonorDetails(mobileno);

		// Check if user details are found
		if (portalLoginEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}

		// Log user details
		System.out.println(portalLoginEntity.getEdonorLName());
		System.out.println(portalLoginEntity.getEdonorFName());
		System.out.println(portalLoginEntity.getMobileno() + " This is donor Number");

		
		
		// Return user details
		return new ResponseEntity<>(portalLoginEntity, HttpStatus.OK);
	}

	// New service: Fetch previous donation details by mobile number
	public ResponseEntity<?> fetchPreviousDonationDetails(String mobileNo) {
		try {
			List<PortalLoginEntity> donationDetails = portalDonorRepository.getPrevDonationDetailsByMobile(mobileNo);

			if (donationDetails == null || donationDetails.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No donation details found for mobile number " + mobileNo);
			}

			return new ResponseEntity<>(donationDetails, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching previous donation details.");
		}
	}
	
	
//	update donor details in manage profile section....
	public boolean updateOrInsertDonorDetails(PortalLoginEntity portalLoginEntity) throws Exception {
        try {
//        	System.out.println("bada sheer::"+portalLoginEntity.getEdonorFName());
        	portalDonorRepository.updateDonorDetails(portalLoginEntity);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating or inserting donor details: " + e.getMessage());
            throw e; 
        }
    }
}