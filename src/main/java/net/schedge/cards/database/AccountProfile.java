package net.schedge.cards.database;

public class AccountProfile {
	
	public int id;
	public String username;
	public String displayName;
	
	public AccountProfile(int id, String username, String displayName) {
		this.username = username;
		this.id = id;
		this.displayName = displayName;
	}
	
	public boolean isValid() {
		return id != 0;
	}

}
