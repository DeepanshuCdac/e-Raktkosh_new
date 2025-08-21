package in.cdac.eraktkosh.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.cdac.eraktkosh.services.ABHAServices;

@RestController
@RequestMapping("/eraktkosh/abha")
public class ABHAController {

	private ABHAServices abhaServices;

	public ABHAController(ABHAServices abhaServices) {

		this.abhaServices = abhaServices;

	}

	@PostMapping(value = "/commonABHACall", produces = "application/json")
	public ResponseEntity<String> sendEmail(@RequestBody String reqBody, HttpServletRequest request) {
		try {

			JsonObject reqjson = new Gson().fromJson(reqBody, JsonObject.class);

			String resp = ABHAServices.commonABHACall(request, reqjson);

			return ResponseEntity.status(HttpStatus.OK).body(resp);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
		}
	}

}
