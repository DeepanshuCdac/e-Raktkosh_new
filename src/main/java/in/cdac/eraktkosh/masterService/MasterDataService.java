// in use for all in one api call...

package in.cdac.eraktkosh.masterService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.MasterDataResponse;
import in.cdac.eraktkosh.dto.StateWithDistricts;
import in.cdac.eraktkosh.masterEntity.District;
import in.cdac.eraktkosh.masterRepository.MasterRepository;

@Service
public class MasterDataService {

	@Autowired
	private MasterRepository masterRepo;

//    service for getting all the data of master...
	public MasterDataResponse getAllMasterData(Integer hospitalCode) {
		MasterDataResponse response = new MasterDataResponse();

		List<StateWithDistricts> statesWithDistricts = masterRepo.findAllValidStates().stream().map(state -> {
			List<District> districts = masterRepo.findDistrictsByStateCode(state.getStateCode());
			return new StateWithDistricts(state.getStateCode(), state.getStateName(), districts);
		}).collect(Collectors.toList());

		response.setStatesWithDistricts(statesWithDistricts);

		response.setGenders(masterRepo.findAllGenders());
		response.setMaritalStatus(masterRepo.findAllMaritalStatus());
		response.setReligion(masterRepo.findAllReligion());
		response.setOccupations(masterRepo.findAllOccupation());
		response.setBloodGroups(masterRepo.findAllBloodGroup());
		response.setComponentList(masterRepo.findAllComponentList(hospitalCode));

		return response;
	}

//    service for blood availability...
	public String getStock(int stateCode, int districtCode, int bloodGroup, int bloodComponent, int showStock,
			int lang) {
		return null;

	}
}
