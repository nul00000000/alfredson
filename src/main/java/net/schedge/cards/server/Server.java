package net.schedge.cards.server;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.schedge.cards.Logger;
import net.schedge.cards.server.packet.Packet;
import net.schedge.cards.server.packet.PacketDeserializer;

public class Server extends WebSocketServer implements Closeable {
	
	private ArrayList<WebSocket> incomingConnections;
	public ArrayList<WebSocket> outgoingConnections;
	private HashMap<WebSocket, ArrayList<Packet>> incoming;
		
	private long totalBytes = 0;
	private long lastBitrateCheck;
	
	private Gson gson;
	
	public Server(InetSocketAddress address) {
		super(address);
		this.incoming = new HashMap<>();
		this.incomingConnections = new ArrayList<>();
		this.outgoingConnections = new ArrayList<>();
		this.lastBitrateCheck = System.currentTimeMillis();
		
		GsonBuilder builder = new GsonBuilder();
		PacketDeserializer des = new PacketDeserializer();
		builder.registerTypeAdapter(Packet.class, des);
		this.gson = builder.create();
	}
	
	public double getBitrate() {
		long a = System.currentTimeMillis();
		double r = (double) totalBytes / (a - lastBitrateCheck) * 8000;
		totalBytes = 0;
		lastBitrateCheck = a;
		return r;
	}
	
	public void shutdown(int closecode) {
		for(WebSocket ws : incoming.keySet()) {
			ws.close();	
		}
		try {
			this.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(WebSocket con, int closeCode) {
		con.close();
		incoming.remove(con);
	}
	
	public void shutdown() {
		this.shutdown(CloseFrame.NORMAL);
	}
	
	/**
	 * @return The connected client, or null if none was found
	 */
	public WebSocket acceptConnection() {
		if(incomingConnections.size() > 0) {
			WebSocket ws = incomingConnections.get(0);
			incomingConnections.remove(0);
			return ws;
		} else {
			return null;
		}
	}
	
	public void backlogPacket(WebSocket s, Packet packet) {
		incoming.get(s).add(packet);
	}
	
	/**
	 * @param to
	 * @param packet
	 * @return true if packet was sent successfully, false if not, suggesting the connection was closed
	 */
	public boolean sendPacket(WebSocket to, Packet packet) {
		try {
			to.send(gson.toJson(packet));
			return true;
		} catch(IllegalArgumentException e) {
			Logger.debug("tried to send null data from " + packet);
			return false;
		} catch(WebsocketNotConnectedException e) {
			Logger.debug("tried to send data to unconnected websocket (" + packet.toString() + ")");
			return false;
		}
	}
	
	public void broadcastPacket(Packet packet) {
		for(WebSocket s : incoming.keySet()) {
			s.send(gson.toJson(packet));
		}
	}
	
	/**
	 * Only checks that there is some unread data, not that a full packet is ready
	 * @return if a packet is ready to be read, if this is false, {@code readPacket()} will block until one arrives
	 */
	public int available(WebSocket from) {
		ArrayList<Packet> p = incoming.get(from);
		if(p == null) {
			return -1;
		} else {
			return p.size() > 0 ? 1 : 0;
		}
	}
			
	/**
	 * Will block until full packet is received
	 * @return the next Packet sent from the server
	 */
	public Packet readPacket(WebSocket from) {
		Packet r = incoming.get(from).get(0);
		incoming.get(from).remove(0);
		return r;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		incoming.put(conn, new ArrayList<>());
		incomingConnections.add(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		incomingConnections.remove(conn); //returns true if connection has not been accepted
		incoming.remove(conn);
		if(remote) {
			outgoingConnections.add(conn);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		incoming.get(conn).add(gson.fromJson(message, Packet.class));
	}
	
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		incoming.get(conn).add(gson.fromJson(new String(message.array()), Packet.class));
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {
		Logger.info("Server started on " + this.getAddress());
	}

	@Override
	public void close() {
		shutdown();
	}

}
