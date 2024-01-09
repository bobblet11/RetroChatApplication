package shared;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client implements Serializable{
	private static final long serialVersionUID = 1L;
	
	transient protected ObjectOutputStream outputStream;
	transient protected ObjectInputStream inputStream;
	transient protected Socket socket;
	
	private String username;
	private int connectedChatroomID;
	
	public Client(Socket sock) throws IOException {
		this.socket = sock;
		this.inputStream = new ObjectInputStream(sock.getInputStream());
		this.outputStream = new ObjectOutputStream(sock.getOutputStream());
	}
	
	public Client(){}
	
	public Message readMessage() {
		try {
			return (Message) inputStream.readObject();
		}
		catch (IOException e) {
			System.out.println("InputStream is closed");
			e.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException e) {
			System.out.println("Incompatible object cast");	
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean sendData(Object data) {
		try {
			outputStream.reset();
			
			if (data instanceof Message) {
				outputStream.writeObject((Message) data);
				return true;
			}
			
			outputStream.writeObject((ArrayList<Chatroom>) data);
			return true;
		}
		catch (IOException e) {
			System.out.println("failed to send data");
			return false;
		}
	}
		
	public void disconnect() {
		
		try {
			socket.close();
			outputStream.close();
			inputStream.close();
		}
		catch (IOException e){}
		finally {
			System.out.println("socket " + socket + "has been closed");
		}
	}
	
	public void setUsername(String name) {
		username = name;
	}
	
	public void setConnectedChatroomID(int id) {
		connectedChatroomID = id;
	}
	
	public int getConnectedChatroomID() {
		return connectedChatroomID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
