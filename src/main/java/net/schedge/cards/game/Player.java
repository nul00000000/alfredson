package net.schedge.cards.game;

import org.java_websocket.WebSocket;

public class Player {
	
	public WebSocket connection;
	private Game game;
	
	public final int id = 0;
	public final int loginID = 0;
	public LiveCard card;
	
	public Player(WebSocket connection, Game game) {
		this.connection = connection;
		this.card = null;
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setCard(int card) {
		this.card = new LiveCard(Card.getCard(card));
	}

}
