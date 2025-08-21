package in.cdac.eraktkosh.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.config.EmailConfig;
import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.dto.CampNotificationDTO;
import in.cdac.eraktkosh.dto.NotifyMeDTO;
import in.cdac.eraktkosh.repository.BloodAvailabilityRepository;
import in.cdac.eraktkosh.repository.SubscribeDonorRepository;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailConfig emailConfig;

	@Autowired
	private SubscribeDonorRepository subscribeDonorRepository;

	@Autowired
	private BloodAvailabilityRepository bloodAvailabilityRepo;

//    email template for user those are subscribing themselves for the camp scheduled in their area ....
	public void sendRegistrationEmail(String toEmail, Long serialNo) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(emailConfig.getUsername());
			helper.setTo(toEmail);
			helper.setSubject("Welcome to e-Raktkosh - Registration Successful");

			final String localBaseUrl = "http://10.226.25.103:8080";
			final String uatBaseUrl = "https://uateraktkosh.dcservices.in/beta#/";
			final String prodBaseUrl = "https://eraktkosh.mohfw.gov.in/eraktkoshPortal/#/";

			// Set the active URL base here
			final String baseUrl = localBaseUrl;

			// Unsubscribe link with serialNo
			String unsubscribeLink = baseUrl + "/eraktkosh/subscribe/unsubscribe?email=" + toEmail;

			String emailContent = "<html><body>" + "<h2 style='color: #d9534f;'>Dear Donor,</h2>"
					+ "<p>Thank you for subscribing to <b>e-Raktkosh</b> services!</p>"
					+ "<p>We are excited to have you on board. You'll now receive timely updates about blood donation camps in your selected region.</p>"
					+ "<p style='font-size:14px; color:#555;'>For any assistance, feel free to contact our support team.</p>"
					+ "<hr style='border: 1px solid #d9534f;'>" + "<p><a href='" + unsubscribeLink
					+ "' style='color: red;'>Click here to unsubscribe</a></p>"
					+ "<p style='color: #d9534f; font-size: 12px;'>This is an automated email. Please do not reply.</p>"
					+ "</body></html>";

			helper.setText(emailContent, true);

			logger.info("Sending HTML email to: {}", toEmail);
			mailSender.send(message);
			logger.info("HTML email successfully sent to: {}", toEmail);

		} catch (MailException | MessagingException e) {
			logger.error("Error sending HTML email to {}: {}", toEmail, e.getMessage(), e);
		}
	}

//    email template for the user's those are unsubscribing themselves....
	public void sendUnsubscriptionEmail(String toEmail) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailConfig.getUsername());
			message.setTo(toEmail);
			message.setSubject("Unsubscription Confirmation");
			message.setText(
					"You have been unsubscribed from our notifications. If this was a mistake, you can re-subscribe anytime.");

			logger.info("Sending unsubscription email to: {}", toEmail);
			mailSender.send(message);
			logger.info("Unsubscription email successfully sent to: {}", toEmail);
		} catch (MailException e) {
			logger.error("Error sending unsubscription email to {}: {}", toEmail, e.getMessage());
		}
	}

//    email template for user those who are registering themselves....
	public void sendReRegistrationEmail(String toEmail, Long serialNo) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailConfig.getUsername());
			message.setTo(toEmail);
			message.setSubject("Re-Registration Successful");
			message.setText("Thank you for Subscribing again to e-Raktkosh services.");

			logger.info("Sending email to: {}", toEmail);
			mailSender.send(message);
			logger.info("Email successfully sent to: {}", toEmail);
		} catch (MailException e) {
			logger.error("Error sending email to {}: {}", toEmail, e.getMessage());
		}
	}

