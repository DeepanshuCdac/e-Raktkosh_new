package in.cdac.eraktkosh.entity;

import javax.persistence.Entity;

@Entity
public class RegisterEntity {
	private String mobileNo;
	private String firstName;
	private String lastName;
	private Integer bloodGroupCode;
	private String password;
	private Integer firstLogin;
	private Integer demographics;
	private Integer isValid;
	private Integer stateCode;
	private Integer districtCode;
	private String pinCode;
	private String emailId;
	private String genderCode;
	private String userId;
	private String address;
	private Integer allBlood;
	private Integer repository;
	private Integer registrationMode;
	private String fatherName;
	private String dob;

	// Getters and Setters
	public String getMobileNo() {
		return mobileNo;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getBloodGroupCode() {
		return bloodGroupCode;
	}

	public void setBloodGroupCode(Integer bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Integer firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Integer getDemographics() {
		return demographics;
	}

	public void setDemographics(Integer demographics) {
		this.demographics = demographics;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
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

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAllBlood() {
		return allBlood;
	}

	public void setAllBlood(Integer allBlood) {
		this.allBlood = allBlood;
	}

	public Integer getRepository() {
		return repository;
	}

	public void setRepository(Integer repository) {
		this.repository = repository;
	}

	public Integer getRegistrationMode() {
		return registrationMode;
	}

	public void setRegistrationMode(Integer registrationMode) {
		this.registrationMode = registrationMode;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
}
