package fr.cercusmc.oneblockmc.utils.enums;

public enum PlaceHolderType {
	
	NB_BLOCK("%nb%"),
	CURRENT_PHASE("%currentPhase%"),
	NEXT_PHASE("%nextPhase%"),
	PLAYER("%player%"),
	FILE_NAME("%fileName%"),
	MATERIAL_NAME("%materialName%"),
	BIOME_NAME("%biomeName%"),
	LOC_X("%x%"),
	LOC_Y("%y%"),
	LOC_Z("%z%"),
	LEVEL("%level%"),
	SYNTAX("%syntax%"),
	ISLAND_NAME("%islandName%"),
	ISLAND_OWNER("%islandOwner%"),
	ISLAND_RADIUS("%radius%"),
	PAGE("%page%"),
	MAX_PAGE("%maxPage%"),
	MEMBERS("%members%"),
	MONEY_REQUIRED("%moneyRequired%"),
	EXP_REQUIRED("%expRequired%"),
	LEVEL_XP_REQUIRED("%levelXpRequired%"),
	ISLAND_LEVEL_REQUIRED("%islandLevelRequired%"),
	CURRENT_RADIUS_MAX("%currentRadiusMax%"),
	NEXT_RADIUS("%nextRadius%");
	
	
	private String placeHolderName;

	PlaceHolderType(String placeHolderName) {
		this.placeHolderName = placeHolderName;
	}
	
	public String getPlaceHolderName() {
		return placeHolderName;
	}

}
