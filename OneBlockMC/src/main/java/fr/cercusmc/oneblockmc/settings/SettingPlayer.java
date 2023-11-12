package fr.cercusmc.oneblockmc.settings;

public class SettingPlayer {
	
	private String role;
	private String settingName;
	private Boolean value;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean value) {
		this.value = value;
	}
	public SettingPlayer(String role, String settingName, Boolean value) {
		super();
		this.role = role;
		this.settingName = settingName;
		this.value = value;
	}
	
	

}
