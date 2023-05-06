package net.schedge.cards.server.packet;

public class CardStatusPacket extends Packet {
	
	public int slot;
	public int health;
	
	public CardStatusPacket(int slot, int health) {
		this.packetType = "cardStatus";
		this.slot = slot;
		this.health = health;
	}

}
