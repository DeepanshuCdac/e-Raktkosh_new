package in.cdac.eraktkosh.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.cdac.eraktkosh.dto.BloodAvailabilityDTO;
import in.cdac.eraktkosh.services.QueryLoader;

@Repository
public class BloodAvailabilityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final QueryLoader queryLoader;

    @Autowired
    public BloodAvailabilityRepository(JdbcTemplate jdbcTemplate, QueryLoader queryLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryLoader = queryLoader;
    }

    public List<BloodAvailabilityDTO> fetchBloodAvailability(
            Integer stateCode,
            Integer districtId,
            Integer componentId,
            Integer bloodGroupId,
            List<Integer> hospitalCodes) {

        String query = queryLoader.getQuery("fetch.blood.availability");

        if (componentId == null) {
            query = query.replaceFirst("AND hbnum_component_id = \\?", "AND 1=1");
            query = query.replaceFirst("AND a.hbnum_component_id = \\?", "AND 1=1");
        }

        if (districtId == null) {
            query = query.replaceFirst("AND num_dist_id = \\?", "AND 1=1");
            query = query.replaceFirst("AND num_dist_id = \\?", "AND 1=1");
        }

        if (bloodGroupId == null) {
            query = query.replaceFirst("AND hbnum_bld_grp_id = \\?", "AND 1=1");
            query = query.replaceFirst("AND a.hbnum_bld_grp_id = \\?", "AND 1=1");
        }

        if (hospitalCodes == null || hospitalCodes.isEmpty()) {
            query = query.replaceAll("AND a\\.gnum_hospital_code IN \\(:hospitalCodes\\)", "AND 1=1");
        } else {
            String placeholders = hospitalCodes.stream()
                    .map(code -> "?")
                    .collect(Collectors.joining(", "));
            query = query.replace(":hospitalCodes", placeholders);
        }

        List<Object> params = new ArrayList<>();
        params.add(stateCode);
        if (districtId != null) params.add(districtId);
        if (componentId != null) params.add(componentId);
        if (bloodGroupId != null) params.add(bloodGroupId);
        if (hospitalCodes != null && !hospitalCodes.isEmpty()) params.addAll(hospitalCodes);

        params.add(stateCode);
        if (districtId != null) params.add(districtId);
        if (componentId != null) params.add(componentId);
        if (bloodGroupId != null) params.add(bloodGroupId);
        if (hospitalCodes != null && !hospitalCodes.isEmpty()) params.addAll(hospitalCodes);

        List<BloodAvailabilityDTO> result = jdbcTemplate.query(query, params.toArray(), getRowMapper());

        return aggregateBloodGroups(result);
    }

    private RowMapper<BloodAvailabilityDTO> getRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            BloodAvailabilityDTO dto = new BloodAvailabilityDTO();
            dto.setBldgrpcode1(rs.getString("bldgrpcode1"));
            dto.setCompId(rs.getString("compId"));
            dto.setCount(rs.getInt("count"));
            dto.setHospitalname(rs.getString("hospitalname"));
            dto.setHospitaladd(rs.getString("hospitaladd"));
            dto.setHospitalcontact(rs.getString("hospitalcontact"));
            dto.setHospitalCode(rs.getString("hospitalCode"));
            dto.setModerateStock(rs.getString("moderateStock"));
            dto.setHospitalType(rs.getString("hospitalType"));
            dto.setEntrydate(rs.getString("entrydate"));
            dto.setOffline(rs.getString("offline"));
            dto.setType(rs.getString("type"));
            return dto;
        };
    }

    private String getComponentName(String compId) {
        if (compId == null) return "Unknown Component";

        switch (compId.trim()) {
            case "21": return "Cryo Poor Plasma";
            case "17": return "Cryoprecipitate";
            case "13": return "Fresh Frozen Plasma";
            case "35": return "Granulocyte";
            case "29": return "Irradiated RBC";
            case "37": return "Leukocyte";
            case "30": return "Leukoreduced Rbc";
            case "12": return "Packed Red Blood Cells";
            case "19": return "Plasma";
            case "20": return "Platelet Concentrate";
            case "15": return "Platelet Poor Plasma";
            case "16": return "Platelet Rich Plasma";
            case "24": return "Platelets additive solutions";
            case "23": return "Random Donor Platelets";
            case "36": return "Reconsituted WB";
            case "28": return "Sagm Packed Red Blood Cells";
            case "18": return "Single Donor Plasma";
            case "14": return "Single Donor Platelet";
            case "34": return "Washed Prbc";
            case "11": return "Whole Blood";
            default: return "Unknown";
        }
    }

    private List<BloodAvailabilityDTO> aggregateBloodGroups(List<BloodAvailabilityDTO> list) {
        Map<String, List<BloodAvailabilityDTO>> groupedByHospital = list.stream()
                .collect(Collectors.groupingBy(BloodAvailabilityDTO::getHospitalCode));

        List<BloodAvailabilityDTO> aggregatedList = new ArrayList<>();

        for (Map.Entry<String, List<BloodAvailabilityDTO>> hospitalEntry : groupedByHospital.entrySet()) {
            String hospitalCode = hospitalEntry.getKey();
            List<BloodAvailabilityDTO> hospitalData = hospitalEntry.getValue();

            Map<String, List<BloodAvailabilityDTO>> groupedByComponent = hospitalData.stream()
                    .collect(Collectors.groupingBy(dto -> getComponentName(dto.getCompId())));

            Map<String, Map<String, String>> componentMap = new LinkedHashMap<>();

            for (Map.Entry<String, List<BloodAvailabilityDTO>> componentEntry : groupedByComponent.entrySet()) {
                String componentName = componentEntry.getKey();
                List<BloodAvailabilityDTO> componentList = componentEntry.getValue();

                List<String> available = new ArrayList<>();
                List<String> notAvailable = new ArrayList<>();

                for (BloodAvailabilityDTO dto : componentList) {
                    String line = dto.getBldgrpcode1() + " : " + dto.getCount();
                    if (dto.getCount() > 0) {
                        available.add(line);
                    } else {
                        notAvailable.add(line);
                    }
                }

                Map<String, String> availability = new HashMap<>();
                availability.put("available_WithQty", String.join(", ", available));
                availability.put("not_available_WithQty", String.join(", ", notAvailable));

                componentMap.put(componentName, availability);
            }

            BloodAvailabilityDTO aggregatedDTO = new BloodAvailabilityDTO();
            BloodAvailabilityDTO firstDTO = hospitalData.get(0);

            aggregatedDTO.setHospitalCode(hospitalCode);
            aggregatedDTO.setHospitalname(firstDTO.getHospitalname());
            aggregatedDTO.setHospitaladd(firstDTO.getHospitaladd());
            aggregatedDTO.setHospitalcontact(firstDTO.getHospitalcontact());
            aggregatedDTO.setModerateStock(firstDTO.getModerateStock());
            aggregatedDTO.setHospitalType(firstDTO.getHospitalType());
            aggregatedDTO.setEntrydate(firstDTO.getEntrydate());
            aggregatedDTO.setOffline(firstDTO.getOffline());
            aggregatedDTO.setType(firstDTO.getType());
            aggregatedDTO.setComponents(componentMap);

            aggregatedList.add(aggregatedDTO);
        }

        return aggregatedList;
    }
}
