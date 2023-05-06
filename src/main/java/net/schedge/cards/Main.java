package net.schedge.cards;

import java.io.Closeable;
import java.net.InetSocketAddress;

import net.schedge.cards.game.GameHandler;
import net.schedge.cards.provider.CardServer;
import net.schedge.cards.server.Server;

public class Main {
	
	public static boolean DEVENV = false;
	
	public static void main(String[] args) {
		Thread serverThread = null;
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
			server = new Server(new InetSocketAddress(8686));
			serverThread = new Thread((Server) server);
			serverThread.start();
		} else {
			server = new Server(new InetSocketAddress(port)); 
			serverThread = new Thread((Server) server);
			serverThread.start();
		}
		
		if(dual || !cardServer) {
			Server gameServer = (Server) server;
			GameHandler games = new GameHandler(gameServer);
			try {
				double TPS = 60;
				double SPT = 1.0 / TPS;
				double a = 0;
				double b = 0;
				while(true) {
					a = System.nanoTime() / 1000000000.0;
					games.update();
					b = System.nanoTime() / 1000000000.0 - a;
					if(b < SPT) {
						try {
							Thread.sleep((long) ((SPT - b) * 1000));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} finally {
				gameServer.close();
			}
		} else {
			CardServer cServer = (CardServer) server;
			try {
				while(true) {
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) {
				Logger.warn("Main thread interrupted, exiting");
			} finally {
				cServer.close();
			}
		}
	}

}
