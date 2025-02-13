package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gblt_marital_status_mst")
public class MaritalStatusEntity {
	
	@Id
	private Integer maritalStatusCode;
	private String maritalStatusName;
	
	public Integer getMaritalStatusCode() {
		return maritalStatusCode;
	}
	public void setMaritalStatusCode(Integer maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}
	public String getMaritalStatusName() {
		return maritalStatusName;
	}
	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}
	
	

}
