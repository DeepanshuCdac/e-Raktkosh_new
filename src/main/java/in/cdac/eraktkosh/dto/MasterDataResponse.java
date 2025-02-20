// in use for all in one api call...

package in.cdac.eraktkosh.dto;

import java.util.List;

import in.cdac.eraktkosh.masterEntity.BloodGroupEntity;
import in.cdac.eraktkosh.masterEntity.ComponentListEntity;
import in.cdac.eraktkosh.masterEntity.GenderEntity;
import in.cdac.eraktkosh.masterEntity.MaritalStatusEntity;
import in.cdac.eraktkosh.masterEntity.OccupationEntity;
import in.cdac.eraktkosh.masterEntity.ReligionEntity;

public class MasterDataResponse {

	private List<StateWithDistricts> statesWithDistricts;
	private List<GenderEntity> genders;
	private List<MaritalStatusEntity> maritalStatus;
	private List<ReligionEntity> religion;
	private List<OccupationEntity> occupations;
	private List<BloodGroupEntity> bloodGroups;
	private List<ComponentListEntity> componentList;

	public List<StateWithDistricts> getStatesWithDistricts() {
		return statesWithDistricts;
	}

	public void setStatesWithDistricts(List<StateWithDistricts> statesWithDistricts) {
		this.statesWithDistricts = statesWithDistricts;
	}

	// Getters and setters for other fields
	public List<GenderEntity> getGenders() {
		return genders;
	}

	public void setGenders(List<GenderEntity> genders) {
		this.genders = genders;
	}

	public List<MaritalStatusEntity> getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(List<MaritalStatusEntity> maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public List<ReligionEntity> getReligion() {
		return religion;
	}

	public void setReligion(List<ReligionEntity> religion) {
		this.religion = religion;
	}

	public List<OccupationEntity> getOccupations() {
		return occupations;
	}

	public void setOccupations(List<OccupationEntity> occupations) {
		this.occupations = occupations;
	}

	public List<BloodGroupEntity> getBloodGroups() {
		return bloodGroups;
	}

	public void setBloodGroups(List<BloodGroupEntity> bloodGroups) {
		this.bloodGroups = bloodGroups;
	}

	public List<ComponentListEntity> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<ComponentListEntity> componentList) {
		this.componentList = componentList;
	}
}
