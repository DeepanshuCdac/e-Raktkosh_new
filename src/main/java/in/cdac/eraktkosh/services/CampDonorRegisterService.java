package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.CampDonorRegisterDTO;
import in.cdac.eraktkosh.repository.CampDonorRegisterRepository;

@Service
public class CampDonorRegisterService {

	@Autowired
	private CampDonorRegisterRepository donorRepository;

	public List<CampDonorRegisterDTO> getDonorDetails(String mobile) {
		return donorRepository.getDonorsByMobile(mobile);
	}

}
