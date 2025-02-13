package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gblt_district_mst")
public class District {
	
	@Id
	 private Integer districtCode;
	    private String districtName;

	    // Getters and setters
	    public Integer getDistrictCode() {
	        return districtCode;
	    }

	    public void setDistrictCode(Integer districtCode) {
	        this.districtCode = districtCode;
	    }

	    public String getDistrictName() {
	        return districtName;
	    }

	    public void setDistrictName(String districtName) {
	        this.districtName = districtName;
	    }

}
