package in.cdac.eraktkosh.dto;

public class BloodAvailabilityResponseDTO {

	private String hospitalname;
	private String hospitaladd;
	private String hospitalcontact;
	private String hospitalType;
	private String hospitalCode;
	private String available;
	private String not_available;
	private String moderate;
	private String lastUpdate;
	private String currentStatus;
	private String available_WithQty;
	private String not_available_WithQty;

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getHospitaladd() {
		return hospitaladd;
	}

	public void setHospitaladd(String hospitaladd) {
		this.hospitaladd = hospitaladd;
	}

	public String getHospitalcontact() {
		return hospitalcontact;
	}

	public void setHospitalcontact(String hospitalcontact) {
		this.hospitalcontact = hospitalcontact;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getNot_available() {
		return not_available;
	}

	public void setNot_available(String not_available) {
		this.not_available = not_available;
	}

	public String getModerate() {
		return moderate;
	}

	public void setModerate(String moderate) {
		this.moderate = moderate;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getAvailable_WithQty() {
		return available_WithQty;
	}

	public void setAvailable_WithQty(String available_WithQty) {
		this.available_WithQty = available_WithQty;
	}

	public String getNot_available_WithQty() {
		return not_available_WithQty;
	}

	public void setNot_available_WithQty(String not_available_WithQty) {
		this.not_available_WithQty = not_available_WithQty;
	}

}
