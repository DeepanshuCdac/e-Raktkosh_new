package in.cdac.eraktkosh.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.cdac.eraktkosh.services.UtilityService;

@RestController
@RequestMapping("/eraktkosh/utility")
public class UtilityController {
	@Autowired
	private UtilityService utilityService;

	@PostMapping(value = "/getLgdStateCode", produces = "application/json")
	public ResponseEntity<String> getLgdStateCode(@RequestBody String reqBody, HttpServletRequest request) {
		try {

			JsonObject reqjson = new Gson().fromJson(reqBody, JsonObject.class);

			if (reqjson.has("stateCode") && !reqjson.get("stateCode").isJsonNull() && reqjson.get("stateCode") != null
					&& !reqjson.get("stateCode").getAsString().trim().equals("")) {
				String resp = utilityService.getLgdStateCode(reqjson.get("stateCode").getAsString());
				return ResponseEntity.status(HttpStatus.OK).body(resp);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
		}
	}

	@PostMapping(value = "/getLgdDistrictCode", produces = "application/json")
	public ResponseEntity<String> getLgdDistrictCode(@RequestBody String reqBody, HttpServletRequest request) {
		try {

			JsonObject reqjson = new Gson().fromJson(reqBody, JsonObject.class);

			if (reqjson.has("districtCode") && !reqjson.get("districtCode").isJsonNull()
					&& reqjson.get("districtCode") != null
					&& !reqjson.get("districtCode").getAsString().trim().equals("")) {
				String resp = utilityService.getLgdDistrictCode(reqjson.get("districtCode").getAsString());
				return ResponseEntity.status(HttpStatus.OK).body(resp);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
		}
	}

	@PostMapping(value = "/getStateCode", produces = "application/json")
	public ResponseEntity<String> getStateCode(@RequestBody String reqBody, HttpServletRequest request) {
		try {

			JsonObject reqjson = new Gson().fromJson(reqBody, JsonObject.class);

			if (reqjson.has("lgdStateCode") && !reqjson.get("lgdStateCode").isJsonNull()
					&& reqjson.get("lgdStateCode") != null
					&& !reqjson.get("lgdStateCode").getAsString().trim().equals("")) {
				String resp = utilityService.getStateCode(reqjson.get("lgdStateCode").getAsString());
				return ResponseEntity.status(HttpStatus.OK).body(resp);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
		}
	}

	@PostMapping(value = "/getDistrictCode", produces = "application/json")
	public ResponseEntity<String> getDistrictCode(@RequestBody String reqBody, HttpServletRequest request) {
		try {

			JsonObject reqjson = new Gson().fromJson(reqBody, JsonObject.class);

			if (reqjson.has("lgdDistrictCode") && !reqjson.get("lgdDistrictCode").isJsonNull()
					&& reqjson.get("lgdDistrictCode") != null
					&& !reqjson.get("lgdDistrictCode").getAsString().trim().equals("")) {
				String resp = utilityService.getDistrictCode(reqjson.get("lgdDistrictCode").getAsString());
				return ResponseEntity.status(HttpStatus.OK).body(resp);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
		}
	}

}
