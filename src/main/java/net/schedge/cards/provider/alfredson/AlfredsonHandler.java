package net.schedge.cards.provider.alfredson;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.schedge.cards.Main;

public abstract class AlfredsonHandler implements HttpHandler {
	
	protected String rootLink = "https://schedge.net/alfredson/";
	
	public AlfredsonHandler() {
		if(Main.DEVENV) {
			rootLink = "http://localhost:8000/";
		}
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if(exchange.getRequestMethod().equals("GET")) {
			handleGet(exchange);
		} else if(exchange.getRequestMethod().equals("POST")) {
			handlePost(exchange);
		} else {
			handleOther(exchange);
		}
	}
	
	public abstract void handleGet(HttpExchange exchange) throws IOException;
	public abstract void handlePost(HttpExchange exchange) throws IOException;
	
	public void handleOther(HttpExchange exchange) throws IOException {
		OutputStream output = exchange.getResponseBody();
		
		String html = "We couldn't find what you were looking for, mostly because your client gave us a weird request";
		
		exchange.sendResponseHeaders(404, html.length());
		output.write(html.getBytes());
		output.flush();
		output.close();
	}

}
