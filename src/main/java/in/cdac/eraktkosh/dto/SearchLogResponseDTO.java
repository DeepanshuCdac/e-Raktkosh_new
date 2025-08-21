package in.cdac.eraktkosh.dto;

import java.time.LocalDateTime;

public class SearchLogResponseDTO {

	private String status;
	private Long logId;
	private LocalDateTime timestamp;

	public SearchLogResponseDTO() {
	}

	public SearchLogResponseDTO(String status, Long logId, LocalDateTime timestamp) {
		this.status = status;
		this.logId = logId;
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
