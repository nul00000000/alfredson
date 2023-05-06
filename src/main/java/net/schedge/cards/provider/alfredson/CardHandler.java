package net.schedge.cards.provider.alfredson;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import net.schedge.cards.game.Card;
import net.schedge.cards.provider.json.JsonHandler;

public class CardHandler extends AlfredsonHandler {
	
	@Override
	public void handleGet(HttpExchange exchange) throws IOException {
		this.handleOther(exchange);
	}

	@Override
	public void handlePost(HttpExchange exchange) throws IOException {		
		JsonCardRequest req = JsonHandler.getObjectFromJson(new String(exchange.getRequestBody().readAllBytes()), JsonCardRequest.class);
		
		CardInfo card = Card.getCard(req.id).getInfo();
		String res;
		
		if(card != null) {
			res = JsonHandler.toJson(card);
		} else {
			res = "{\"attack\":\"if you're seeing this there is a big issue somewhere\"}";
		}
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(200, res.length());
		OutputStream output = exchange.getResponseBody();
		output.write(res.getBytes());
		output.flush();
		output.close();
	}
	
	public class JsonCardRequest {
		
		public int id;

	}


}
