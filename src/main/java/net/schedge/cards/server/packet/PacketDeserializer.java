package net.schedge.cards.server.packet;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PacketDeserializer implements JsonDeserializer<Packet> {
	
	@Override
	public Packet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String type = obj.get("packetType").getAsString();
		if(type.equals("putCard")) {
			return context.deserialize(json, PutCardPacket.class);
		} else if(type.equals("verify")) {
			return context.deserialize(json, VerifyPacket.class);
		} else if(type.equals("gameStart")) {
			return context.deserialize(json, GameStartPacket.class);
		} else if(type.equals("cardStatus")) {
			return context.deserialize(json, CardStatusPacket.class);
		} else if(type.equals("attack")) {
			return context.deserialize(json, AttackPacket.class);
		} else {
			return context.deserialize(json, DuncePacket.class);
		}
	}

}
