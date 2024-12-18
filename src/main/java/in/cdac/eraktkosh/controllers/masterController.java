package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import in.cdac.eraktkosh.master.entity.State;
import in.cdac.eraktkosh.master.service.StateService;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/states")
public class masterController {

    private final StateService stateService;

    @Autowired
    public masterController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/occupationDetails")
    public  Map<String, Object> getStates(
            @RequestParam String countryCode,
          
            @RequestParam String eraktkoshEnabled
            )
            {
        return stateService.getStates(countryCode, eraktkoshEnabled);
    }
    
  
    @GetMapping("/deepanshu")
    public String hello() {
    	return "hiii deepanshu";
		
	}
}
