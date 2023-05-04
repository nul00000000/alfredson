package net.schedge.cards.game;

public class Player {
	
	public final int id;
	public final int loginID;
	public LiveCard card;
	
	public Player(int id, int loginID) {
		this.id = id;
		this.loginID = loginID;
		this.card = null;
	}
	
	public void setCard(int card) {
		this.card = new LiveCard(Card.getCard(card));
	}

}
