package serverside;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import misc.Chatroom;
import misc.Client;

public class Server {
	
	public static void main(String args[]) throws IOException
	{
		Server server = new Server();
	}
	
	private final int PORT = 6000;
	private ServerSocket serverSock = null;
	//remove clients when chatrooms implementation is done
	public static ArrayList<Chatroom> chatrooms;
	public static ArrayList<Client> clients;
	
	public Server() throws IOException
	{
		initServer();	
		//will replace with chatrooms when ready
		clients = new ArrayList<Client>();
		chatrooms = new ArrayList<Chatroom>();
		
		//make 3 random chatrooms for now since we dont have a way to make chatrooms yet
		chatrooms.add(new Chatroom(0));
		chatrooms.add(new Chatroom(1));
		chatrooms.add(new Chatroom(2));
		
		Client fakeClient1 = new Client();
		fakeClient1.setUsername("gaylord");
		Client fakeClient2 = new Client();
		fakeClient2.setUsername("Harry");
		Client fakeClient3 = new Client();
		fakeClient3.setUsername("Ben");
		
		chatrooms.get(0).addClient(fakeClient1);
		chatrooms.get(0).addClient(fakeClient2);
		chatrooms.get(1).addClient(fakeClient3);
		
		receiveClients();
	}
	
	private void receiveClients()
	{
		System.out.println("listening for clients...");
		Socket tempClientHolder = null;
		while (true)
		{
			try
			{
				tempClientHolder = serverSock.accept();
				System.out.println("client connection from " + tempClientHolder.toString());
				Client client = new Client(tempClientHolder);	
				ServerListenerThread listenerThread = new ServerListenerThread(client);
				listenerThread.start();
				
			}
			catch (Exception e)
			{
				System.out.println("client connection request rejected");
				e.printStackTrace();
			}
			finally
			{
				System.out.println("listening for next client...");
			}
		}
	}
	
	private void initServer() throws IOException
	{
		System.out.println("starting server on PORT: " + PORT);
		serverSock = new ServerSocket(PORT);
		System.out.println("success!");
	}
	
}
