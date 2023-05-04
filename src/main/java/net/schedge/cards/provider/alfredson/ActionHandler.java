package net.schedge.cards.provider.alfredson;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import net.schedge.cards.Logger;
import net.schedge.cards.database.schedge.SchedgeDatabase;
import net.schedge.cards.game.Game;
import net.schedge.cards.game.Player;
import net.schedge.cards.server.json.JoinResponse;
import net.schedge.cards.server.json.JsonHandler;
import net.schedge.cards.server.json.JsonRequest;
import net.schedge.cards.server.json.SetCardResponse;

public class ActionHandler extends AlfredsonHandler {
	
	public static final int ERROR_SESSION_INVALID = 1;
	public static final int ERROR_GAME_FULL = 2;
	public static final int ERROR_PLAYER_NOT_PRESENT = 3;
	public static final int ERROR_ACTION_INVALID = 4;
	public static final int ERROR_ACTION_DATA_INVALID = 5;
	
	private Game game;
	private SchedgeDatabase db;
	
	public ActionHandler(Game game, SchedgeDatabase db) {
		this.game = game;
		this.db = db;
	}

	@Override
	public void handleGet(HttpExchange exchange) throws IOException {
		this.handleOther(exchange);
	}
	
	private int validateAndGetID(Headers headers) {
		if(headers.get("Cookie") == null) {
			return 0;
		}
		String[] cookies = headers.get("Cookie").get(0).split("; ");
		int id = 0;
		int token = 0;
		for(String c : cookies) {
			String[] parts = c.split("=");
			if(parts.length != 2) {
				Logger.warn("Client may be cracked, cookie data invalid"); //fortnite building montage
				return 0;
			}
			if(parts[0].equals("userId")) {
				id = Integer.parseInt(parts[1]);
			} else if(parts[0].equals("token")) {
				token = Integer.parseInt(parts[1]);
			}
		}
		boolean valid = db.validatePair(id, token);
		if(!valid) {
			return 0;
		}
		return id;
	}

	@Override
	public void handlePost(HttpExchange exchange) throws IOException {
		JsonRequest req = JsonHandler.getRequestFromJson(new String(exchange.getRequestBody().readAllBytes()));
		
		String res;
		if(req.type.equalsIgnoreCase("join")) {
			int id = this.validateAndGetID(exchange.getRequestHeaders());
			int playerID = 0;
			if(id == 0) {
				res = JsonHandler.toJson(new JoinResponse(ERROR_SESSION_INVALID, 0));
			} else {
				playerID = game.getNextID();
				if(game.addPlayer(new Player(playerID, id))) {
					res = JsonHandler.toJson(new JoinResponse(0, playerID));
				} else {
					res = JsonHandler.toJson(new JoinResponse(ERROR_GAME_FULL, 0));
				}
			}
//			else if(game.getPlayer(id) != null) {
//				res = JsonHandler.toJson(new JoinResponse("Player already in game", 2));
//			}
		} else if(req.type.equalsIgnoreCase("setCard")) {
			int id = this.validateAndGetID(exchange.getRequestHeaders());
			Player p = game.getPlayer(req.playerID, id);
			if(id == 0) {
				res = JsonHandler.toJson(new SetCardResponse(ERROR_SESSION_INVALID));
			} else if(p == null) {
				res = JsonHandler.toJson(new SetCardResponse(ERROR_PLAYER_NOT_PRESENT));
			} else if(p.card != null) {
				res = JsonHandler.toJson(new SetCardResponse(ERROR_ACTION_INVALID));
			} else if(req.params.length < 0 || !(req.params[0] instanceof Integer)) {
				res = JsonHandler.toJson(new SetCardResponse(ERROR_ACTION_DATA_INVALID));
			} else {
				res = JsonHandler.toJson(new SetCardResponse(0));
				p.setCard((int) req.params[0]);
			}
		} else {
			res = "{\"prank\":\"em john\"}";
		}
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(200, res.length());
		OutputStream output = exchange.getResponseBody();
		output.write(res.getBytes());
		output.flush();
		output.close();
	}

}
