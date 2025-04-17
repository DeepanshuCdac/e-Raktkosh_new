package in.cdac.eraktkosh.dto;

import java.sql.Date;

public class NotificationDataDTO {
    private Long id;
    private String title;
    private String data;
    private Date startDate;
    
    public NotificationDataDTO(Long id, String title, String data, Date startDate) {
        this.id = id;
        this.title = title;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
