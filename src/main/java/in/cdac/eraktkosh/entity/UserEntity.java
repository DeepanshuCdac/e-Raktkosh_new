package in.cdac.eraktkosh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hbbt_portal_donor_dtl")
public class UserEntity {

	@Id
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

	public String getMobileNo() {
		return mobileNo;
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

	public int getBloodGroupCode() {
		return bloodGroupCode;
	}

	public void setBloodGroupCode(int bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(int firstLogin) {
		this.firstLogin = firstLogin;
	}

	public int getDemographics() {
		return demographics;
	}

	public void setDemographics(int demographics) {
		this.demographics = demographics;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public int getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(int districtCode) {
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

	public int getAllBlood() {
		return allBlood;
	}

	public void setAllBlood(int allBlood) {
		this.allBlood = allBlood;
	}

	public int getRepository() {
		return repository;
	}

	public void setRepository(int repository) {
		this.repository = repository;
	}

	public int getRegistrationMode() {
		return registrationMode;
	}

	public void setRegistrationMode(int registrationMode) {
		this.registrationMode = registrationMode;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

}
