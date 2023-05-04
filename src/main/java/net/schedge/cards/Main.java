package net.schedge.cards;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.schedge.cards.game.Game;
import net.schedge.cards.provider.CardServer;
import net.schedge.cards.server.Server;

public class Main {
	
	public static boolean DEVENV = false;
	
	public static void main(String[] args) {
		int port = 8686;
		boolean portSet = false;
		boolean cardServer = false;
		boolean dual = false;
		Closeable server = null;
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-d")) {
				DEVENV = true;
				Logger.outLevel = Logger.DEBUG;
				Logger.onlyErrors = false;
			}
			if(args[i].equals("-cardServer")) {
				if(!portSet) {
					port = 8383;
				}
				cardServer = true;
			}
			if(args[i].equals("-dual")) {
				dual = true;
			}
			if(args[i].equals("-p") && args.length >= i + 2) {
				try {
					port = Integer.parseInt(args[i + 1]);
					portSet = true;
				} catch(NumberFormatException e) {}
				i++;
			}
		}
//		SchedgeDatabase schedgeDatabase = new SchedgeDatabase();
		if(cardServer) {
			server = new CardServer(port);
		} else if (dual) {
			server = new CardServer(8383);
			Game game = new Game();
			server = new Server(new InetSocketAddress(8686), game, null);
		} else {
			Game game = new Game();
			server = new Server(new InetSocketAddress(port), game, null); //if you are getting a nullpointerexception it means you finally used
																		  //logins and this null should be the database (and remember to close it in the finally)
		}
		try {
			while(true) {
				Thread.sleep(60000);
			}
		} catch (InterruptedException e) {
			Logger.warn("Main thread interrupted, exiting");
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
