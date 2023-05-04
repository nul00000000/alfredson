package net.schedge.cards.provider;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import net.schedge.cards.Logger;
import net.schedge.cards.provider.alfredson.CardHandler;

public class CardServer implements Closeable {
	
	private HttpServer server;
	
	public CardServer(int port) {
		try {
			this.server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/cardinfo", new CardHandler());
			server.start();
			Logger.info("WebServer started on port " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		server.stop(5);
	}

	@Override
	public void close() {
		shutdown();
	}

}
