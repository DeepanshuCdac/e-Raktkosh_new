package in.cdac.eraktkosh.dto;

import java.util.Map;

public class BloodAvailabilityDTO {

	private String bldgrpcode1;
	private String compId;
	private String componentName;
	private Integer count;
	private String hospitalname;
	private String hospitaladd;
	private String hospitalcontact;
	private String hospitalCode;
	private String moderateStock;
	private String hospitalType;
	private String entrydate;
	private String offline;
	private String type;
	private String available_WithQty;
	private Map<String, Map<String, String>> components;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Map<String, Map<String, String>> getComponents() {
		return components;
	}

	public void setComponents(Map<String, Map<String, String>> components) {
		this.components = components;
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

	private String not_available_WithQty;

	public String getBldgrpcode1() {
		return bldgrpcode1;
	}

	public void setBldgrpcode1(String bldgrpcode1) {
		this.bldgrpcode1 = bldgrpcode1;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

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

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getModerateStock() {
		return moderateStock;
	}

	public void setModerateStock(String moderateStock) {
		this.moderateStock = moderateStock;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}

	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}