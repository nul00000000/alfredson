package net.schedge.cards.server.packet;

public class DuncePacket extends Packet {
	
	public String info;
	
	public DuncePacket(String info) {
		this.packetType = "dunce";
		this.info = info;
	}

}
