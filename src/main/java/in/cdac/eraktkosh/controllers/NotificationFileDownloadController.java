package in.cdac.eraktkosh.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/download")
public class NotificationFileDownloadController {

	@GetMapping("/document")
	public ResponseEntity<Resource> downloadDocument(@RequestParam("type") int type) {
		String osType = System.getProperty("os.name");
		boolean isWindows = osType.startsWith("Win");
		String filePath = getFilePath(type, isWindows);

		if (filePath == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
			headers.setContentType(MediaType.APPLICATION_PDF);

			return ResponseEntity.ok().headers(headers).contentLength(file.length())
					.contentType(MediaType.APPLICATION_PDF).body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private String getFilePath(int type, boolean isWindows) {
		String basePath = isWindows ? "c:/BBNW1/eRaktKosh/eraktkosh_downloads/" : "/opt/eraktkosh_downloads/";

		switch (type) {
		case 1:
			return basePath + "NHM_Guidelines_on_Hemoglobinopathies_in_India_web.pdf";
		case 2:
			return basePath + "Training_exercise_eraktkosh.pdf";
		case 3:
			return basePath + "VBD Guidelines.pdf";
		case 4:
			return basePath + "criteria for disability.pdf";
		case 5:
			return basePath + "FreeBlood.pdf";
		case 6:
			return basePath + "Technical Specification for Blood banks & Blood Storage Equipments.pdf";
		case 7:
			return basePath + "Bed side Transfusion guidelines.pdf";
		case 8:
			return basePath + "Draft policy for hemoglobinopathies.pdf";
		case 9:
			return basePath + "Essentialservices.pdf";
		case 10:
			return basePath + "Honble HFM Letter to all the Hon'ble State Health Ministers.pdf";
		case 11:
			return basePath + "Covid-19.pdf";
		case 12:
			return basePath + "Letter from Add.pdf";
		case 13:
			return basePath
					+ "Final Trainining Manual for Hemoglobinopathies and hemophilia received by NIIH on October 2019.pdf";
		case 14:
			return basePath + "Deferral Criteria for blood donation Post Covid-19.pdf";
		case 15:
			return basePath + "International Thalassemia Day.pdf";
		default:
			return null;
		}
	}
}
