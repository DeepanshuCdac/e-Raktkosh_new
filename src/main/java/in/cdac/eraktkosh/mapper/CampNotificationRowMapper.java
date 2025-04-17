package in.cdac.eraktkosh.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import in.cdac.eraktkosh.dto.CampNotificationDTO;

public class CampNotificationRowMapper implements RowMapper<CampNotificationDTO> {
    @Override
    public CampNotificationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CampNotificationDTO dto = new CampNotificationDTO();
        dto.setMobileNo(rs.getString("hbnum_mobile_no"));
        dto.setEmail(rs.getString("hbstr_email_id"));
        dto.setCampReqNo(rs.getString("hbnum_camp_req_no"));
        dto.setCampName(rs.getString("hbstr_camp_name"));
        dto.setCampDate(rs.getString("hbdt_camp_date"));
        dto.setCampTime(rs.getString("hbdt_camp_time"));
        dto.setVenueName(rs.getString("hbstr_venue_name"));
        dto.setVenueCity(rs.getString("hbstr_venue_city"));
        return dto;
    }
}

