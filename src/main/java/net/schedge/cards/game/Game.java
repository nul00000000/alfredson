package net.schedge.cards.game;

public class Game {
	
	private Player player1;
	private Player player2;
	
	private int nextID = 1;
	
	public Game() {
		this.player1 = null;
		this.player2 = null;
	}
	
	public int getNextID() {
		int r = nextID;
		nextID++;
		return r;
	}
	
	/***
	 * Attempts to add player to game
	 * @param player Player that is joining
	 * @return true if player was able to join successfully, false if not
	 */
	public boolean addPlayer(Player player) {
		if(player1 == null) {
			player1 = player;
			return true;
		} else if(player2 == null) {
			player2 = player;
			return true;
		} else {
			return false;
		}
	}
	
	public Player getPlayer(int id, int loginID) {
		if(player1.id == id && player1.loginID == loginID) {
			return player1;
		} else if(player2.id == id && player2.loginID == loginID) {
			return player2;
		} else {
			return null;
		}
	}
	
	public boolean isFull() {
		return player1 != null && player2 != null;
	}

}
