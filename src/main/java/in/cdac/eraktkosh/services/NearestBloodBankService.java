package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.NearestBloodBank;
import in.cdac.eraktkosh.repository.NearestBloodBankRepository;

@Service
public class NearestBloodBankService {

	@Autowired
	private NearestBloodBankRepository nearestBloodBankRepo;

	public List<NearestBloodBank> getNearestHospitals(int stateCode, int districtCode) {

		return nearestBloodBankRepo.findNearestHospitals(stateCode, districtCode);
	}

}
