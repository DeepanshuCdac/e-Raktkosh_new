package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.DonorResponseDTO;
import in.cdac.eraktkosh.repository.DonorResponseRepository;

@Service
public class DonorResponseService {

	@Autowired
	private DonorResponseRepository repository;

	public void handleDonorResponse(DonorResponseDTO dto) {
		repository.updateResponse(dto.getEmail(), dto.getCampReqNo(), dto.getIsResponse());
	}
}
