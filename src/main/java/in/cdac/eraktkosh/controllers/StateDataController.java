package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.StateDataResponseDTO;
import in.cdac.eraktkosh.services.StateDataService;

@RestController
@RequestMapping("/eraktkosh/stateData")
public class StateDataController {

	 @Autowired
	    private StateDataService service;

	    @GetMapping
	    public StateDataResponseDTO getDashboardData() {
	        return service.getStateDashboardData();
	    }
}
