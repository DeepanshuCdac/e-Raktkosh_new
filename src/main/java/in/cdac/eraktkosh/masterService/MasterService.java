package in.cdac.eraktkosh.masterService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.StateWithDistricts;
import in.cdac.eraktkosh.masterEntity.BloodGroupEntity;
import in.cdac.eraktkosh.masterEntity.ComponentListEntity;
import in.cdac.eraktkosh.masterEntity.District;
import in.cdac.eraktkosh.masterEntity.GenderEntity;
import in.cdac.eraktkosh.masterEntity.MaritalStatusEntity;
import in.cdac.eraktkosh.masterEntity.OccupationEntity;
import in.cdac.eraktkosh.masterEntity.ReligionEntity;
import in.cdac.eraktkosh.masterEntity.State;
import in.cdac.eraktkosh.masterRepository.MasterRepository;

@Service
public class MasterService {
	
	@Autowired
	private MasterRepository masterRepo;
	
//	Marriage Status Service ... starts 
	 public MarriageResponse getAllMaritalStatus() {
	        List<MaritalStatusEntity> maritalStatus = masterRepo.findAllMaritalStatus();
	        return new MarriageResponse(maritalStatus);
	    }

	    public static class MarriageResponse {
	        private List<MaritalStatusEntity> MaritalStatus;

	        public MarriageResponse(List<MaritalStatusEntity> maritalStatus) {
	            this.MaritalStatus = maritalStatus;
	        }

	        public List<MaritalStatusEntity> getMaritalStatus() {
	            return MaritalStatus;
	        }

	        public void setMaritalStatus(List<MaritalStatusEntity> maritalStatus) {
	            this.MaritalStatus = maritalStatus;
	        }
	    }
//	   Marriage Status service --- ends
	    
//	    gender service -- starts --
	    public GenderResponse getAllGenders() {
	        List<GenderEntity> genders = masterRepo.findAllGenders();
	        return new GenderResponse(genders);
	    }

	    public static class GenderResponse {
	        private List<GenderEntity> Genders;

	        public GenderResponse(List<GenderEntity> genders) {
	            this.Genders = genders;
	        }

	        public List<GenderEntity> getGenders() {
	            return Genders;
	        }

	        public void setGenders(List<GenderEntity> genders) {
	            this.Genders = genders;
	        }
	    }
//	   gender service -- ends --
	    
//	    occupation service -- starts --
	    public OccupationResponse getAllOccupation() {
	        List<OccupationEntity> occupation = masterRepo.findAllOccupation();
	        return new OccupationResponse(occupation);
	    }

	    public static class OccupationResponse {
	        private List<OccupationEntity> Occupation;

	        public OccupationResponse(List<OccupationEntity> occupation) {
	            this.Occupation = occupation;
	        }

	        public List<OccupationEntity> getOccupation() {
	            return Occupation;
	        }

	        public void setOccupation(List<OccupationEntity> occupation) {
	            this.Occupation = occupation;
	        }
	    }
//	    occupation service -- ends --
	    
//	    religion service -- starts --
	    public ReligionResponse getAllReligion() {
	        List<ReligionEntity> religion = masterRepo.findAllReligion();
	        return new ReligionResponse(religion);
	    }

	    public static class ReligionResponse {
	        private List<ReligionEntity> Religion;

	        public ReligionResponse(List<ReligionEntity> religion) {
	            this.Religion = religion;
	        }

	        public List<ReligionEntity> getReligion() {
	            return Religion;
	        }

	        public void setReligion(List<ReligionEntity> religion) {
	            this.Religion = religion;
	        }
	    }
//	    religion service -- ends --
	    
//	    bloodGroup service -- starts --
	    public BloodGroupResponse getAllBloodGroup() {
	        List<BloodGroupEntity> bloodGroup= masterRepo.findAllBloodGroup();
	        return new BloodGroupResponse(bloodGroup);
	    }

	    public static class BloodGroupResponse {
	        private List<BloodGroupEntity> BloodGroup;

	        public BloodGroupResponse(List<BloodGroupEntity> bloodGroup) {
	            this.BloodGroup = bloodGroup;
	        }

	        public List<BloodGroupEntity> getBloodGroup() {
	            return BloodGroup;
	        }

	        public void setBloodGroup(List<BloodGroupEntity> bloodGroup) {
	            this.BloodGroup = bloodGroup;
	        }
	    }
//	    bloodGroup service -- ends --
	    
//	    componentList service -- starts --
	    public ComponentListResponse getAllComponentList(int hospitalCode) {
	        List<ComponentListEntity> componentList= masterRepo.findAllComponentList(hospitalCode);
	        return new ComponentListResponse(componentList);
	    }

	    public static class ComponentListResponse {
	        private List<ComponentListEntity> ComponentList;

	        public ComponentListResponse(List<ComponentListEntity> componentList) {
	            this.ComponentList = componentList;
	        }

	        public List<ComponentListEntity> getComponentList() {
	            return ComponentList;
	        }

	        public void setComponentList(List<ComponentListEntity> componentList) {
	            this.ComponentList = componentList;
	        }
	    }
//	    componentList service -- ends --
	    
//	    state service -- starts --
	    public List<State> getAllStates() {
	        return masterRepo.findAllValidStates();
	    }
//	    state service -- ends --
	    
//	    District service -- starts --
	    public List<District> getDistrictsByStateCode(Integer stateCode) {
	        return masterRepo.findDistrictsByStateCode(stateCode);
	    }
//	    District service -- ends --
	    
//	    StateWithDistricts service -- starts --
	    public List<StateWithDistricts> getAllStatesWithDistricts() {
	        List<State> states = masterRepo.findAllValidStates();
	        return states.stream().map(state -> {
	            List<District> districts = masterRepo.findDistrictsByStateCode(state.getStateCode());
	            return new StateWithDistricts(state.getStateCode(), state.getStateName(), districts);
	        }).collect(Collectors.toList());
	    }
//	    StateWithDistricts service -- ends --
}
