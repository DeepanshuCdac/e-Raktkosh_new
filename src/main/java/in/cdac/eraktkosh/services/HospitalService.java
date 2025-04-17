package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.HospitalDTO;
import in.cdac.eraktkosh.repository.HospitalRepository;

@Service
public class HospitalService {
	
	@Autowired
    private HospitalRepository hospitalRepository;

    public List<HospitalDTO> getHospitalsByStateAndDistrict(int stateCode, int districtId) {
        return hospitalRepository.findByStateAndDistrict(stateCode, districtId);
    }

}