//    this is the email template for sending the notification of camp to the subscribed user....
	public void sendCampNotificationEmail(CampNotificationDTO dto) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(emailConfig.getUsername());
			helper.setTo(dto.getEmail());
			helper.setSubject("Upcoming Blood Donation Camp Near You!");

			final String localBaseUrl = "http://10.226.25.103:8080";
			final String uatBaseUrl = "https://uateraktkosh.dcservices.in/beta#/";
			final String prodBaseUrl = "https://eraktkosh.mohfw.gov.in/eraktkoshPortal/#/";

			// Set the active URL base here
			final String baseUrl = localBaseUrl;

			String venueFull = dto.getVenueName() + ", " + dto.getVenueCity();
			String encodedVenue = "";
			try {
				encodedVenue = URLEncoder.encode(venueFull, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("Error encoding venue for Google Maps link: {}", e.getMessage(), e);
			}
			String googleMapsLink = "https://www.google.com/maps/search/?api=1&query=" + encodedVenue;

			String emailContent = "<html><body>" + "<h2 style='color: #d9534f;'>Dear Donor,</h2>"
					+ "<p>We are pleased to inform you about an upcoming <b>Blood Donation Camp</b> in your area:</p>"
					+ "<table style='border-collapse: collapse; font-size: 14px;'>"
					+ "<tr><td><b>Camp Name:</b></td><td>" + dto.getCampName() + "</td></tr>"
					+ "<tr><td><b>Date:</b></td><td>" + dto.getCampDate() + "</td></tr>"
					+ "<tr><td><b>Time:</b></td><td>" + dto.getCampTime() + "</td></tr>"
					+ "<tr><td><b>Venue:</b></td><td>" + venueFull + "</td></tr>"
					+ "<tr><td><b>Contact Person:</b></td><td>" + dto.getMobileNo() + "</td></tr>" + "</table>"
					+ "<p style='margin-top: 10px;'>" + "📍 <a href='" + googleMapsLink
					+ "' target='_blank' style='text-decoration: none; color: #007bff;'>"
					+ "Click here to view the location on Google Maps</a>" + "</p>"
					+ "<p style='margin-top: 15px; font-size: 14px;'>\r\n"
					+ "  🏅 With your participation in the blood donation camp, you will receive a <b>Donation Certificate</b>. \r\n"
					+ "  This certificate can be valuable — if you or your family ever need blood, you are eligible to receive \r\n"
					+ "  <b>1 unit of blood free of cost</b> within 30 days from the date of certificate issuance.\r\n"
					+ "</p>" + "<p>Would you like to join the camp?</p>" + "<p>" + "<a href='" + baseUrl
					+ "/response.html?email=" + dto.getEmail() + "&campReqNo=" + dto.getCampReqNo()
					+ "&isResponse=1' style='text-decoration: none; font-size: 16px;'>" + "👍 Yes</a>&nbsp;&nbsp;&nbsp;"
					+ "<a href='" + baseUrl + "/response.html?email=" + dto.getEmail() + "&campReqNo="
					+ dto.getCampReqNo() + "&isResponse=0' style='text-decoration: none; font-size: 16px;'>"
					+ "👎 No</a>" + "</p>" + "<p>We hope to see you there and appreciate your contribution!</p>"
					+ "<hr style='border: 1px solid #d9534f;'>"
					+ "<p style='font-size: 12px; color: #999;'>This is an automated message. Please do not reply.</p>"
					+ "</body></html>";

			helper.setText(emailContent, true);

			logger.info("Sending camp notification email to: {}", dto.getEmail());
			mailSender.send(message);
			logger.info("Camp notification email successfully sent to: {}", dto.getEmail());

//            donorresponseRepository.insertEmailLog(dto.getEmail(), dto.getCampReqNo());

		} catch (MailException | MessagingException e) {
			logger.error("Error sending camp notification email to {}: {}", dto.getEmail(), e.getMessage(), e);
		}
	}

