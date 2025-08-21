package in.cdac.eraktkosh.services;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.NotifyMeDTO;
import in.cdac.eraktkosh.repository.NotifyMeRepository;

@Service
public class NotifyMeService {

	@Autowired
	private NotifyMeRepository repository;

	@Autowired
	private EmailService emailService;

	public String insertNotification(NotifyMeDTO request) throws SQLException {

		// 1. Validate hospital codes
		if (request.getHospitalCodes() == null || request.getHospitalCodes().isEmpty()
				|| request.getHospitalCodes().size() > 5) {
			return "Hospital codes must contain at least 1 and at most 5 codes.";
		}

		// 2. Convert hospital code list to SQL Array once
		Array hospitalCodeArray = repository.getHospitalCodeArray(request.getHospitalCodes());

		// 3. Check if same email with exact parameters already exists
		List<Map<String, Object>> existing = repository.findExactMatch(request.getEmailId(), request.getStateCode(),
				request.getDistrictCode(), request.getBloodGroupCode(), request.getBloodComponentId(),
				request.getMobileNo(), hospitalCodeArray);

		if (!existing.isEmpty()) {
			int olderThan3Days = repository.isEntryOlderThanThreeDays(request.getEmailId(), request.getStateCode(),
					request.getDistrictCode(), request.getBloodGroupCode(), request.getBloodComponentId(),
					request.getMobileNo(), hospitalCodeArray);

			if (olderThan3Days == 0) {
				return "Email already registered with same data recently.";
			}
		}

		// 4. Insert new notification
		repository.insertNotification(request);

		// 5. Fetch hospital names for email
		List<String> hospitalNames = repository.getHospitalNames(request.getHospitalCodes());

		// 6. Send confirmation email
		boolean emailSent = emailService.sendRegistrationConfirmationEmail(request, hospitalNames);

		// 7. Update notify flag if email sent successfully
		if (emailSent) {
			repository.updateNotifyFlagToSent(request.getEmailId(), request.getHospitalCodes(), request.getStateCode(),
					request.getDistrictCode());
			return "Notification Saved and Verified Successfully and Email Sent Successfully!!";
		}

		return "Notification saved, but email failed to send.";
	}
}
