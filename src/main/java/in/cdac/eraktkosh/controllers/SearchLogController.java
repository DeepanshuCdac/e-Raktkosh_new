package in.cdac.eraktkosh.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.eraktkosh.dto.SearchLogRequestDTO;
import in.cdac.eraktkosh.dto.SearchLogResponseDTO;
import in.cdac.eraktkosh.services.SearchLogService;

@RestController
@RequestMapping("/eraktkosh/search-log")
public class SearchLogController {

	private final SearchLogService searchLogService;

    public SearchLogController(SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }

    @PostMapping
    public ResponseEntity<SearchLogResponseDTO> logSearch(@RequestBody SearchLogRequestDTO request, HttpServletRequest httpRequest) {
        try {
        	SearchLogResponseDTO response = searchLogService.logSearch(request, httpRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	SearchLogResponseDTO errorResponse = new SearchLogResponseDTO();
            errorResponse.setStatus("error");
            errorResponse.setLogId(null);
            errorResponse.setTimestamp(null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
