package net.schedge.cards.server.packet;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface Packet {
	
	public String toJSON();
	
	public class PacketDeserializer implements JsonDeserializer<Packet> {

		@Override
		public Packet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			String type = obj.get("packetType").getAsString();
			if(type.equals("putCard")) {
				return context.deserialize(json, PutCardPacket.class);
			} else {
				return context.deserialize(json, DuncePacket.class);
			}
		}
		
	}

}
