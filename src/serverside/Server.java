package serverside;
import java.net.*;
import java.util.ArrayList;

import misc.Chatroom;

public class Server {
	
	public static void main(String args[])
	{
		Server server = new Server();
	}
	
	private final int PORT = 6000;
	private ServerSocket serverSock = null;
	static ArrayList<Client> clients;
	static ArrayList<Chatroom> chatrooms;
	
	public Server()
	{
		initServer();	
		clients = new ArrayList<Client>();
		chatrooms = new ArrayList<Chatroom>();
		
		//make 3 random chatrooms for now since we dont have a way to make chatrooms yet
		chatrooms.add(new Chatroom(0));
		chatrooms.add(new Chatroom(1));
		chatrooms.add(new Chatroom(2));
		
		receiveClients();
	}
	
	private void receiveClients()
	{
		Socket tempClientHolder = null;
		
		while (true)
		{
			System.out.println("listening for clients...");
			try
			{
				tempClientHolder = serverSock.accept();
				
				System.out.println("client connection from " + tempClientHolder.toString());
				Client client = new Client(tempClientHolder);	
				clients.add(client);
				ServerListenerThread listenerThread = new ServerListenerThread(client);
				listenerThread.start();
				
			}
			catch (Exception e)
			{
				System.out.println("exception caught!");
				System.out.println("client connection request rejected");
				e.printStackTrace();
			}
			finally
			{
				System.out.println("listening for next client...");
			}
		}
	}
	
	private void initServer()
	{
		System.out.println("starting server on PORT: " + PORT);
		try
		{
			serverSock = new ServerSocket(PORT);
			System.out.println("success!");
		}
		catch(Exception e)
		{
			System.out.println("failed to initialise the server!");
			e.printStackTrace();
			System.exit(0);
		}
		finally
		{
			System.out.println("initServer method complete\n- - - - -");
		}
	}
	
}
