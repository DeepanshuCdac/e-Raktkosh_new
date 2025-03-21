package in.cdac.eraktkosh.dto;

public class NotificationDataDTO {
    private Long id;
    private String title;
    private String data;
    
    public NotificationDataDTO(Long id, String title, String data) {
        this.id = id;
        this.title = title;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
