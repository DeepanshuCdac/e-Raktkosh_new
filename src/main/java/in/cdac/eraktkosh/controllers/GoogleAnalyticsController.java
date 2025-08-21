package in.cdac.eraktkosh.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.services.GoogleAnalyticsService;

@RestController
@RequestMapping
public class GoogleAnalyticsController {
	
	 @Autowired
	    private GoogleAnalyticsService analyticsService;

	 @GetMapping("/active-users")
	 public Map<String, Integer> getActiveUsers() {
	     int count = analyticsService.getActiveUsers();
	     return Collections.singletonMap("activeUsers", count);
	 }
}
