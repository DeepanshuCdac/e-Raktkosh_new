package in.cdac.eraktkosh.dto;

public class TotalBloodCollectedDTO {

	private String stateName;
	private int totalCollection;
	private int stateCode;

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getTotalCollection() {
		return totalCollection;
	}

	public void setTotalCollection(int totalCollection) {
		this.totalCollection = totalCollection;
	}
}
