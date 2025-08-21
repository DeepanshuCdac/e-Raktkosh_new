package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import in.cdac.eraktkosh.repository.UtilityRepository;

@Service
public class UtilityService {

	@Autowired
	private UtilityRepository utilityRepository;

	public String getLgdStateCode(String stateCode) {

		System.out.println("Inside UtilityService::getLgdStateCode");
		Gson gson = new Gson();

		String LgdStateCode = utilityRepository.getLgdStateCode(stateCode);

		JsonObject resp = new JsonObject();
		resp.addProperty("lgdStateCode", LgdStateCode);

		return gson.toJson(resp);
	}

	public String getLgdDistrictCode(String districtCode) {

		System.out.println("Inside UtilityService::getLgdDistrictCode");
		Gson gson = new Gson();

		String LgdDistrictCode = utilityRepository.getLgdDistrictCode(districtCode);

		JsonObject resp = new JsonObject();
		resp.addProperty("lgdDistrictCode", LgdDistrictCode);

		return gson.toJson(resp);
	}

	public String getStateCode(String lgdStateCode) {

		System.out.println("Inside UtilityService::getStateCode");
		Gson gson = new Gson();

		String StateCode = utilityRepository.getStateCode(lgdStateCode);

		JsonObject resp = new JsonObject();
		resp.addProperty("stateCode", StateCode);

		return gson.toJson(resp);
	}

	public String getDistrictCode(String lgdDistrictCode) {

		System.out.println("Inside UtilityService::getDistrictCode");
		Gson gson = new Gson();

		String DistrictCode = utilityRepository.getDistrictCode(lgdDistrictCode);

		JsonObject resp = new JsonObject();
		resp.addProperty("districtCode", DistrictCode);

		return gson.toJson(resp);
	}

}
