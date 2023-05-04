package net.schedge.cards.server.json;

public class JoinResponse {
	
	public int errorCode;
	public int playerID;
	
	public JoinResponse(int code, int playerID) {
		this.errorCode = code;
		this.playerID = playerID;
	}

}
