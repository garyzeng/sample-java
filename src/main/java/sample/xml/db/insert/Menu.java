package sample.xml.db.insert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
	
	private static final long serialVersionUID = 6696974250588342384L;

	private String uuid;
	
	private String name;
	
	private String url;
	
	private boolean enabled;
	
	private boolean display;
	
	private String icon;
	
	private String description;
	
	private String parentUuid;
	
	private Integer position;
	
	private List<String> groups = new ArrayList<String>();
	
	public Menu() {
		
	}
	
	public Menu(String uuid, String name, String url,
			boolean enabled, boolean display, String icon,
			String description, String parentUuid, Integer position) {
		this.uuid = uuid;
		this.name = name;
		this.url = url;
		this.enabled = enabled;
		this.display = display;
		this.icon = icon;
		this.description = description;
		this.parentUuid = parentUuid;
		this.position = position;
	}

	

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name + " " + uuid + " " + parentUuid;
	}

}
