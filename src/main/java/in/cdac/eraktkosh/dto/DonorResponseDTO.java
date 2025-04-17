package in.cdac.eraktkosh.dto;

public class DonorResponseDTO {
	
	private String email;
    private String campReqNo;
    private Integer isResponse; 

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

    public Integer getIsResponse() {
        return isResponse;
    }

    public void setIsResponse(Integer isResponse) {
        this.isResponse = isResponse;
    }

}
