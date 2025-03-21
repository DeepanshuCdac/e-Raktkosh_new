package in.cdac.eraktkosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cdac.eraktkosh.dto.NotificationDTO;
import in.cdac.eraktkosh.dto.NotificationDataDTO;
import in.cdac.eraktkosh.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDTO> getNotifications() {
        return notificationRepository.getValidTickerNotifications();
    }
    
    public Optional<NotificationDataDTO> getNotificationPopupById(long id) {
        return notificationRepository.getNotificationPopupById(id);
    }
}
