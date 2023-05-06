package net.schedge.cards.game;

public class LiveCard {
	
	private Card cardBase;
	
	private int health;
	
	public LiveCard(Card cardBase) {
		this.cardBase = cardBase;
		this.health = cardBase.health;
	}
	
	public LiveCard(int cardBaseID) {
		this(Card.getCard(cardBaseID));
	}
	
	public int getID() {
		return cardBase.id;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void giveAttack(LiveCard to) {
		to.receiveAttack(this, this.cardBase.attackDamage);
	}
	
	public void receiveAttack(LiveCard from, int damage) {
		this.health -= (damage); //we are temporarily ~~assuming all defenses are #1s~~ assuming no defense :)
	}

}
