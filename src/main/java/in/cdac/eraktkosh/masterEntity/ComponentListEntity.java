package in.cdac.eraktkosh.masterEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hbbt_bld_component_mst")
public class ComponentListEntity {
	
	@Id
	private Integer componentCode;
	private String componentName;
	private String componentShortName;
	
	public Integer getComponentCode() {
		return componentCode;
	}
	public void setComponentCode(Integer componentCode) {
		this.componentCode = componentCode;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getComponentShortName() {
		return componentShortName;
	}
	public void setComponentShortName(String componentShortName) {
		this.componentShortName = componentShortName;
	}

}
