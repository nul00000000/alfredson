package net.schedge.cards.server.packet;

public class GameStartPacket extends Packet {
	
	public boolean head;
	
	public GameStartPacket(boolean head) {
		this.packetType = "gameStart";
		this.head = head;
	}

}
