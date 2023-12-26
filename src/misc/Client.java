package misc;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import serverside.Server;

public class Client{
	
	public static char DELIMETER = '?';
	protected ObjectOutputStream outputStream;
	protected ObjectInputStream inputStream;
	protected Socket socket;
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
	
	public void sendMessage(Message message)
	{
		try
		{
			outputStream.writeObject(message);
		}
		catch (IOException e)
		{
			System.out.println("OutputStream is closed");
			e.printStackTrace();
		}
	}
	
	
	public void sendChatroomList()
	{
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		for (Chatroom room : Server.chatrooms)
		{
			out.add(room.getChatroomProperties());
		}
		
		try
		{
			outputStream.writeObject(out);
		}
		catch (IOException e)
		{
			System.out.println("OutputStream is closed");
			e.printStackTrace();
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
	
	public String getUsername()
	{
		return username;
	}
	
	public Socket getSocket()
	{
		return socket;
	}
}
