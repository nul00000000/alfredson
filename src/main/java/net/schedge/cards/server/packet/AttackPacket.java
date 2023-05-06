package net.schedge.cards.server.packet;

public class AttackPacket extends Packet {
	
	public int fromSlot;
	public int toSlot;
	
	public AttackPacket(int fromSlot, int toSlot) {
		this.packetType = "attack";
		this.fromSlot = fromSlot;
		this.toSlot = toSlot;
	}

}
