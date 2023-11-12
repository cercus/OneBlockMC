package fr.cercusmc.oneblockmc.settings;

import java.util.Objects;

import org.bukkit.Material;

public class Setting {
	
	private String rank;
	private Material icon;
	private String description;
	private boolean defaultValue;
	private String setting;
	
	
	public Setting(String rank, Material icon, String description, String setting, boolean defaultValue) {
		super();
		this.rank = rank;
		this.icon = icon;
		this.description = description;
		this.defaultValue = defaultValue;
		this.setting = setting;
	}
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	@Override
	public String toString() {
		return "Setting{rank=" + rank + ", icon=" + icon + ", description=" + description + ", defaultValue="
				+ defaultValue + ", setting=" + setting + "}";
	}
	@Override
	public int hashCode() {
		return Objects.hash(defaultValue, description, icon, rank, setting);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		return defaultValue == other.defaultValue && Objects.equals(description, other.description)
				&& icon == other.icon && Objects.equals(rank, other.rank) && Objects.equals(setting, other.setting);
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Material getIcon() {
		return icon;
	}
	public void setIcon(Material icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

}
