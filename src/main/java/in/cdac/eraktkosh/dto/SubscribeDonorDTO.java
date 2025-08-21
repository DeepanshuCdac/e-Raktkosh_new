package in.cdac.eraktkosh.dto;

public class SubscribeDonorDTO {

	private Integer uniqueKey;
	private Integer stateCode;
	private Integer districtCode;
	private Long mobileNo;
	private String email;

	public Integer getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(Integer uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public Integer getStateCode() {
		return stateCode;
	}

	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Integer districtCode) {
		this.districtCode = districtCode;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
