package server;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import shared.*;

public class Server {
	
	private final int PORT = 6000;
	private ServerSocket serverSock;
	
	static final String accountsTextFile = "resources/Accounts.txt";
	static File accountsFile = new File(accountsTextFile);
	static ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>(0);
	static ServerStreamingThread serverStreamingThread;
	static int listenerThreadCount = 0;
	
	public Server()
	{
		initialiseServer();
		
		chatrooms.add(new Chatroom(0));
		chatrooms.add(new Chatroom(1));
		chatrooms.add(new Chatroom(2));
			
		receiveClients();
	}
	
	private void receiveClients()
	{
		System.out.println("listening for clients...");
		serverStreamingThread = new ServerStreamingThread();
		serverStreamingThread.start();
		Socket tempClientHolder = null;
		
		while (true) {
			try {
				tempClientHolder = serverSock.accept();
				System.out.println("client connection from " + tempClientHolder.toString());
				Client client = new Client(tempClientHolder);	
				Thread listenerThread = new ServerListenerThread(client);
				listenerThread.start();
				ServerStreamingThread.addClient(client);
				listenerThreadCount++;
			}
			catch (Exception e) {
				System.out.println("client connection request rejected");
				e.printStackTrace();
			}
			finally {
				System.out.println("listening for next client...");
			}
		}
	}
	
	private void initialiseServer()
	{
		try {
			System.out.println("starting server on PORT: " + PORT);
			serverSock = new ServerSocket(PORT);
		}
		catch (IOException e) {
			System.out.println("failed to initialise server");
			System.exit(0);
		}
	}
	
}
