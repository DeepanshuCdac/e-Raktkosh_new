package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import in.cdac.eraktkosh.dto.CampNotificationDTO;
import in.cdac.eraktkosh.repository.CampNotificationRepository;

@Component
public class MyScheduler {

	 @Autowired
	    private CampNotificationRepository repository;

	    @Autowired
	    private EmailService emailService;

	    @Scheduled(fixedRate = 60000) 
//	    @Scheduled(cron = "0 0 8,20 * * ?")
	    public void runCampNotificationScheduler() {
	        List<CampNotificationDTO> campList = repository.getUpcomingCampNotifications();

	        for (CampNotificationDTO camp : campList) {
	            try {
	                emailService.sendCampNotificationEmail(camp);
	                repository.insertLogEntry(camp);
	                System.out.println("Email sent to: " + camp.getEmail());  
	            } catch (Exception e) {
	                System.err.println("Failed to send email to " + camp.getEmail() + ": " + e.getMessage());
	            }
	        }
	    }
	}