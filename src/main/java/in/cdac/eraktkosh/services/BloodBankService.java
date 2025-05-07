package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.BloodBankDTO;
import in.cdac.eraktkosh.repository.BloodBankRepository;

@Service
public class BloodBankService {

	 @Autowired
	    private BloodBankRepository repository;

	    public List<BloodBankDTO> getBloodBanksByStateAndDistrict(int stateCode, int districtCode) {
	        return repository.getBloodBankList(stateCode, districtCode);
	    }
}
