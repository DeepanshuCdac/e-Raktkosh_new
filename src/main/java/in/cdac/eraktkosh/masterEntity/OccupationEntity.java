package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gblt_occupation_mst")
public class OccupationEntity {
	
	@Id
	private Integer occupationCode;
	private String occupationName;
	
	public Integer getOccupationCode() {
		return occupationCode;
	}
	public void setOccupationCode(Integer occupationCode) {
		this.occupationCode = occupationCode;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	
	

}
