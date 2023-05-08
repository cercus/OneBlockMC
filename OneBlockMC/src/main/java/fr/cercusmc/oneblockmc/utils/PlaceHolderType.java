package fr.cercusmc.oneblockmc.utils;

public enum PlaceHolderType {
	
	NB_BLOCK("%nb%"),
	CURRENT_PHASE("%current_phase%"),
	NEXT_PHASE("%next_phase%"),
	PLAYER("%player%"),
	FILE_NAME("%fileName%"),
	MATERIAL_NAME("%materialName%"),
	BIOME_NAME("%biomeName%"),
	LOC_X("%x%"),
	LOC_Y("%y%"),
	LOC_Z("%z%"),
	LEVEL("%level%"),
	SYNTAX("%syntax%");
	
	
	private String placeHolderName;

	PlaceHolderType(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}
	
	public String getPlaceHolderName() {
		return placeHolderName;
	}

}
