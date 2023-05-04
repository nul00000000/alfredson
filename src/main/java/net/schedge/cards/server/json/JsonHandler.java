package net.schedge.cards.server.json;

import com.google.gson.Gson;

import net.schedge.cards.database.schedge.SchedgeProfile;

public class JsonHandler {
	
	private static Gson gson;
	
	static {
		gson = new Gson();
	}
	
	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
	
	public static SchedgeProfile getSchedgeProfileFromJson(String json) {
		return gson.fromJson(json, SchedgeProfile.class);
	}
	
	public static <T> T getObjectFromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
	
	public static JsonRequest getRequestFromJson(String json) {
		return gson.fromJson(json, JsonRequest.class);
	}

}
