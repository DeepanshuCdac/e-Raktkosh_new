package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.CampDonorRegisterRequestDTO;
import in.cdac.eraktkosh.repository.CampDonorRegisterRequestRepository;

@Service
public class CampDonorRegisterRequestService {

	@Autowired
	private CampDonorRegisterRequestRepository repository;

	public void registerCampDonor(CampDonorRegisterRequestDTO req) {

		if (repository.isDonorAlreadyRegistered(req.getCampId(), req.getMobileNo())) {
			repository.archiveAndDeleteExistingDonor(req.getCampId(), req.getMobileNo());
		}

		if (!repository.isPortalDonorExists(req.getMobileNo())) {
			repository.insertPortalDonor(req);
		}

		Long seqNo = repository.getDonorSequenceNumber();
		repository.insertCampDonor(req, seqNo);
	}
}
