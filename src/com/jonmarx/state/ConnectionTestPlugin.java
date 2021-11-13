package com.jonmarx.state;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.jonmarx.game.GameState;
import com.jonmarx.net.TCPConnectionNIO;
import com.jonmarx.plugin.Plugin;
import com.jonmarx.net.Packet;
import com.jonmarx.net.Connection;
import com.jonmarx.net.NullPacket;

public class ConnectionTestPlugin implements Plugin {
	private ServerSocketChannel server;
	private ArrayList<Connection> connections = new ArrayList<>();
	
	public void onInit() {
		System.out.println("Starting up Server Plugin.");
		try {
			server = ServerSocketChannel.open();
			server.bind(new InetSocketAddress(1053));
			server.configureBlocking(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpdate() {
		// find new clients
		while(true) {
			SocketChannel channel = null;
			try {
				channel = server.accept();
				if(channel == null) break;
				
				channel.finishConnect();
				connections.add(new TCPConnectionNIO(channel));
				System.out.println("Added Connection!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// loop through existing clients, read messages
		for(Connection conn : connections) {
			if(conn.isClosed()) {
				connections.remove(conn);
				System.out.println("much sad, disconnected");
				continue;
			}
			while(true) {
				try {
					Packet packet = conn.getMessage();
					if(packet instanceof NullPacket) break;
					System.out.println(packet.getClass().getName());
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}
