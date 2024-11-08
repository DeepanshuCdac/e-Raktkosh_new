package in.cdac.eraktkosh.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//import com.hazelcast.core.HazelcastInstance;

import in.cdac.eraktkosh.entity.PortalLoginEntity;
import in.cdac.eraktkosh.repository.EraktkoshPortalLoginRepository;
import in.cdac.eraktkosh.utility.SendMessageToUser;

@Service
public class PortalLoginService {
//	@Autowired
//    private HazelcastInstance hazelcastInstance;

	@Autowired
	EraktkoshPortalLoginRepository portalDonorRepository;

	@Autowired
	HttpSession session;
	@Autowired
	private HazelcastInstance hazelcastInstance;

	private final String OTP_CHARS = "0123456789";
	private final int OTP_LENGTH = 6;
	private static final int OTP_EXPIRATION_TIME = 5 * 60 * 1000;

	private Map<String, Integer> otpCountStore = new HashMap<>(); // to store otpCount for each user
	private Map<String, Long> otpTimestampStore = new HashMap<>();

	public String generateOtp(String mobile_no) throws InvalidKeyException, NoSuchAlgorithmException {
	    JSONObject finalResponse = new JSONObject();

	    try {
	        // Check if the user exists first
	        boolean userExists = isUserExists(mobile_no);
	        String userNotExistMessage = "If you are a Registered User you will get an OTP.";

	        // If the user does not exist, respond with an appropriate message
	        if (!userExists) {
	            finalResponse.put("isUserExists", false);
	            finalResponse.put("messageSuccess", userNotExistMessage);
	            return finalResponse.toString();
	        }

	        // Generate a random 6-digit OTP only if the user exists
	        SecureRandom random = new SecureRandom();
	        StringBuilder otp = new StringBuilder(OTP_LENGTH);
	        for (int i = 0; i < OTP_LENGTH; i++) {
	            int index = random.nextInt(OTP_CHARS.length());
	            otp.append(OTP_CHARS.charAt(index));
	        }

	        String msg = "Your eRaktKosh OTP for username ";
	        String contactno = "*******" + mobile_no.substring(mobile_no.length() - 3);
	        msg += contactno + " is: ";

	        int otpCount = Otpcount(mobile_no);
	        long currentTime = System.currentTimeMillis();
	        Long lastOtpTimestamp = otpTimestampStore.get(mobile_no);
	        String successMessage = "If you are a Registered User you will get an OTP.";
	        String errorMessage = "Try After Some time ......!";

	        // Check if OTP should be generated based on timing and count
	        if (otpCount == 0 || (lastOtpTimestamp != null && currentTime - lastOtpTimestamp >= OTP_EXPIRATION_TIME)) {
	            // Update OTP count and timestamp if eligible for a new OTP
	            otpCount++;
	            otpCountStore.put(mobile_no, otpCount);
	            otpTimestampStore.put(mobile_no, currentTime);

	            // Calculate OTP expiration time (current time + 5 minutes)
	            long otpExpirationTime = currentTime + OTP_EXPIRATION_TIME;

	            // Send OTP message
	            SendMessageToUser.SendOTP(msg + otp + ". Please do not share your OTP with anyone.", mobile_no);

	            // Store OTP in Hazelcast with expiration
	            IMap<String, String> otpMap = hazelcastInstance.getMap("otpMap");
	            otpMap.put(mobile_no, otp.toString(), OTP_EXPIRATION_TIME, TimeUnit.MINUTES);

	            // Build success response
	            finalResponse.put("otp", otp.toString());
	            finalResponse.put("otpCount", otpCount);
	            finalResponse.put("isUserExists", true);
	            finalResponse.put("otpExpirationTime", otpExpirationTime);
	            finalResponse.put("messageSuccess", successMessage);
	        } else {
	            // Respond if OTP was generated recently
	            finalResponse.put("errorMessage", errorMessage);
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


	// Simulated method to return OTP count for a user (from cache or DB)
	public int Otpcount(String mobile_no) {
		// Check if the mobile number has a stored otpCount
		return otpCountStore.getOrDefault(mobile_no, 0);
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
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No donation details found for this mobile number.");
	            }
	            
	            
	            return new ResponseEntity<>(donationDetails, HttpStatus.OK);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching previous donation details.");
	        }
	    }
}