package server;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import shared.*;

public class Server {
	
	private int PORT = 6000;
	private ServerSocket serverSock;
	private DatagramSocket ds;
	
	static final String accountsTextFile = "resources/Accounts.txt";
	static File accountsFile = new File(accountsTextFile);
	static ArrayList<Chatroom> chatrooms = new ArrayList<Chatroom>(0);
	static ServerStreamingThread serverStreamingThread;
	static int listenerThreadCount = 0;
	
	public Server(){
		getUserInput();
		
		chatrooms.add(new Chatroom(0));
		chatrooms.add(new Chatroom(1));
		chatrooms.add(new Chatroom(2));
			
		receiveClients();
	}
	
	private void getUserInput(){
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true){
			System.out.println("Which port would you like to host the server on? [enter -1 to exit]");
			System.out.print("PORT: ");
			
			try	{PORT = Integer.parseInt(in.readLine());}
			catch (NumberFormatException e)  {
				System.out.println("Please enter a number value");
				continue;
				}
			catch (IOException e)  {
				System.out.println("Failed to read input");
				continue;
				}
			
			if (PORT == -1)
				System.exit(0);
			
			if (testPortChoice())
				return;
			System.out.println("failed to initialise server\n try another IP or PORT to host");
		}
	}
	
	private boolean testPortChoice(){
		try {
			initialiseServer();
		}
		catch (SocketException se){
			System.out.println("Could not bind port");
			return false;
		}
		catch (IOException ioe){
			System.out.println("General IO error");
			return false;
		}
		catch (IllegalArgumentException iae){
			System.out.println("PORT is out of range (0 to 65536)");
			return false;
		}
		finally{
			if (ds!=null) {
				ds.close();
			}
			ds = null;
		}
		return true;
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
	
	private void initialiseServer() throws SocketException, IOException, IllegalArgumentException 
	{
		System.out.println("starting server on PORT: " + PORT);
		serverSock = new ServerSocket(PORT);
		ds = new DatagramSocket(PORT);
		
	
	}
	
}
