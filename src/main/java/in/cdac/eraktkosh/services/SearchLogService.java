package in.cdac.eraktkosh.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.cdac.eraktkosh.dto.SearchLogRequestDTO;
import in.cdac.eraktkosh.dto.SearchLogResponseDTO;
import in.cdac.eraktkosh.repository.SearchLogRepository;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;
    private final ObjectMapper objectMapper;

    public SearchLogService(SearchLogRepository searchLogRepository, ObjectMapper objectMapper) {
        this.searchLogRepository = searchLogRepository;
        this.objectMapper = objectMapper;
    }

    public SearchLogResponseDTO logSearch(SearchLogRequestDTO request, HttpServletRequest httpRequest) throws JsonProcessingException {
        String searchJson = objectMapper.writeValueAsString(request.getSearchParams());
        
        String ipAddress = request.getIpAddress();
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = httpRequest.getRemoteAddr();
            }
        }
        
        return searchLogRepository.saveSearchLog(
                request.getServiceType(),
                searchJson,
                ipAddress
        );
    }
}