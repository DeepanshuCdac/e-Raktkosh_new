package in.cdac.eraktkosh.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.repository.BloodAvailabilityRepository;

@Service
public class BloodAvailabilityService {

	@Autowired
	private BloodAvailabilityRepository bloodAvailabilityRepository;

	public List<BloodAvailabilityDTO> fetchBloodAvailability(Integer stateCode, Integer districtId, Integer componentId,
			Integer bloodGroupId, List<Integer> hospitalCodes) {

		return bloodAvailabilityRepository.fetchBloodAvailability(stateCode, districtId, componentId, bloodGroupId,
				hospitalCodes);
	}
}
