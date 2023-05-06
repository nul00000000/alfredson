package net.schedge.cards.game;

import net.schedge.cards.server.packet.AttackPacket;
import net.schedge.cards.server.packet.CardStatusPacket;
import net.schedge.cards.server.packet.GameStartPacket;
import net.schedge.cards.server.packet.Packet;
import net.schedge.cards.server.packet.PutCardPacket;

public class Game {
	
	private GameHandler handler;
	
	private Player player1;
	private Player player2;
	
	private int nextID = 1;
	
	//0 for not started, 1 for started, 2 for finished or stopped
	private int gameState = 0;
	
	public LiveCard[] inPlayCards;
	
	public Game(GameHandler handler) {
		this.player1 = null;
		this.player2 = null;
		this.handler = handler;
		this.inPlayCards = new LiveCard[6];
		for(int i = 0; i < 6; i++) {
			inPlayCards[i] = null;
		}
	}
	
	public int getNextID() {
		int r = nextID;
		nextID++;
		return r;
	}
	
	private void broadcastToGame(Packet packet) {
		handler.sendPacket(player1, packet);
		handler.sendPacket(player2, packet);
	}
	
	public void startGame() {
		handler.sendPacket(player1, new GameStartPacket(true));
		handler.sendPacket(player2, new GameStartPacket(false));
		for(int i = 0; i < 6; i++) {
			broadcastToGame(new PutCardPacket(i, -1));
		}
		gameState = 3;
	}
	
	public void stopGame() {
		if(player1 != null) {
			handler.removePlayer(player1);
		}
		if(player2 != null) {
			handler.removePlayer(player2);
		}
		handler.finishGame(this);
		gameState = 2;
	}
	
	public int getState() {
		return gameState;
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
			startGame();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFull() {
		return player1 != null && player2 != null;
	}
	
	public void processPacket(Player player, Packet packet) {
		if(packet instanceof PutCardPacket) {
			if((((PutCardPacket) packet).slot < 3 && player == player1) || (((PutCardPacket) packet).slot >= 3 && player == player2)) {
				inPlayCards[((PutCardPacket) packet).slot] = new LiveCard(((PutCardPacket) packet).cardID);
				this.broadcastToGame(new PutCardPacket(((PutCardPacket) packet).slot, ((PutCardPacket) packet).cardID));
				this.broadcastToGame(new CardStatusPacket(((PutCardPacket) packet).slot, inPlayCards[((PutCardPacket) packet).slot].getHealth()));
			}
		} else if(packet instanceof AttackPacket) {
			AttackPacket p = (AttackPacket) packet;
			if(((p.fromSlot < 3 && p.toSlot >= 3 && player == player1) || (p.fromSlot >= 3 && p.toSlot < 3 && player == player2))) {
				inPlayCards[p.fromSlot].giveAttack(inPlayCards[p.toSlot]);
				this.broadcastToGame(new CardStatusPacket(p.toSlot, inPlayCards[p.toSlot].getHealth()));
			}
		}
	}

}
