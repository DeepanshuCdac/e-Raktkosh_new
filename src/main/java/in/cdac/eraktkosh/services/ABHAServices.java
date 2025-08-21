package in.cdac.eraktkosh.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import in.cdac.eraktkosh.utility.HisUtil;

@Service
public class ABHAServices {

	private static HashMap<String, String> APIKeyMap;
	private static final String TOKEN_KEY = "";
	static {
		APIKeyMap = new HashMap<String, String>();

		// v3 Aadhaar login flow -- done..
		APIKeyMap.put("LoginAadhaarGenerateOtp", "/hip/abdm/v3/login/aadhaar/generateOtp");
		APIKeyMap.put("LoginAadhaarVerifyOtp", "/hip/abdm/v3/login/aadhaar/verifyOTP");

		// v3 get profile -- done
		APIKeyMap.put("LoginAbhaGetProfile", "/hip/abdm/v3/profile/getProfile");

		// v3 Mobile login flow -- DONE
		APIKeyMap.put("LoginMobileGenerateOtp", "/hip/abdm/v3/login/mobile/generateOtp");
		APIKeyMap.put("LoginMobileVerifyOtp", "/hip/abdm/v3/login/mobile/verifyOTP");
		APIKeyMap.put("LoginMobileVerifyuser", "/hip/abdm/v3/login/mobile/verifyUser");

		// v3 ABHA Number Login Flow -- done..
		APIKeyMap.put("LoginAbhaNumberGenerateOtp", "/hip/abdm/v3/login/ABHA/generateOtp");
		APIKeyMap.put("LoginAbhaNumberVerifyOtp", "/hip/abdm/v3/login/ABHA/verifyOTP");

		// v3 ABHA Search Flow -- DONE...
		APIKeyMap.put("SearchAbhaNumberViaMobile", "/hip/abdm/v3/abhasearch/search");
		APIKeyMap.put("AbhaSearchRequestOtp", "/hip/abdm/v3/abhasearch/requestOtp");
		APIKeyMap.put("AbhaSearchVerifyOtp", "/hip/abdm/v3/abhasearch/verifyLogin");

		// v3 create abha via aadhaar bio --
		APIKeyMap.put("AadhaarBioCreationVerify", "/hip/abdm/v3/registration/aadhaarbio/verifybio");

		// v3 create abha via aadhaar otp -- done
		APIKeyMap.put("CreationAadhaarRequestOtp", "/hip/abdm/v3/registration/aadhaarone/generateOtp");
		APIKeyMap.put("CreationAadhaarVerifyOtp", "/hip/abdm/v3/registration/aadhaarone/verifyOTP");
		APIKeyMap.put("CreationAadhaarAbhaSuggestions", "/hip/abdm/v3/registration/aadhaarone/abhaSuggestion");
		APIKeyMap.put("CreationAadhaarCreateAbhaAddress", "/hip/abdm/v3/registration/aadhaarone/createAbhaAddress");

		// v3 create abha communication mobile otp -- done -- used when creating via
		// aadhaar or bio..
		APIKeyMap.put("CommunicationMobileRequestOtp", "/hip/abdm/v3/registration/aadhaarone/generateMobileOtp");
		APIKeyMap.put("CommunicationMobileVerifyOtp", "/hip/abdm/v3/registration/aadhaarone/verifyMobileOTP");

		// v3 create abha mobile otp -- done
		APIKeyMap.put("CreationMobileRequestOtp", "/hip/v3/registration/mobile/generateOtp");
		APIKeyMap.put("CreationMobileVerifyOtp", "/hip/v3/registration/mobile/verifyOtp");
		APIKeyMap.put("CreationMobileCreateAbha", "/hip/v3/registration/mobile/createAbhaAddress");

		// v3 login abha address -- done
		APIKeyMap.put("LoginAbhaAddressGenerateOtp", "/hip/abdm/v3/login/abhaAddress/generateOtp");
		APIKeyMap.put("LoginAbhaAddressVerifyOtp", "/hip/abdm/v3/login/abhaAddress/verifyOTP");
		APIKeyMap.put("LoginSearchAuthModes", "/hip/v3/search/searchByHealthId");

		// v3 get phr-profile -- done
		APIKeyMap.put("LoginAbhaAddressGetPhrProfile", "/hip/abdm/v3/profile/getPHRProfile");

	}

