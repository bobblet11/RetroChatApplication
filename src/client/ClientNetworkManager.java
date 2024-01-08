package client;
import java.net.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import shared.*;
import shared.Message.command;
import shared.Message.type;

public class ClientNetworkManager extends Client{
	
	private static final long serialVersionUID = 1L;
	
	private final String IP = "127.0.0.1";
	private final int PORT = 6000;	
	static ArrayList<Chatroom> chatroomList = new ArrayList<Chatroom>(0);
	
	public ClientNetworkManager() {
		connect();
		startListeningThread();
	}
	
	private void connect() {
		System.out.println("connecting to,\nIP: " + IP + "\nPORT: " + PORT);
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		}
		catch(IOException e) {
			ClientGUI.serverUnavailable();
			System.out.println("failed to connect to server!");
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	private void startListeningThread() {
		System.out.println("starting listening thread");
		try {
			ClientListenerThread listenerThread = new ClientListenerThread(inputStream, this);
			listenerThread.start();
		}
		catch (Exception e) {
			System.out.println("failed to start listening thread!");
			e.printStackTrace();
			System.exit(0);
		}
	}

	void read() throws IOException, ClassNotFoundException{
		
		Object incoming = (Object) inputStream.readObject();
		
		if (incoming instanceof Message) {
			Message incomingMessage = (Message) incoming;
			
			if (incomingMessage.getType() == type.STANDARD) {
				Color colourOfText = incomingMessage.getSender().equals(Message.SERVER) ? Color.LIGHT_GRAY : Color.BLACK;
				ClientGUI.updateTextArea(incomingMessage, colourOfText);	
				return;
			}
			
			if (incomingMessage.getCommandType() == command.LOGIN_REQUEST) {
				if (incomingMessage.logInIsApproved()) {
					ClientGUI.successfulLogin();
				}
				else {
					ClientGUI.unsuccessfulLogin();
					setUsername("");
				}
				return;
			}
			
			if (incomingMessage.getCommandType() == command.JOIN_CHATROOM_REQUEST)
			{
				if (incomingMessage.joinChatroomIsApproved()){
					ClientGUI.joinChatroom();
				}
				else {
					setConnectedChatroomID(-1);
				}
				return;
			}
			
			if (incomingMessage.getCommandType() == command.EXIT_CHATROOM_REQUEST && incomingMessage.exitChatroomIsApproved())
			{
				ClientGUI.exitChatroom();
				setConnectedChatroomID(-1);
				return;
			}
		}
		else {
			chatroomList =  (ArrayList<Chatroom>) incoming;
			ClientGUI.updateServerList();
		}
	}
	
	public boolean attemptLogIn(String username, String password) throws IOException {
		Message logInRequest = new Message(username+"?"+password, username, command.LOGIN_REQUEST);
		sendData(logInRequest);
		
		Message response = readMessage();
		if (response.logInIsApproved()) {
			setUsername(username);
			return true;
		}
		return false;
	}
		
}
