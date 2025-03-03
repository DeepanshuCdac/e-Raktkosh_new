package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.CampDetailDTO;
import in.cdac.eraktkosh.repository.CampDetailRepository;

@Service
public class CampDetailService {
	
	@Autowired
    private CampDetailRepository campDetailRepository;

    public List<CampDetailDTO> getCamps(int stateCode, Integer districtCode, String campDate) {
        return campDetailRepository.fetchCamps(stateCode, districtCode, campDate);
    }
}