//    this is the email template for faq section user asking question....
	// Improved email service method
	public void sendUserQuestionToAdmin(String userEmail, String question) {
		try {
			// 1. Send question to admin (HTML format)
			MimeMessage adminMessage = mailSender.createMimeMessage();
			MimeMessageHelper adminHelper = new MimeMessageHelper(adminMessage, true, "UTF-8");

			adminHelper.setFrom(emailConfig.getUsername());
			adminHelper.setTo(emailConfig.getUsername()); // or a specific admin email
			adminHelper.setReplyTo(userEmail); // So "reply" goes to user
			adminHelper.setSubject("New FAQ Question from " + userEmail);

			String adminContent = "<html><body>" + "<h2 style='color: #d9534f;'>New Question Submitted</h2>"
					+ "<p><strong>From:</strong> " + userEmail + "</p>" + "<p><strong>Question:</strong></p>"
					+ "<div style='background: #f5f5f5; padding: 10px; border-left: 4px solid #d9534f;'>" + question
					+ "</div>" + "<p style='margin-top: 20px;'>Click 'Reply' to respond to the user.</p>"
					+ "<hr style='border: 1px solid #eee;'>" + "<p style='color: #999; font-size: 12px;'>"
					+ "This is an automated message. Please reply directly to respond to the user." + "</p>"
					+ "</body></html>";

			adminHelper.setText(adminContent, true);
			mailSender.send(adminMessage);

			// 2. Send confirmation to user
			MimeMessage userMessage = mailSender.createMimeMessage();
			MimeMessageHelper userHelper = new MimeMessageHelper(userMessage, true, "UTF-8");

			userHelper.setFrom(emailConfig.getUsername());
			userHelper.setTo(userEmail);
			userHelper.setSubject("We've received your question");

			String userContent = "<html><body>" + "<h2 style='color: #5bc0de;'>Thank you for your question!</h2>"
					+ "<p>We've received your question and our team will get back to you soon.</p>"
					+ "<div style='background: #f5f5f5; padding: 10px; border-left: 4px solid #5bc0de; margin-bottom: 15px;'>"
					+ "<strong>Your question:</strong><br>" + question + "</div>"
					+ "<p>If you need immediate assistance, please contact us directly.</p>"
					+ "<hr style='border: 1px solid #eee;'>" + "<p style='color: #999; font-size: 12px;'>"
					+ "This is an automated message. Please do not reply." + "</p>" + "</body></html>";

			userHelper.setText(userContent, true);
			mailSender.send(userMessage);

		} catch (Exception e) {
			logger.error("Error sending FAQ emails: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to send FAQ emails", e);
		}
	}

	// Email template to notify user about available blood in their area...
	public boolean sendBloodAvailabilityEmail(String toEmail, String hospitalCode, Integer stateCode) {
		List<BloodAvailabilityDTO> list = bloodAvailabilityRepo.fetchBloodAvailability(stateCode, null, null, null,
				Collections.singletonList(Integer.parseInt(hospitalCode)));

		if (list == null || list.isEmpty()) {
			return false;
		}

		try {
			BloodAvailabilityDTO dto = list.get(0);
			StringBuilder sb = new StringBuilder();
			sb.append("🩸 Blood Availability at ").append(dto.getHospitalname()).append("\n\n");
			sb.append("🏥 Address: ").append(dto.getHospitaladd()).append("\n");
			sb.append("📞 Contact: ").append(dto.getHospitalcontact()).append("\n");
			sb.append("📅 Last Updated: ").append(dto.getEntrydate()).append("\n\n");

//            Map<String, Map<String, String>> components = dto.getComponents();
//            for (Map.Entry<String, Map<String, String>> entry : components.entrySet()) {
//                sb.append("🧪 Component: ").append(entry.getKey()).append("\n");
//                sb.append("✅ Available: ").append(entry.getValue().get("available_WithQty")).append("\n");
//            }

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(emailConfig.getUsername());

			helper.setTo(toEmail);
			message.setSubject("Blood Availability at " + dto.getHospitalname());
			message.setText(sb.toString());

			mailSender.send(message);
			return true;

		} catch (Exception e) {
			System.err.println("Email sending failed: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

//    Email template when the user allows notify_me services...
	public boolean sendRegistrationConfirmationEmail(NotifyMeDTO request, List<String> hospitalNames) {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("🩸 Thank you for registering with eRaktKosh!\n\n");
			sb.append("We will notify you when blood is available in the Blood Centers below:\n\n");
//            sb.append("📍 State Code   : ").append(request.getStateCode()).append("\n");
//            sb.append("📍 District Code: ").append(request.getDistrictCode()).append("\n");
//            sb.append("💉 Blood Group  : ").append(request.getBloodGroupCode()).append("\n\n");

			sb.append("🏥 Selected Blood Banks:\n");
			for (String name : hospitalNames) {
				sb.append("• ").append(name).append("\n");
			}

			sb.append("\n🙏 Thank you for your support.\n");
			sb.append("— eRaktKosh Team");

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(emailConfig.getUsername());
			helper.setTo(request.getEmailId());
			helper.setSubject("eRaktKosh Registration Confirmation");
			helper.setText(sb.toString());

			mailSender.send(message);
			return true;

		} catch (Exception e) {
			System.err.println("Registration email sending failed: " + e.getMessage());
			return false;
		}
	}

	public boolean sendAvailabilityEmail(NotifyMeDTO dto, List<String> hospitalNames) {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("🩸 Blood/Component Availability Notification\n\n");
			sb.append("Hello,\n\n");
			sb.append(
					"The following blood banks/hospitals you registered for now have the required blood/component available:\n\n");

			for (String name : hospitalNames) {
				sb.append("• ").append(name).append("\n");
			}

			sb.append("\nPlease contact the hospital for further details.\n");
			sb.append("\n🙏 Thank you for being with eRaktKosh.\n");
			sb.append("— eRaktKosh Team");

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(emailConfig.getUsername());
			helper.setTo(dto.getEmailId());
			helper.setSubject("Blood/Component Availability Notification - eRaktKosh");
			helper.setText(sb.toString());

			mailSender.send(message);
			return true;

		} catch (Exception e) {
			System.err.println("Availability email sending failed: " + e.getMessage());
			return false;
		}
	}

}
