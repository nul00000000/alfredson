package net.schedge.cards.provider.alfredson;

public class CardInfo {
	
	public int id;
	public String name;
	public String characterType;
	public int health;
	public String cardType;
	public String originStory;
	public String attack;
	public int attackDamage;
	public String defense;
	public int defenseDamage;
	public String special;
	public String cardArt;
	
	public CardInfo(int id, String name, String characterType, int health, String cardType, String originStory,
			String attack, int attackDamage, String defense, int defenseDamage, String special, String cardArt) {
		super();
		this.id = id;
		this.name = name;
		this.characterType = characterType;
		this.health = health;
		this.cardType = cardType;
		this.originStory = originStory;
		this.attack = attack;
		this.attackDamage = attackDamage;
		this.defense = defense;
		this.defenseDamage = defenseDamage;
		this.special = special;
		this.cardArt = cardArt;
	}
	
}
