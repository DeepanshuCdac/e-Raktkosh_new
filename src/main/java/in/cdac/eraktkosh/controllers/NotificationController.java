package in.cdac.eraktkosh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import in.cdac.eraktkosh.dto.NotificationDTO;
import in.cdac.eraktkosh.dto.NotificationDataDTO;
import in.cdac.eraktkosh.services.NotificationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eraktkosh/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/ticker")
    public List<NotificationDTO> getTickerNotifications() {
        return notificationService.getNotifications();
    }
    
    @PostMapping("/popup")
    public Optional<NotificationDataDTO> getNotificationPopup(@RequestBody NotificationDataDTO request) {
    	
        return notificationService.getNotificationPopupById(request.getId());
    }

}
