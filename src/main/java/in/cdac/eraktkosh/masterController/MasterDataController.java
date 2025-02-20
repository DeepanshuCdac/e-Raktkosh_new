// in use for all in one api ...

package in.cdac.eraktkosh.masterController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.MasterDataResponse;
import in.cdac.eraktkosh.masterService.MasterDataService;

@RestController
@RequestMapping("/eraktkosh/master")
public class MasterDataController {

	@Autowired
	private MasterDataService masterDataService;

//	 master api for all the masters...
	@PostMapping("/all")
	public MasterDataResponse getAllMasterData(@RequestBody Map<String, Integer> request) {
		int hospitalCode = request.get("hospitalCode");
		return masterDataService.getAllMasterData(hospitalCode);
	}

}
