package in.cdac.eraktkosh.dto;

public class NotificationDTO {
	
	 	private Long id;
	    private String title;
	    private Integer isUrl;
	    private String docUrl;

	    public NotificationDTO(Long id, String title, Integer isUrl, String docUrl) {
	        this.id = id;
	        this.title = title;
	        this.isUrl = isUrl;
	        this.docUrl = docUrl;
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

}
