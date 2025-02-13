package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gblt_religion_mst")
public class ReligionEntity {
	
	@Id
	private Integer religionCode;
	private String religionName;
	
	public Integer getReligionCode() {
		return religionCode;
	}
	public void setReligionCode(Integer religionCode) {
		this.religionCode = religionCode;
	}
	public String getReligionName() {
		return religionName;
	}
	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}
	

}
