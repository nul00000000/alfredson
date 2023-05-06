package net.schedge.cards.game;

import java.util.ArrayList;

import org.java_websocket.WebSocket;

import net.schedge.cards.Logger;
import net.schedge.cards.server.Server;
import net.schedge.cards.server.packet.Packet;
import net.schedge.cards.server.packet.VerifyPacket;

public class GameHandler {
	
	private Server server;
	private ArrayList<WebSocket> queue;
	private ArrayList<Player> players;
	
	private ArrayList<Game> games;
	private Game gameToFill;
	
	public GameHandler(Server server) {
		this.server = server;
		this.queue = new ArrayList<>();
		this.players = new ArrayList<>();
		this.games = new ArrayList<>();
		this.gameToFill = new Game(this);
	}
	
	public void registerConnection(WebSocket conn) {
		Player p = new Player(conn, gameToFill);
		gameToFill.addPlayer(p);
		if(gameToFill.isFull()) {
			this.games.add(gameToFill);
			gameToFill = new Game(this);
		}
		this.players.add(p);
	}
	
	public void finishGame(Game game) {
		this.games.remove(game);
	}
	
	public void removePlayer(Player player) {
		Logger.info("[LEAVE] (" + player.connection.getRemoteSocketAddress().getHostString() + ")");
		this.players.remove(player);
		server.closeConnection(player.connection, 0);
	}
	
	public void sendPacket(WebSocket conn, Packet packet) {
		server.sendPacket(conn, packet);
	}
	
	public void sendPacket(Player player, Packet packet) {
		server.sendPacket(player.connection, packet);
	}
	
	private void checkConnections() {
		for(int i = 0; i < players.size(); i++) {
			Player pl = players.get(i);
			WebSocket conn = pl.connection;
			if(conn.isClosed()) {
				pl.getGame().stopGame();
				Logger.debug("games running: " + games.size());
				i--;
				continue;
			}
		}
		{
			WebSocket con = server.acceptConnection();
			int a = 0;
			while(con != null && a < 100) {
				queue.add(con);
				con = server.acceptConnection();
				a++;
			}
		}
		for(int i = 0; i < queue.size(); i++) {
			WebSocket con = queue.get(i);
			int h = server.available(con);
			if(h == 1) {
				Packet p = server.readPacket(con);
				if(p instanceof VerifyPacket) {
						registerConnection(con);
						Logger.info("[JOIN] (" + con.getRemoteSocketAddress().getHostString() + ")");
						queue.remove(i);
						i--;
				} else {
					server.backlogPacket(con, p);
				}
			} else if(h == -1) {
				queue.remove(i);
				i--;
			} else if(h == 0) {
				//do nothing
			} else {
				Logger.debug("Unrecognized availablity status: " + h);
			}
		}
	}
	
	public void update() {
		checkConnections();
		
		for(int i = 0; i < players.size(); i++) {
			Player pl = players.get(i);
			WebSocket conn = pl.connection;
			while(server.available(conn) > 0) {
				pl.getGame().processPacket(pl, server.readPacket(conn));
			}
		}
//		for(int i = 0; i < games.size(); i++) {
//			games.get(i).update();
//		}
	}

}
