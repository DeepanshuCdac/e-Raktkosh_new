package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.StateDataResponseDTO;
import in.cdac.eraktkosh.repository.StateDataRepository;

@Service
public class StateDataService {

	@Autowired
    private StateDataRepository repository;

    public StateDataResponseDTO getStateDashboardData() {
    	StateDataResponseDTO response = new StateDataResponseDTO();
        response.setBloodCollectionSummary(repository.getBloodCollectionSummary());
        response.setTotalBloodCenters(repository.getTotalBloodCenters());
        response.setDonorRegistered(repository.getDonorRegistered());
        response.setCampsOrganised(repository.getCampsOrganised());
        return response;
    }
}
