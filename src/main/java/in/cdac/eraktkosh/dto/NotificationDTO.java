package in.cdac.eraktkosh.dto;

import java.sql.Date;

public class NotificationDTO {

	private Long id;
	private String title;
	private Integer isUrl;
	private String docUrl;
	private Date startDate;

	public NotificationDTO(Long id, String title, Integer isUrl, String docUrl, Date startDate) {
		this.id = id;
		this.title = title;
		this.isUrl = isUrl;
		this.docUrl = docUrl;
		this.startDate = startDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIsUrl() {
		return isUrl;
	}

	public void setIsUrl(Integer isUrl) {
		this.isUrl = isUrl;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
