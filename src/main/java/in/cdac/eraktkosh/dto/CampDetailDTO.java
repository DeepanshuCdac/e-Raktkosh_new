package in.cdac.eraktkosh.dto;

public class CampDetailDTO {
	
	  private String campDate;
	    private String campTime;
	    private String campName;
	    private String campVenue;
	    private String contact;
	    private String conductedBy;
	    private String hospName;
	    private Long campReqNo;
	    private Integer type;
	    private Integer isPortalRegistration;
	    private String stateName;
	    private String districtName;
	    private String dateToSort;
	    
	    public CampDetailDTO(String campDate, String campTime, String campName, String campVenue, String contact, 
                String conductedBy, String hospName, Long campReqNo, Integer type, Integer isPortalRegistration, 
                String stateName, String districtName, String dateToSort) {
this.campDate = campDate;
this.campTime = campTime;
this.campName = campName;
this.campVenue = campVenue;
this.contact = contact;
this.conductedBy = conductedBy;
this.hospName = hospName;
this.campReqNo = campReqNo;
this.type = type;
this.isPortalRegistration = isPortalRegistration;
this.stateName = stateName;
this.districtName = districtName;
this.dateToSort = dateToSort;
}

public String getCampDate() { return campDate; }
public String getCampTime() { return campTime; }
public String getCampName() { return campName; }
public String getCampVenue() { return campVenue; }
public String getContact() { return contact; }
public String getConductedBy() { return conductedBy; }
public String getHospName() { return hospName; }
public Long getCampReqNo() { return campReqNo; }
public Integer getType() { return type; }
public Integer getIsPortalRegistration() { return isPortalRegistration; }
public String getStateName() { return stateName; }
public String getDistrictName() { return districtName; }
public String getDateToSort() { return dateToSort; }

public void setCampDate(String campDate) { this.campDate = campDate; }
public void setCampTime(String campTime) { this.campTime = campTime; }
public void setCampName(String campName) { this.campName = campName; }
public void setCampVenue(String campVenue) { this.campVenue = campVenue; }
public void setContact(String contact) { this.contact = contact; }
public void setConductedBy(String conductedBy) { this.conductedBy = conductedBy; }
public void setHospName(String hospName) { this.hospName = hospName; }
public void setCampReqNo(Long campReqNo) { this.campReqNo = campReqNo; }
public void setType(Integer type) { this.type = type; }
public void setIsPortalRegistration(Integer isPortalRegistration) { this.isPortalRegistration = isPortalRegistration; }
public void setStateName(String stateName) { this.stateName = stateName; }
public void setDistrictName(String districtName) { this.districtName = districtName; }
public void setDateToSort(String dateToSort) { this.dateToSort = dateToSort; }

@Override
public String toString() {
return "CampDetailDTO{" +
       "campDate='" + campDate + '\'' +
       ", campTime='" + campTime + '\'' +
       ", campName='" + campName + '\'' +
       ", campVenue='" + campVenue + '\'' +
       ", contact='" + contact + '\'' +
       ", conductedBy='" + conductedBy + '\'' +
       ", hospName='" + hospName + '\'' +
       ", campReqNo=" + campReqNo +
       ", type=" + type +
       ", isPortalRegistration=" + isPortalRegistration +
       ", stateName='" + stateName + '\'' +
       ", districtName='" + districtName + '\'' +
       ", dateToSort='" + dateToSort + '\'' +
       '}';
	}
}
