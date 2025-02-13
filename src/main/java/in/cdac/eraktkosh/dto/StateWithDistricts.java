package in.cdac.eraktkosh.dto;

import java.util.List;

import in.cdac.eraktkosh.masterEntity.District;

public class StateWithDistricts {
	
	   private Integer stateCode;
	    private String stateName;
	    private List<District> districts;
	    
	    public StateWithDistricts(Integer stateCode, String stateName, List<District> districts) {
	        this.stateCode = stateCode;
	        this.stateName = stateName;
	        this.districts = districts;
	    }
	    
	    public Integer getStateCode() {
	        return stateCode;
	    }

	    public void setStateCode(Integer stateCode) {
	        this.stateCode = stateCode;
	    }

	    public String getStateName() {
	        return stateName;
	    }

	    public void setStateName(String stateName) {
	        this.stateName = stateName;
	    }

	    public List<District> getDistricts() {
	        return districts;
	    }

	    public void setDistricts(List<District> districts) {
	        this.districts = districts;
	    }

}