	public static String getFacilityIdBasedOnHOSiptalCode(HttpServletRequest request) throws Exception {
		// HisDAO hisDAO_p = null;
		// HospitalMasterVO voHospital_p = new HospitalMasterVO();
		// int nProcedureIndex;
		// String strDBErr;
		// ResultSet objResSet;
		// UserVO userVO = getUserVO(request);
		String hospCode = request.getParameter("HospitalCode");

		if (hospCode == null || hospCode.equals("")) {
			hospCode = HisUtil.getParameterFromHisPathXML("HOSPITAL_CODE");
		}

		String strColVal = "";
		// List<HospitalMasterVO> lst = new ArrayList<HospitalMasterVO>();
		try {

			// objResSet.get
			// varHospitalName If Database Error Occurs, No farther processing is required.

			// strColVal = objResSet.getString(2);

			System.out.println("FAcilityIdddddddddddddddd" + strColVal);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strColVal;
	}

	public static JsonObject map_request_to_Json(HttpServletRequest request, JsonObject reqjson) {

		JsonObject requestJSON = new JsonObject();
		JsonObject headerObj = new JsonObject();
		Gson gson = new Gson();

		System.out.println("parameter names: ");
		// Enumeration<String> names = request.getParameterNames();
		// int count = 0;

		String headerPrefix = "header#";
//		while (names.hasMoreElements()) {
//			count++;
//			String Param = names.nextElement();
//			System.out.println(Param);
//			if (!Param.equals(TOKEN_KEY) && !Param.equals("APIKey") && !Param.equals("hmode") && !Param.equals("hmode")) {
//				if (Param.startsWith(headerPrefix)) {
//
//					headerObj.addProperty(Param.split("#")[1], request.getParameter(Param));
//				} else {
//					requestJSON.addProperty(Param, request.getParameter(Param));
//				}
//
//			}
//			requestJSON.add("headers", headerObj);
//
//			if (count > 100)
//				break;
//		}

		Set<String> keys = reqjson.keySet();

		for (String key : keys) {
			JsonElement value = reqjson.get(key);
			System.out.println(key);

			if (!key.equals(TOKEN_KEY) && !key.equals("APIKey") && !key.equals("hmode") && !key.equals("hmode")) {
				if (key.startsWith(headerPrefix)) {

					headerObj.addProperty(key.split("#")[1], reqjson.get(key).getAsString());
				} else {
					requestJSON.addProperty(key, reqjson.get(key).getAsString());
				}

			}
			requestJSON.add("headers", headerObj);

		}

		return requestJSON;
	}

	public static String commonABHACall(HttpServletRequest request, JsonObject reqjson) throws Exception {

		Gson gson = new Gson();

		if (!reqjson.has("APIKey") || reqjson.get("APIKey").isJsonNull() || reqjson.get("APIKey") == null
				|| reqjson.get("APIKey").getAsString().trim().equals("")) {

			JsonObject resp = new JsonObject();
			resp.addProperty("isSuccess", 0);
			resp.addProperty("Error", "No value for APIKey.");

			// writeResponse(response, gson.toJson(resp));
			return gson.toJson(resp);
		}

		String APIKey = reqjson.get("APIKey").getAsString();
		// request.removeAttribute("APIKey");

		String APIUrl = "";
		try {
			if (APIKeyMap.containsKey(APIKey)) {
				APIUrl = APIKeyMap.get(APIKey);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (APIUrl.equals("")) {

			JsonObject resp = new JsonObject();
			resp.addProperty("isSuccess", 0);
			resp.addProperty("Error", "No value for APIUrl.");

			// writeResponse(response, gson.toJson(resp));
			return gson.toJson(resp);
		}

		String url = HisUtil.getParameterFromHisPathXML("ABDM_HIP_PATH");
		String HISAuthKey = HisUtil.getParameterFromHisPathXML("ABDM_HIP_AUTH_KEY");
		// URL obj = new URL(url + "/hip/demoAuth/user/createABHADemoAuth");

		URL obj = new URL(url + APIUrl);

		// URL obj = new
		// URL("http://10.226.17.5:8082/hip/demoAuth/user/createABHADemoAuth");
		System.out.println("obj--" + obj);
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

		postConnection.setConnectTimeout(5000);
		postConnection.setReadTimeout(60000);

		// postConnection.addRequestProperty("HIS-AUTH-KEY",
		// "fbdcf45645fgnG34534FvdfFvdfbNytHERgSdbsdvsdvs3");
		// postConnection.addRequestProperty("HIS-AUTH-KEY", abdmUtility.HISAuthKey);
		postConnection.addRequestProperty("HIS-AUTH-KEY", HISAuthKey);
		// postConnection.addRequestProperty("X-Token", token);
		// URLConnection postConnection = obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
		postConnection.setRequestProperty("Accept", "application/json");

		JsonObject getRequest = map_request_to_Json(request, reqjson);
		// JsonObject reqJson = gson.fromJson(request.toString(), JsonObject.class);
		// System.out.println("reqJson: "+ reqJson);
		if (getRequest.has("headers")) {
			JsonObject headersObj = getRequest.get("headers").getAsJsonObject();
			getRequest.remove("headers");

			headersObj.entrySet().forEach(entry -> {
				String key = entry.getKey();
				String value = entry.getValue().getAsString();
				System.out.println("Key: " + key + ", Value: " + value);
				postConnection.setRequestProperty(key, value);
			});
		}

		System.out.println("getRequest: " + getRequest);

		// getRequest.put("authMode", authMode);
		// getRequest.put("otp", otp);
		// getRequest.put("txnId", txnId);

		// UserVO userVO = getUserVO(request);
		String hospitalcode = "";// request.getParameter("HospitalCode");

		if (reqjson.has("HospitalCode") && !reqjson.get("HospitalCode").isJsonNull()
				&& reqjson.get("HospitalCode") != null
				&& !reqjson.get("HospitalCode").getAsString().trim().equals("")) {

			hospitalcode = reqjson.get("HospitalCode").getAsString();
		}

		String facilityID = "";
		try {
			facilityID = getFacilityIdBasedOnHOSiptalCode(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		getRequest.addProperty("hospitalCode", hospitalcode);
		getRequest.addProperty("facilityID", facilityID.equals("") ? "BB" + hospitalcode : facilityID);

		String jsonInputString = getRequest.toString();
		System.out.println("sending data--------------------------------" + jsonInputString);
		byte[] out = jsonInputString.getBytes("utf-8");

		postConnection.setDoOutput(true);
		try (OutputStream os = postConnection.getOutputStream()) {
			os.write(out);
		}

		int responseCode = 0;
		String isverify = null;
		try {
			responseCode = postConnection.getResponseCode();
		} catch (Exception e) {
			System.out.println("read timeout");
			responseCode = 400;

			System.out.println("response-------------------------" + responseCode);

			System.out.println("POST NOT WORKED");
			JsonObject jsonResponse1 = new JsonObject();

			JsonObject Error = new JsonObject();
			JsonArray details = new JsonArray();
			JsonObject deatailObj = new JsonObject();
			deatailObj.addProperty("message", "Respone took too long.");
			details.add(deatailObj);
			Error.add("details", details);
			jsonResponse1.add("Error", Error);

			jsonResponse1.addProperty("responseCode", responseCode);
			jsonResponse1.addProperty("isSuccess", "0");
			jsonResponse1.addProperty("error", "Respone took too long.");
			isverify = jsonResponse1.toString();
			System.out.println("respons-----detail--------" + isverify);
			// writeResponse(response, isverify);
			return isverify;
		}

		if (responseCode == 202 || responseCode == 200) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			StringBuffer responses = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				responses.append(inputLine);
			}
			in.close();

			// added on 4-13-23
			System.out.println("resp ___________" + responses.toString());

			// updating for authResult
			String respString = responses.toString();
			try {
				JsonObject respJsonObject = gson.fromJson(responses.toString(), JsonObject.class);
				if (respJsonObject.has("authResult") && respJsonObject.get("authResult") != null) {
					String authResult = respJsonObject.get("authResult").getAsString();
					if ("failed".equalsIgnoreCase(authResult)) {
						respJsonObject.addProperty("isSuccess", "0");
						respString = gson.toJson(respJsonObject);
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error in authResult updateion logic");
			}

			// updating for authResult

			isverify = respString;
			// writeResponse(response, responses.toString());
			// writeResponse(response, respString);

		} else {

			System.out.println("response-------------------------" + responseCode);

			System.out.println("POST NOT WORKED");
			JsonObject jsonResponse1 = new JsonObject();
			jsonResponse1.addProperty("responseCode", responseCode);
			jsonResponse1.addProperty("isSuccess", "0");
			jsonResponse1.addProperty("error", "Something Went Wrong");
			isverify = jsonResponse1.toString();
			System.out.println("respons-----detail--------" + isverify);
			// writeResponse(response, isverify);
		}

		return isverify;
	}

}
