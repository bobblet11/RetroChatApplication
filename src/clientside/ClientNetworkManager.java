package clientside;
import java.net.*;
import java.io.*;
import java.util.*;
import misc.Message;
import misc.Chatroom;

public class ClientNetworkManager {
	
	private final String IP = "127.0.0.1";
	private final int PORT = 6000;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket = null;
	private BufferedReader reader;
	private String username;
	private String password;
	
	static ArrayList<Chatroom> chatroomList;
	static Chatroom chatroom;
	//start a thread that periodically asks for up to date chatroom list
	
	public ClientNetworkManager()
	{
	}
	
	public boolean LogIn(String username, String password) throws IOException
	{
		Message logInRequest = new Message(username+"?"+password, username, Message.LOGIN_REQUEST);
		sendMessage(logInRequest);
		Message response = readMessage();
		if (response.getSender().equals(Message.FROM_SERVER) 
				&& response.getCommandType() == Message.LOGIN_REQUEST 
				&& response.logInIsApproved())
		{
			this.username = username;
			this.password = password;
			return true;
		}
		return false;
	}
	
	private void readInput()
	{
		System.out.println("reading console input...");
		String message = "";
		while (!message.equals("EXIT"))
		{
			try
			{
				message = reader.readLine();
				Message msg = new Message(message,"Client");
				sendMessage(msg);
			}
			catch(IOException e)
			{
				System.out.println("failed to read console input!");
				e.printStackTrace();
			}
		}
	}
	
	
	public void connect()
	{
		System.out.println("connecting to,\nIP: " + IP + "\n PORT: " + PORT);
		try
		{
			socket = new Socket(IP, PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("success!");
		}
		catch(Exception e)
		{
			System.out.println("failed to connect to server!");
			e.printStackTrace();
			System.exit(0);
		}
		finally
		{
			System.out.println("connect method complete\n- - - - -");
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
		finally
		{
			System.out.println("startListeningThread method complete\n- - - - -");
		}
	}
	
	public void sendMessage(Message message) throws IOException
	{
		outputStream.writeObject(message);
	}
	

	public Message readMessage()
	{
		System.out.println("awaiting message...");
		try
		{
			return (Message) inputStream.readObject();
		}
		catch (Exception e)
		{
			System.out.println("failed to read message!");
			e.printStackTrace();
			return null;
		}
		finally
		{
			System.out.println("readMessage method complete\n- - - - -");
		}
	}
	
	
	public void requestChatroomList()
	{
		//System.out.println("requesting chatroom list...");
		//Message chatroomRequest = new Message("", username, Message.LOGIN_REQUEST);
		System.out.println("awaiting chatroom...");
		try
		{
			chatroomList = (ArrayList<Chatroom>) inputStream.readObject();
			String allChatrooms = "";
			for (Chatroom room: chatroomList)
			{
				allChatrooms = allChatrooms + room.getChatroomProperties() + "\n";
			}
			System.out.println(allChatrooms);
		}
		catch (Exception e)
		{
			System.out.println("failed to read chatroom!");
			e.printStackTrace();
		}
		finally
		{
			System.out.println("readChatroomRequest method complete\n- - - - -");
		}
	}
		
}
