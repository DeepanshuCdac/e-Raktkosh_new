package in.cdac.eraktkosh.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CampNotificationDTO {

	private String mobileNo;
    private String email;
    private String campReqNo;
    private String campName;
    private String campDate;
    private String campTime;
    private String venueName;
    private String venueCity;
    
    public LocalDate getCampDateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return LocalDate.parse(campDate, formatter);
    }

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCampReqNo() {
		return campReqNo;
	}

	public void setCampReqNo(String campReqNo) {
		this.campReqNo = campReqNo;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public String getCampDate() {
		return campDate;
	}

	public void setCampDate(String campDate) {
		this.campDate = campDate;
	}

	public String getCampTime() {
		return campTime;
	}

	public void setCampTime(String campTime) {
		this.campTime = campTime;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueCity() {
		return venueCity;
	}

	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}
}
