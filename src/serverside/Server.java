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
	private ArrayList<Chatroom> chatrooms;
	public static ArrayList<Client> clients;
	public static File accountsFile = new File("src/serverside/Accounts.txt");
	private ArrayList<ServerListenerThread> threads = new  ArrayList<ServerListenerThread>();
	
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
				System.out.println(threads);
				for (ServerListenerThread thread: threads)
				{
					System.out.println("Thread " + thread.threadId() + " alive state is " + thread.isAlive());
				}
				tempClientHolder = serverSock.accept();
				System.out.println("client connection from " + tempClientHolder.toString());
				Client client = new Client(tempClientHolder);	
				ServerListenerThread listenerThread = new ServerListenerThread(client, this);
				threads.add(listenerThread);
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
	
	public ArrayList<Chatroom> getChatroom()
	{
		return this.chatrooms;
	}
}
