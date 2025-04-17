package in.cdac.eraktkosh.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.NotificationDTO;
import in.cdac.eraktkosh.dto.NotificationDataDTO;
import in.cdac.eraktkosh.services.QueryLoader;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final QueryLoader queryLoader;

    @Autowired
    public NotificationRepository(JdbcTemplate jdbcTemplate,  QueryLoader queryLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryLoader = queryLoader;
    }

    public List<NotificationDTO> getValidTickerNotifications() {
        String sql = queryLoader.getQuery("fetch.NOTIFICATION_DATA");  

        return jdbcTemplate.query(sql, notificationRowMapper);
    }

    private final RowMapper<NotificationDTO> notificationRowMapper = (rs, rowNum) -> new NotificationDTO(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getInt("isUrl"),
            rs.getString("docUrl"),
            rs.getDate("startDate")
    );
    
    @SuppressWarnings("deprecation")
    public Optional<NotificationDataDTO> getNotificationPopupById(Long id) {
        String sql = queryLoader.getQuery("fetch.NOTIFICATION_POPUP");

        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                return Optional.of(new NotificationDataDTO(
                    id,
                    rs.getString("hgstr_notification_title"),
                    rs.getString("hgstr_notification_data"),
                    rs.getDate("startDate")
                ));
            }
            return Optional.empty();
        });
    }

}
