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
		startListeningThread();
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
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);
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
			ClientListenerThread listenerThread = new ClientListenerThread(inputStream, this);
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
	
	public void read()
	{
		try
		{
			Object incoming = (Object) inputStream.readObject();
			if (incoming instanceof Message)
			{
				Message incomingMessage = (Message) incoming;
				if (incomingMessage.getType() == Message.STANDARD)
				{
					ClientGUI.updateTextArea(incomingMessage);
				}
				else if (incomingMessage.getCommandType() == Message.LOGIN_REQUEST)
				{
					if (incomingMessage.logInIsApproved())
					{
						ClientGUI.successfulLogin();
					}
					else
					{
						ClientGUI.unsuccessfulLogin();
						username = "";
					}
				}
				else if (incomingMessage.getCommandType() == Message.JOIN_CHATROOM_REQUEST)
				{
					if (incomingMessage.joinChatroomIsApproved())
					{
						ClientGUI.joinChatroom();
					}
					else
					{
						setConnectedChatroomID(-1);
					}
				}
				else if (incomingMessage.getCommandType() == Message.EXIT_CHATROOM_REQUEST)
				{
					if (incomingMessage.exitChatroomIsApproved())
					{
						ClientGUI.exitChatroom();
						setConnectedChatroomID(-1);
					}
				}
			}
			else
			{
				chatroomList =  (ArrayList<ArrayList<String>>) incoming;
				ClientGUI.updateServerList();
				
			}
		}
		catch (IOException e)
		{
			System.out.println("InputStream is closed");
			e.printStackTrace();
			System.exit(1);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Incompatible object cast");
			e.printStackTrace();
			System.exit(1);
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
