package misc;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import serverside.Server;

public class Client implements Serializable{
	
	public static char DELIMETER = '?';
	transient protected ObjectOutputStream outputStream;
	transient protected ObjectInputStream inputStream;
	transient protected Socket socket;
	protected String username;
	protected String password;
	protected int connectedChatroomID;
	
	public Client(Socket sock) throws IOException
	{
		this.socket = sock;
		this.inputStream = new ObjectInputStream(sock.getInputStream());
		this.outputStream = new ObjectOutputStream(sock.getOutputStream());
	}
	
	public Client()
	{
		System.out.println("FUCK");
	}
	
	public Message readMessage()
	{
		try
		{
			return (Message) inputStream.readObject();
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
	
	public synchronized boolean send(boolean isMessage, Object data)
	{
		try
		{
			outputStream.reset();
			
			if (isMessage)
			{
				sendMessage((Message) data);
				return true;
			}
			else
			{
				return sendChatroomList((ArrayList<Chatroom>) data);
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	
	public void sendMessage(Message message)
	{
		try
		{
			outputStream.writeUnshared(message);
			
		}
		catch (IOException e)
		{
			System.out.println("OutputStream is closed");
			e.printStackTrace();
		}
	}
	
	
	public boolean sendChatroomList(ArrayList<Chatroom> chatrooms)
	{
		try
		{
			outputStream.writeUnshared(chatrooms);
			return true;
		}
		catch (IOException e)
		{
			System.out.println("OutputStream is closed");
			e.printStackTrace();
			return false;
		}
	}
	
	public void disconnect()
	{
		//send disconnect message
		//replace clients with chatrooms.get(chatroomID);
		Server.clients.remove(this);
		try
		{
			socket.close();
			outputStream.close();
			inputStream.close();
		}
		catch (IOException e){}
		finally
		{
			System.out.println("socket " + socket + "has been closed");
		}
	}
	
	public void setUsername(String name)
	{
		username = name;
	}
	
	public void setConnectedChatroomID(int id)
	{
		connectedChatroomID = id;
	}
	
	public int getConnectedChatroomID()
	{
		return connectedChatroomID;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public Socket getSocket()
	{
		return socket;
	}
}
