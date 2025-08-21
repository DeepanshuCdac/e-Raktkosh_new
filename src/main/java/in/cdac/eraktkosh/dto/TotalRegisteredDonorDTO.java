package in.cdac.eraktkosh.dto;

public class TotalRegisteredDonorDTO {

	private String stateName;
	private int stateCode;
	private int hnumDonorRegistered;

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

	public int getHnumDonorRegistered() {
		return hnumDonorRegistered;
	}

	public void setHnumDonorRegistered(int hnumDonorRegistered) {
		this.hnumDonorRegistered = hnumDonorRegistered;
	}
}
