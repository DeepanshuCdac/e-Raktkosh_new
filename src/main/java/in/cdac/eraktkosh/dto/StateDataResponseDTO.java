package in.cdac.eraktkosh.dto;

import java.util.List;

public class StateDataResponseDTO {
	
	private List<TotalBloodCollectedDTO> bloodCollectionSummary;
    private List<TotalBloodCenterDTO> totalBloodCenters;
    private List<TotalRegisteredDonorDTO> donorRegistered;
    private List<TotalCampsOrganisedDTO> campsOrganised;
    
	public List<TotalCampsOrganisedDTO> getCampsOrganised() {
		return campsOrganised;
	}
	public void setCampsOrganised(List<TotalCampsOrganisedDTO> campsOrganised) {
		this.campsOrganised = campsOrganised;
	}
	public List<TotalBloodCollectedDTO> getBloodCollectionSummary() {
		return bloodCollectionSummary;
	}
	public void setBloodCollectionSummary(List<TotalBloodCollectedDTO> bloodCollectionSummary) {
		this.bloodCollectionSummary = bloodCollectionSummary;
	}
	public List<TotalBloodCenterDTO> getTotalBloodCenters() {
		return totalBloodCenters;
	}
	public void setTotalBloodCenters(List<TotalBloodCenterDTO> totalBloodCenters) {
		this.totalBloodCenters = totalBloodCenters;
	}
	public List<TotalRegisteredDonorDTO> getDonorRegistered() {
		return donorRegistered;
	}
	public void setDonorRegistered(List<TotalRegisteredDonorDTO> donorRegistered) {
		this.donorRegistered = donorRegistered;
	}

}
