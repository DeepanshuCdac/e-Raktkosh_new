package in.cdac.eraktkosh.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NotifyMeDTO {

	@NotNull
	private Integer stateCode;

	@NotNull
	private Integer districtCode;

	@NotNull
	private Integer bloodGroupCode;

	private Integer bloodComponentId;

	private Long mobileNo;

	@NotNull
	@Email
	private String emailId;

	@Size(min = 1, max = 5)
	private List<Integer> hospitalCodes;

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

	public Integer getBloodGroupCode() {
		return bloodGroupCode;
	}

	public void setBloodGroupCode(Integer bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<Integer> getHospitalCodes() {
		return hospitalCodes;
	}

	public void setHospitalCodes(List<Integer> hospitalCodes) {
		this.hospitalCodes = hospitalCodes;
	}

}
