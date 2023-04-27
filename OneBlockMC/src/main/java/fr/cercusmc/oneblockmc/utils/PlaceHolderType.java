package fr.cercusmc.oneblockmc.utils;

public enum PlaceHolderType {
	
	NB_BLOCK("%nb%"),
	CURRENT_PHASE("%current_phase%"),
	NEXT_PHASE("%next_phase%"),
	PLAYER("%player%"),
	FILE_NAME("%fileName%"),
	MATERIAL_NAME("%materialName%"),
	BIOME_NAME("%biomeName%");
	
	
	private String placeHolderName;

	PlaceHolderType(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}
	
	public String getPlaceHolderName() {
		return placeHolderName;
	}

}
