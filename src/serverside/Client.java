package serverside;
import java.io.*;
import java.net.*;
import misc.Message;

public class Client {
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket sock;
	
	private String username;
	
	public Client(Socket sock) throws IOException
	{
		this.sock = sock;
		this.inputStream = new ObjectInputStream(sock.getInputStream());
		this.outputStream = new ObjectOutputStream(sock.getOutputStream());

	}
	
	public Message readMessage()
	{
		try
		{
			return (Message) inputStream.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendMessage(Message message) throws IOException
	{
		outputStream.writeObject(message);
	}
	
	public void sendChatroomList() throws IOException
	{
		outputStream.writeObject(Server.chatrooms);
	}
	
	public void disconnect()
	{
		//send disconnect message
		//replace clients with chatrooms arraylist
		Server.clients.remove(this);
		try
		{
			sock.close();
			outputStream.close();
			inputStream.close();
		}
		catch (IOException e)
		{
			
		}
		finally
		{
			System.out.println("socket " + sock + "has been closed");
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
		return sock;
	}
}
