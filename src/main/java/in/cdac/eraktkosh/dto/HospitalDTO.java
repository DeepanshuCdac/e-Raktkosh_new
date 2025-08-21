package in.cdac.eraktkosh.dto;

public class HospitalDTO {

	private int hospitalCode;
	private String hospitalName;
	private long loginMobile;

	public HospitalDTO(int hospitalCode, String hospitalName, long loginMobile) {
		this.hospitalCode = hospitalCode;
		this.hospitalName = hospitalName;
		this.loginMobile = loginMobile;
	}

	public int getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(int hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public long getLoginMobile() {
		return loginMobile;
	}

	public void setLoginMobile(long loginMobile) {
		this.loginMobile = loginMobile;
	}

}
