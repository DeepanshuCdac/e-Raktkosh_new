package in.cdac.eraktkosh.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.TotalBloodCenterDTO;
import in.cdac.eraktkosh.dto.TotalBloodCollectedDTO;
import in.cdac.eraktkosh.dto.TotalCampsOrganisedDTO;
import in.cdac.eraktkosh.dto.TotalRegisteredDonorDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class StateDataRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryLoader queryLoader;

    @SuppressWarnings("deprecation")
    public List<TotalBloodCollectedDTO> getBloodCollectionSummary() {
        String query = queryLoader.getQuery("query.blood.collection.summary");
        return jdbcTemplate.query(query, new Object[]{}, (rs, rowNum) -> {
        	TotalBloodCollectedDTO dto = new TotalBloodCollectedDTO();
            dto.setStateName(rs.getString("STATENAME"));
            dto.setStateCode(rs.getInt("StateCode"));
            dto.setTotalCollection(rs.getInt("TOTAL_COLLECTION"));
            return dto;
        });
    }

    @SuppressWarnings("deprecation")
    public List<TotalBloodCenterDTO> getTotalBloodCenters() {
        String query = queryLoader.getQuery("query.total.bloodcentres");
        return jdbcTemplate.query(query, new Object[]{}, (rs, rowNum) -> {
        	TotalBloodCenterDTO dto = new TotalBloodCenterDTO();
            dto.setHnumTotalBloodCentres(rs.getInt("TOTAL_BLOOD_CENTRES"));
            dto.setStateCode(rs.getInt("stateCode"));
            return dto;
        });
    }

    @SuppressWarnings("deprecation")
    public List<TotalRegisteredDonorDTO> getDonorRegistered() {
        String query = queryLoader.getQuery("query.donor.registered");
        return jdbcTemplate.query(query, new Object[]{}, (rs, rowNum) -> {
        	TotalRegisteredDonorDTO dto = new TotalRegisteredDonorDTO();
            dto.setStateName(rs.getString("STATENAME"));
            dto.setStateCode(rs.getInt("stateCode"));
            dto.setHnumDonorRegistered(rs.getInt("HBNUMDONORREGISTERED"));
            return dto;
        });
    }
    
    @SuppressWarnings("deprecation")
    public List<TotalCampsOrganisedDTO> getCampsOrganised() {
        String query = queryLoader.getQuery("query.camps.organised");
        return jdbcTemplate.query(query, new Object[]{}, (rs, rowNum) -> {
            TotalCampsOrganisedDTO dto = new TotalCampsOrganisedDTO();
            dto.setStateCode(rs.getInt("STATE_CODE"));
            dto.setFinalCount(rs.getInt("TOTAL_COUNT"));
            return dto;
        });
    }

}
