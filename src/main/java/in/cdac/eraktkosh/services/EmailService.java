package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.config.EmailConfig;
import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.dto.CampNotificationDTO;
import in.cdac.eraktkosh.repository.BloodAvailabilityRepository;
import in.cdac.eraktkosh.repository.SubscribeDonorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
            
        //  Set the active URL base here
            final String baseUrl = localBaseUrl;
            
         // Unsubscribe link with serialNo
            String unsubscribeLink = baseUrl + "/eraktkosh/subscribe/unsubscribe?email=" + toEmail;

            String emailContent = "<html><body>"
                    + "<h2 style='color: #d9534f;'>Dear Donor,</h2>"
                    + "<p>Thank you for subscribing to <b>e-Raktkosh</b> services!</p>"
                    + "<p>We are excited to have you on board. You'll now receive timely updates about blood donation camps in your selected region.</p>"
                    + "<p style='font-size:14px; color:#555;'>For any assistance, feel free to contact our support team.</p>"
                    + "<hr style='border: 1px solid #d9534f;'>"
                    + "<p><a href='" + unsubscribeLink + "' style='color: red;'>Click here to unsubscribe</a></p>"
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
            message.setText("You have been unsubscribed from our notifications. If this was a mistake, you can re-subscribe anytime.");

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
    
//    this is the email template for sneding the notification of camp to the subscribed user....
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
            
         //  Set the active URL base here
            final String baseUrl = localBaseUrl;

            String venueFull = dto.getVenueName() + ", " + dto.getVenueCity();
            String encodedVenue = "";
            try {
                encodedVenue = URLEncoder.encode(venueFull, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("Error encoding venue for Google Maps link: {}", e.getMessage(), e);
            }
            String googleMapsLink = "https://www.google.com/maps/search/?api=1&query=" + encodedVenue;

            String emailContent = "<html><body>"
            	    + "<h2 style='color: #d9534f;'>Dear Donor,</h2>"
            	    + "<p>We are pleased to inform you about an upcoming <b>Blood Donation Camp</b> in your area:</p>"
            	    + "<table style='border-collapse: collapse; font-size: 14px;'>"
            	    + "<tr><td><b>Camp Name:</b></td><td>" + dto.getCampName() + "</td></tr>"
            	    + "<tr><td><b>Date:</b></td><td>" + dto.getCampDate() + "</td></tr>"
            	    + "<tr><td><b>Time:</b></td><td>" + dto.getCampTime() + "</td></tr>"
            	    + "<tr><td><b>Venue:</b></td><td>" + venueFull + "</td></tr>"
            	    + "<tr><td><b>Contact Person:</b></td><td>" + dto.getMobileNo() + "</td></tr>"
            	    + "</table>"
            	    + "<p style='margin-top: 10px;'>"
            	    + "📍 <a href='" + googleMapsLink + "' target='_blank' style='text-decoration: none; color: #007bff;'>"
            	    + "Click here to view the location on Google Maps</a>"
            	    + "</p>"
            	    + "<p style='margin-top: 15px; font-size: 14px;'>\r\n"
            	    + "  🏅 With your participation in the blood donation camp, you will receive a <b>Donation Certificate</b>. \r\n"
            	    + "  This certificate can be valuable — if you or your family ever need blood, you are eligible to receive \r\n"
            	    + "  <b>1 unit of blood free of cost</b> within 30 days from the date of certificate issuance.\r\n"
            	    + "</p>"
            	    + "<p>Would you like to join the camp?</p>"
            	    + "<p>"
            	    + "<a href='" + baseUrl + "/response.html?email=" + dto.getEmail()
            	    + "&campReqNo=" + dto.getCampReqNo()
            	    + "&isResponse=1' style='text-decoration: none; font-size: 16px;'>"
            	    + "👍 Yes</a>&nbsp;&nbsp;&nbsp;"
            	    + "<a href='" + baseUrl + "/response.html?email=" + dto.getEmail()
            	    + "&campReqNo=" + dto.getCampReqNo()
            	    + "&isResponse=0' style='text-decoration: none; font-size: 16px;'>"
            	    + "👎 No</a>"
            	    + "</p>"
            	    + "<p>We hope to see you there and appreciate your contribution!</p>"
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
    public void sendUserQuestionToAdmin(String userEmail, String question) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailConfig.getUsername());
        message.setSubject("New FAQ Question Submitted");
        message.setText(
            "User Email: " + userEmail + "\n\n" +
            "Question:\n" + question + "\n\n" +
            "Please reply to the user's email manually."
        );

        message.setReplyTo(userEmail);

        mailSender.send(message);
    }
    
 // Email template to notify user about available blood in their area...
    public boolean sendBloodAvailabilityEmail(String toEmail, String hospitalCode, Integer stateCode) {
        List<BloodAvailabilityDTO> list = bloodAvailabilityRepo.fetchBloodAvailability(
                stateCode,
                null,
                null,
                null,
                Collections.singletonList(Integer.parseInt(hospitalCode))
        );

        if (list == null || list.isEmpty()) {
            return false;
        }

        try {
            BloodAvailabilityDTO dto = list.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("🩸 Blood Availability at ").append(dto.getHospitalname()).append("\n\n");
            sb.append("🏥 Address: ").append(dto.getHospitaladd()).append("\n");
            sb.append("📞 Contact: ").append(dto.getHospitalcontact()).append("\n");
            sb.append("📅 Entry Date: ").append(dto.getEntrydate()).append("\n\n");

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




}
