package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import in.cdac.eraktkosh.dto.CampNotificationDTO;
import in.cdac.eraktkosh.repository.CampNotificationRepository;
import in.cdac.eraktkosh.repository.NotifyMeRepository;

@Component
public class MyScheduler {

	@Autowired
	private CampNotificationRepository repository;

	@Autowired
	private NotifyMeRepository notifyrepository;

	@Autowired
	private EmailService emailService;

	// 🕗 every 2 minutes - Camp notification scheduler
	@Scheduled(cron = "0 */2 * * * ?")
	public void runCampNotificationScheduler() {
		List<CampNotificationDTO> campList = repository.getUpcomingCampNotifications();

		for (CampNotificationDTO camp : campList) {
			try {
				emailService.sendCampNotificationEmail(camp);
				repository.insertLogEntry(camp);
				System.out.println("[CampNotify] Email sent to: " + camp.getEmail());
			} catch (Exception e) {
				System.err.println("[CampNotify] Failed to send email to " + camp.getEmail() + " : " + e.getMessage());
			}
		}
	}

	// 🕐 Every hour - Notify blood availability
//	@Scheduled(cron = "0 */2 * * * ?")
//	public void runNotificationCheck() throws SQLException {
//		List<Map<String, Object>> pendingNotifications = notifyrepository.getPendingNotificationsWithin3Days();
//
//		for (Map<String, Object> record : pendingNotifications) {
//			
//			
//			System.out.println(record);
//			
//			NotifyMeDTO dto = mapToDTO(record);
//
//			try {
//				List<Integer> matchedHospitals = notifyrepository.findHospitalsWithRequiredBlood(dto);
//
//				// Exclude already notified hospitals
//				List<Integer> hospitalsToNotify = matchedHospitals.stream()
//						.filter(hospId -> !notifyrepository.hasAlreadyNotifiedToday(dto.getEmailId(), hospId))
//						.collect(Collectors.toList());
//
//				if (!hospitalsToNotify.isEmpty()) {
//					List<String> hospitalNames = notifyrepository.getHospitalNames(hospitalsToNotify); 
//					boolean sent = emailService.sendAvailabilityEmail(dto, hospitalNames);
//
//					if (sent) {
//						notifyrepository.logEmailNotifications(dto.getEmailId(), hospitalsToNotify);
//						System.out.println("[BloodNotify] Email sent to: " + dto.getEmailId() + " for hospitals: "
//								+ hospitalsToNotify);
//					} else {
//						System.err.println("[BloodNotify] Failed to send email to: " + dto.getEmailId());
//					}
//				}
//			} catch (Exception e) {
//				System.err.println(
//						"[BloodNotify] Error processing record for " + dto.getEmailId() + ": " + e.getMessage());
//
//			}
//		}
//	}	
//
//	private NotifyMeDTO mapToDTO(Map<String, Object> record) {
//		NotifyMeDTO dto = new NotifyMeDTO();
//		
//		System.out.println(record.get("hbnum_state_code"));
//		
//		dto.setEmailId((String) record.get("hbstr_email_id"));
//		dto.setStateCode((Integer) record.get("hbnum_state_code"));
//		dto.setDistrictCode((Integer) record.get("hbnum_district_code"));
//		dto.setBloodGroupCode((Integer) record.get("hbnum_blood_group_code"));
//		dto.setBloodComponentId((Integer) record.get("hbnum_component_id"));
//		dto.setMobileNo((Long) record.get("hbnum_mobile_no"));
//
//		@SuppressWarnings("unchecked")
//		List<Integer> hospitalCodes = (List<Integer>) record.get("hbnum_hosp_code");
//		dto.setHospitalCodes(hospitalCodes);
//
//		return dto;
//	}
}
