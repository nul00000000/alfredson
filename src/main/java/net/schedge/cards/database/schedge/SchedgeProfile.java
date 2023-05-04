package net.schedge.cards.database.schedge;

public class SchedgeProfile {
	
	public int id;
	public String username;
	public String displayName;
	
	public SchedgeProfile(int id, String username, String displayName) {
		this.username = username;
		this.id = id;
		this.displayName = displayName;
	}
	
	public boolean isValid() {
		return id != 0;
	}

}
