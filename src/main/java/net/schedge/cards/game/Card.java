package net.schedge.cards.game;

import net.schedge.cards.Logger;
import net.schedge.cards.provider.alfredson.CardInfo;

public class Card {
	
private final static Card[] cards = new Card[3];
	
	public final static Card TEST_MAN;
	public final static Card CALC_BC_STUDENT;
	public final static Card ERROR_CARD;
	
	public final static String[] SPECIAL_DEFAULTS;
	public final static String[] DEFENSE_DEFAULTS;
	
	static {
		SPECIAL_DEFAULTS = new String[] {
				"DISCARD all applied cards on both sides of the field", // 0
				"DISCARD cards in opponents hand", //1
				"DISCARD all opponent cards on field", //2
				"SELF DESTRUCT - Sacrifice your character to eliminate any other character of any HP", //3
				"REVIVE a weapon and apply it", //4
				"REVIVE character as replacement", //5
				"REVIVE character as a \"zombie\"", //6
				"RECOVER HP", //7
				"STEAL an opponent's card", //8
				"IMMUNITY for a number of turns", //9
				"MIMIC - Steal any character's attack", //10
				"SEE face down card", // 11
				"SEE opponent's hand", //12
				"2-FOR-1 attack", //13
				"HEALING - Add some HP per turn", //14
				"SHUFFLE the line-up", //15
				"VANQUISH a character", //16
				"STUNNER - opponent cannot attack for a number of turns", //17
				"SPREAD attack to multiple players" //18
		};
		
		DEFENSE_DEFAULTS = new String[] {
				"AVOID first strike", // 0
				"AVOID ${dmg} damage from each attack", //1
				"AVOID particular weapons", //2
				"AVOID attacks from particular characters", //3
				"PARTNER character avoids ${dmg} damage from each attack", //4
		};
		
		//error card should not be registered
		ERROR_CARD = new Card(-1, 404, 0, 200, 0, 14);
		ERROR_CARD.setInfo("Card not found", "Error", "Error Code",
				"Your client sent a weird request and asked for an invalid card ID",
				"No Worky - Errors usually mean things don't worky",
				"Reload - idk it usually fixes any issues in general",
				"Idk frfr what joke to make here",
				"https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/Minecraft_missing_texture_block.svg/1200px-Minecraft_missing_texture_block.svg.png");
		
		TEST_MAN = registerCard(0, "Test Man", "villian", 69, "Human", 
				"He was just testing something until he wasn't", 
				"Exam Day - Daze the enemy with math questions", 30, 
				"Extra Credit - Minimize your losses with test corrections", 10, 1,
				"Cheating - Steal answers from another person", 8,
				"https://www.oxfordlearning.com/wp-content/uploads/2018/10/types-of-test-takers-study-tips.jpeg");
		CALC_BC_STUDENT = registerCard(1, "Calc BC Student", "villian", 100, "Human", 
				"Lorem ipsum dolor sit amet, in Calculo classis nimis diu sedit. I need more text so that I can test this with three lines", 
				"Integral Incinerate - Continuously add infinitesimally small changes in dFire in respect to dt to burn your opponent", 10, 
				"Differentiation Dodge - Compute the slope at a given point in order to slide away from your opponent's attack", 20, 0,
				"Diverge Series - Split the comic series into mulitple timelines, one of which you win. I am once again asking for a third line", 13,
				"https://schedge.net/alfredson/assests/schmacomo.png");
	}
	
	public final int id;
	public final int health;
	public final int attackDamage;
	public final int defenseDamage;
	public final int defenseType;
	public final int specialType;
	
	private CardInfo info;
		
	private Card(int id, int health, int attackDamage, int defenseDamage, int defenseType, int specialType) {
		this.id = id;
		this.health = health;
		this.attackDamage = attackDamage;
		this.defenseDamage = defenseDamage;
		this.defenseType = defenseType;
		this.specialType = specialType;
	}
	
	public void setInfo(String name, String characterType, String cardType, String originStory, String attack, String defense, String special, String cardArt) {
		this.info = new CardInfo(this.id, name, characterType, this.health, cardType, originStory, attack, this.attackDamage, defense, this.defenseDamage, special, cardArt);
	}
	
	public CardInfo getInfo() {
		return info;
	}
	
	public static Card registerCard(int id, String name, String characterType, int health, String cardType, String originStory,
			String attack, int attackDamage, String defense, int defenseDamage, int defenseType, String special, int specialType, String cardArt) {
		Card card = new Card(id, health, attackDamage, defenseDamage, defenseType, specialType);
		card.setInfo(name, characterType, cardType, originStory, attack, defense, special, cardArt);
		if(cards[card.id] != null) {
			Logger.error("Card already registered for id " + card.id);
		}
		cards[card.id] = card;
		return card;
	}
	
	public static Card registerCard(int id, String name, String characterType, int health, String cardType, String originStory,
			String attack, int attackDamage, String defense, int defenseDamage, int defenseType, int specialType, String cardArt) {
		Card card = new Card(id, health, attackDamage, defenseDamage, defenseType, specialType);
		card.setInfo(name, characterType, cardType, originStory, attack, defense, SPECIAL_DEFAULTS[specialType], cardArt);
		if(cards[card.id] != null) {
			Logger.error("Card already registered for id " + card.id);
		}
		cards[card.id] = card;
		return card;
	}
	
	public static Card registerCard(int id, String name, String characterType, int health, String cardType, String originStory,
			String attack, int attackDamage, int defenseDamage, int defenseType, String special, int specialType, String cardArt) {
		Card card = new Card(id, health, attackDamage, defenseDamage, defenseType, specialType);
		card.setInfo(name, characterType, cardType, originStory, attack, DEFENSE_DEFAULTS[defenseType].replaceAll("\\$\\{dmg\\}", defenseDamage + ""), special, cardArt);
		if(cards[card.id] != null) {
			Logger.error("Card already registered for id " + card.id);
		}
		cards[card.id] = card;
		return card;
	}
	
	public static Card registerCard(int id, String name, String characterType, int health, String cardType, String originStory,
			String attack, int attackDamage, int defenseDamage, int defenseType, int specialType, String cardArt) {
		Card card = new Card(id, health, attackDamage, defenseDamage, defenseType, specialType);
		card.setInfo(name, characterType, cardType, originStory, attack, DEFENSE_DEFAULTS[defenseType].replaceAll("\\$\\{dmg\\}", defenseDamage + ""), 
				SPECIAL_DEFAULTS[specialType], cardArt);
		if(cards[card.id] != null) {
			Logger.error("Card already registered for id " + card.id);
		}
		cards[card.id] = card;
		return card;
	}
	
	public static Card getCard(int id) {
		if(id < cards.length) {
			return cards[id];
		} else {
			return ERROR_CARD;
		}
	}

}
