package net.schedge.cards.server.packet;

public class PutCardPacket extends Packet {
	
	public int cardID;
	public int slot;
	
	public PutCardPacket(int slot, int cardID) {
		this.packetType = "putCard";
		this.cardID = cardID;
		this.slot = slot;
	}

}
