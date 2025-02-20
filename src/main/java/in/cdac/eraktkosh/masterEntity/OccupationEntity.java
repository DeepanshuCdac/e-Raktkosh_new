package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gblt_occupation_mst")
public class OccupationEntity {

	@Id
	private String occupationCode;
	private String occupationName;

	public String getOccupationCode() {
		return occupationCode;
	}

	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}

	public String getOccupationName() {
		return occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

}
