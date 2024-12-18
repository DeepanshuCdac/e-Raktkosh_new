package in.cdac.eraktkosh.master.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.master.entity.State;
import in.cdac.eraktkosh.master.repository.stateRepository;

import java.util.List;
import java.util.Map;

@Service
public class StateService {
	@Autowired
    private  stateRepository stateRepo;

    public  Map<String, Object> getStates(String countryCode, String eraktkoshEnabled) {

        return stateRepo.getStates(countryCode, eraktkoshEnabled);
    }
}
