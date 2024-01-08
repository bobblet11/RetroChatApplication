package client;
import java.net.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import shared.*;

public class ClientNetworkManager extends Client{
	
	private final String IP = "127.0.0.1";
	private final int PORT = 6000;	
	static ArrayList<Chatroom> chatroomList = new ArrayList<Chatroom>(0);
	
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
			ClientGUI.serverUnavailable();
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

	
	public void read()
	{
		try
		{
			Object incoming = (Object) inputStream.readUnshared();
			if (incoming instanceof Message)
			{
				Message incomingMessage = (Message) incoming;
				if (incomingMessage.getType() == Message.STANDARD)
				{
					System.out.println(incomingMessage.getSender());
					if (incomingMessage.getSender().equals(Message.FROM_SERVER))
					{
						ClientGUI.updateTextArea(incomingMessage, Color.LIGHT_GRAY);
					}
					else
					{
						ClientGUI.updateTextArea(incomingMessage, Color.BLACK);	
					}
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
				chatroomList =  (ArrayList<Chatroom>) incoming;
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
