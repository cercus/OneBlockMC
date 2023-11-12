package fr.cercusmc.oneblockmc;

import java.util.UUID;

public class IslandInvite {
	
	private UUID owner;
	private UUID memberWhoWantJoin;
	
	public IslandInvite(UUID owner, UUID memberWhoWantJoin) {
		this.owner = owner;
		this.memberWhoWantJoin = memberWhoWantJoin;
	}
	
	public UUID getMemberWhoWantJoin() {
		return memberWhoWantJoin;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public void setMemberWhoWantJoin(UUID memberWhoWantJoin) {
		this.memberWhoWantJoin = memberWhoWantJoin;
	}
	
	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	
	

}
