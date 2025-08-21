package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.SubscribeDonorDTO;
import in.cdac.eraktkosh.repository.SubscribeDonorRepository;

@Service
public class SubscribeDonorService {

	@Autowired
	private SubscribeDonorRepository subscribeDonorRepository;

	@Autowired
	private EmailService emailService;

	// Register user for subscription
	public String registerUser(SubscribeDonorDTO donorDTO) {
		// Check if email exists in DB and its unsubscribe status
		Integer unsubscribeFlag = subscribeDonorRepository.getUnsubscribeFlagByEmail(donorDTO.getEmail());
		Long serialNo = subscribeDonorRepository.getSerialNoByEmail(donorDTO.getEmail());

		if (unsubscribeFlag != null) {
			if (unsubscribeFlag == 1) {
				// Re-register by updating user details and setting unsubscribe_flag = 0
				int rowsUpdated = subscribeDonorRepository.reSubscribeUser(donorDTO);

				if (rowsUpdated > 0) {
					emailService.sendReRegistrationEmail(donorDTO.getEmail(), serialNo);
					return "Successfully Re-Subscribed with updated details and email sent.";
				}
				return "Error: Re-Subscription Failed";
			} else {
				return "Email already registered";
			}
		}

		// If email does not exist in DB, register as a new user
		int rowsInserted = subscribeDonorRepository.saveUser(donorDTO);

		if (rowsInserted > 0) {
			Long newSerialNo = subscribeDonorRepository.getSerialNoByEmail(donorDTO.getEmail());
			emailService.sendRegistrationEmail(donorDTO.getEmail(), newSerialNo);
			return "Successfully Subscribed and email sent.";
		}

		return "Error: Registration Failed";
	}

	// Unsubscribe user (set unsubscribe_flag = 1)
	public String unsubscribeUser(String email) {
		String response = subscribeDonorRepository.unsubscribeUserByEmail(email);

		if (response.equals("Successfully unsubscribed")) {
			emailService.sendUnsubscriptionEmail(email);
		}

		return response;
	}

	public String getUserEmailBySerialNo(Long serialNo) {
		return subscribeDonorRepository.getEmailBySerialNo(serialNo);
	}

}
