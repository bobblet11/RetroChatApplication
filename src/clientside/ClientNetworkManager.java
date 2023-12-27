package clientside;
import java.net.*;
import java.io.*;
import java.util.*;
import misc.Message;
import serverside.Server;
import misc.Chatroom;
import misc.Client;

public class ClientNetworkManager extends Client{
	
	private final String IP = "127.0.0.1";
	private final int PORT = 6000;	
	static ArrayList<ArrayList<String>> chatroomList;
	//request chatrooms whenver need to,
	
	public ClientNetworkManager()
	{
		connect();
	}
	
	public boolean attemptLogIn(String username, String password) throws IOException
	{
		Message logInRequest = new Message(username+DELIMETER+password, username, Message.LOGIN_REQUEST);
		sendMessage(logInRequest);
		
		Message response = readMessage();
		if (response.logInIsApproved())
		{
			this.username = username;
			this.password = password;
			return true;
		}
		return false;
	}
		
	private void connect()
	{
		System.out.println("connecting to,\nIP: " + IP + "\n PORT: " + PORT);
		try
		{
			socket = new Socket(IP, PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("success!");
		}
		catch(IOException e)
		{
			System.out.println("failed to connect to server!");
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	private void startListeningThread()
	{
		System.out.println("starting listening thread");
		try
		{
			ClientListenerThread listenerThread = new ClientListenerThread(inputStream);
			listenerThread.start();
			System.out.println("success!");
		}
		catch (Exception e)
		{
			System.out.println("failed to start Listening thread!");
			e.printStackTrace();
			System.exit(0);
		}
	}
		
	public void fetchChatroomList()
	{
		System.out.println("requesting chatroom list...");
		Message chatroomRequest = new Message("", username, Message.CHATROOM_LIST_REQUEST);
		sendMessage(chatroomRequest);
		System.out.println("awaiting chatroom...");
		chatroomList = readChatRoomList();
		System.out.println(chatroomList.toString());
	}
	
	private ArrayList<ArrayList<String>> readChatRoomList()
	{
		try
		{
			return (ArrayList<ArrayList<String>>) inputStream.readObject();
		}
		catch (IOException e)
		{
			System.out.println("InputStream is closed");
			e.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Incompatible object cast");
			e.printStackTrace();
			return null;
		}
	}
		
	//@override
	public void disconnect()
	{
		//send a disconnect message
		//server disconnects this client
		//close all threads/clean up
		
	}
	
		
}
