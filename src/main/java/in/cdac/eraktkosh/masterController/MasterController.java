package in.cdac.eraktkosh.masterController;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.StateWithDistricts;
import in.cdac.eraktkosh.masterEntity.District;
import in.cdac.eraktkosh.masterEntity.State;
import in.cdac.eraktkosh.masterService.MasterService;
import in.cdac.eraktkosh.masterService.MasterService.BloodGroupResponse;
import in.cdac.eraktkosh.masterService.MasterService.ComponentListResponse;
import in.cdac.eraktkosh.masterService.MasterService.GenderResponse;
import in.cdac.eraktkosh.masterService.MasterService.MarriageResponse;
import in.cdac.eraktkosh.masterService.MasterService.OccupationResponse;
import in.cdac.eraktkosh.masterService.MasterService.ReligionResponse;

@RestController
@RequestMapping("/eraktkosh")
public class MasterController {
	
	@Autowired
	private MasterService masterService;
	
//	Marriage Status Controller -- starts ---
	 @PostMapping("/marriageStatus")
	    public MarriageResponse getMaritalStatus() {
	        return masterService.getAllMaritalStatus();
	    }
//	 marriage status controller -- ends --
	 
//	 gender controller -- starts --
	   @PostMapping("/genders")
	    public GenderResponse getGenders() {
	        return masterService.getAllGenders();
	    }
//	  gender controller -- ends --
	   
//	  occupation controller -- starts --
	   @PostMapping("/occupations")
	    public OccupationResponse getOccupation() {
	        return masterService.getAllOccupation();
	    }
//	  occupation controller -- ends --
	   
//		  religion controller -- starts --
		   @PostMapping("/religion")
		    public ReligionResponse getReligion() {
		        return masterService.getAllReligion();
		    }
//		religion controller -- ends --
		   
//		BloodGroup controller -- starts --
			   @PostMapping("/bloodGroup")
			    public BloodGroupResponse getBloodGroup() {
			        return masterService.getAllBloodGroup();
			    }
//		BloodGroup controller -- ends --
			   
//		componentList controller -- starts --
			   @PostMapping("/componentList")
			   public ComponentListResponse getComponentList(@RequestBody Map<String, Integer> request) {
			       int hospitalCode = request.get("hospitalCode");
			       return masterService.getAllComponentList(hospitalCode);
			   }

//	    componentList controller -- ends --
	   
//	  state controller -- starts --
		@PostMapping("/states")
		public List<State> getStates() {
	        return masterService.getAllStates(); 
	    }
//	 state controller -- ends --
		
//	 districts controller -- starts --
		 @PostMapping("/districts/{stateCode}")
		    public List<District> getDistrictsByStateCode(@PathVariable Integer stateCode) {
		        return masterService.getDistrictsByStateCode(stateCode);
		    }
//	 districts controller -- ends --
		 
//		 states-with-districts controller -- starts --	 
		 @PostMapping("/states-with-districts")
		    public List<StateWithDistricts> getStatesWithDistricts() {
		        return masterService.getAllStatesWithDistricts();
		    }
//		 districts controller -- ends --
}
