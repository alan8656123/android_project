package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
	private static DatagramSocket socket;
	private static boolean running;
	
	private static int ClientID;
	private static ArrayList<ClientInfo> clients=new ArrayList<ClientInfo>();

	public static void start(int port) {
		try {
			socket = new DatagramSocket(port);
			
			running=true;
			listen();
			System.out.println("Server Started on Port, "+port);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void broadcast(String message) {
		for(ClientInfo info :clients) {
			send(message,info.getaddress(),info.getport());
			
		}
		
		
	}
	public static void send(String message,InetAddress address,int port) {
		try{
			message+="\\e";
			byte[] data =message.getBytes();
			DatagramPacket packet=new DatagramPacket(data,data.length,address,port);
			socket.send(packet);
			System.out.println("Send Message To,"+address.getHostAddress()+":"+port);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	public static void listen() {
		Thread listenThread=new Thread("ChatProgram Listener") {
			public void run() {
				try{
					while(running) {
						byte[] data =new byte[1024];
						DatagramPacket packet=new DatagramPacket(data,data.length);
						socket.receive(packet);
						
						String message = new String(data);
						message=message.substring(0,message.indexOf("\\e"));
						
						//Manage message
						if(!isCommand( message,packet)) {
							System.out.println("sdafsdafadadsf");
							broadcast(message);
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		};listenThread.start();	
	}
	
	
	/*
	 * 
	 * SERVER CONNECT LIST
	 * \con:[name] -> Connects Client to Server
	 * \dis:[id] -> DisConnects Client from Server
	 */
	private static boolean isCommand(String message,DatagramPacket packet) {
		if(message.startsWith("\\con:")) {
			//RUN CONNECTION CODE
			String name=message.substring(message.indexOf(":"));
			clients.add(new ClientInfo(name,ClientID++,packet.getAddress(),packet.getPort()));
			broadcast(name+",Connected");
			
			return true;
		}
		return false;
	}
	
	public static void stop() {
		running=false;
	}
}
