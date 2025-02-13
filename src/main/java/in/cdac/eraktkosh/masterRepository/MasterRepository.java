package in.cdac.eraktkosh.masterRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.masterEntity.BloodGroupEntity;
import in.cdac.eraktkosh.masterEntity.ComponentListEntity;
import in.cdac.eraktkosh.masterEntity.District;
import in.cdac.eraktkosh.masterEntity.GenderEntity;
import in.cdac.eraktkosh.masterEntity.MaritalStatusEntity;
import in.cdac.eraktkosh.masterEntity.OccupationEntity;
import in.cdac.eraktkosh.masterEntity.ReligionEntity;
import in.cdac.eraktkosh.masterEntity.State;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class MasterRepository {
	
	@Autowired
 private JdbcTemplate jdbcTemplate;
    
    private final QueryLoader queryLoader;

    public MasterRepository() {
        queryLoader = new QueryLoader("query.properties");
    }
    
//    marital Status repo -- starts ---
    public List<MaritalStatusEntity>findAllMaritalStatus() {
        String sqlQuery = queryLoader.getQuery("fetch.maritalStatus");
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
        	MaritalStatusEntity maritalStatus = new MaritalStatusEntity();
        	maritalStatus.setMaritalStatusCode(rs.getInt("gnum_marital_status_code"));
        	maritalStatus.setMaritalStatusName(rs.getString("gstr_marital_status"));
            return maritalStatus;
        });
    } 
//    marriage status repo -- ends --
    
//    gender repo -- starts --
    @SuppressWarnings("deprecation")
    public List<GenderEntity>findAllGenders() {
        String sqlQuery = queryLoader.getQuery("fetch.gender");
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
        	GenderEntity gender = new GenderEntity();
            gender.setGenderCode(rs.getString("gstr_gender_code"));
            gender.setGenderName(rs.getString("gstr_gender_name"));
            return gender;
        });
    }
//    gender repo -- ends --
    
//    occupation repo -- starts --
    public List<OccupationEntity>findAllOccupation() {
        String sqlQuery = queryLoader.getQuery("fetch.occupation");
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
        	OccupationEntity occupation = new OccupationEntity();
        	occupation.setOccupationCode(rs.getInt("gnum_occupation_code"));
        	occupation.setOccupationName(rs.getString("gstr_occupation_name"));
            return occupation;
        });
    } 
//    occupation repo -- ends --
    
//  religion repo -- starts --
  public List<ReligionEntity>findAllReligion() {
      String sqlQuery = queryLoader.getQuery("fetch.religion");
      return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
    	  ReligionEntity religion = new ReligionEntity();
    	  religion.setReligionCode(rs.getInt("gnum_religion_code"));
    	  religion.setReligionName(rs.getString("gstr_religion_name"));
          return religion;
      });
  } 
//  religion repo -- ends --
  
//bloodGroup repo -- starts --
public List<BloodGroupEntity>findAllBloodGroup() {
    String sqlQuery = queryLoader.getQuery("fetch.bloodGroup");
    return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
    	BloodGroupEntity bloodGroup = new BloodGroupEntity();
    	bloodGroup.setBloodGroupCode(rs.getInt("hbnum_bldgrp_code"));
    	bloodGroup.setBloodGroupName(rs.getString("hbstr_bldgrp"));
        return bloodGroup;
    });
} 
//bloodGroup repo -- ends --

//Component List repo -- starts --
@SuppressWarnings("deprecation")
public List<ComponentListEntity>findAllComponentList(int hospitalCode) {
  String sqlQuery = queryLoader.getQuery("fetch.componentList");
  return jdbcTemplate.query(sqlQuery, new Object[]{hospitalCode}, (rs, rowNum) -> {
	  ComponentListEntity componentList = new ComponentListEntity();
	  componentList.setComponentCode(rs.getInt("hbnum_bld_component_id"));
	  componentList.setComponentName(rs.getString("hbstr_component_name"));
	  componentList.setComponentShortName(rs.getString("hbstr_short_name"));
      return componentList;
  });
} 
//Component List repo -- ends --
    
//    state repo -- starts --
    @SuppressWarnings("deprecation")
    public List<State> findAllValidStates() {
        String sqlQuery = queryLoader.getQuery("fetch.states");
        
        return jdbcTemplate.query(sqlQuery, new RowMapper<State>() {
            @Override
            public State mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                State state = new State();
                state.setStateCode(rs.getInt("GNUM_STATE_CODE"));
                state.setStateName(rs.getString("GSTR_STATE_NAME"));
                return state;
            }
        });
    }
//    state repo -- ends --
    
//  District repo -- starts --
    @SuppressWarnings("deprecation")
	public List<District> findDistrictsByStateCode(Integer stateCode) {
	    String sqlQuery = queryLoader.getQuery("fetch.districts");
	    return jdbcTemplate.query(sqlQuery, new Object[]{stateCode}, new RowMapper<District>() {
	        @Override
	        public District mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
	        	 District district = new District();
	        	  district.setDistrictCode(rs.getInt("gnum_district_code"));
	              district.setDistrictName(rs.getString("gstr_district_name"));
	              return district;
	        }
	    });
	}
//  District repo -- ends --
    
}
