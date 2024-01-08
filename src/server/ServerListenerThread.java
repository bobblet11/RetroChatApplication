package server;
import java.io.*;
import shared.*;


public class ServerListenerThread extends Thread{
	
	private final int MAX_LOGIN_ATTEMPTS = 3;
	
	private Client thisClient;
	private int loginAttempts = 0;
	private Message currentMsg;
	private boolean authorised = false;

	public ServerListenerThread(Client client) {
		this.setName("LISTENING_THREAD_" + Server.listenerThreadCount);
		System.out.println("starting " + this.getName()  + "\n- - - - - - -");
		this.thisClient = client;
	}
	
	public void run()
	{				
		while(true){
			currentMsg = thisClient.readMessage();

			if (currentMsg == null || !Server.serverStreamingThread.isAlive()){
				System.out.println("terminating " + this.getName()  + "\n- - - - - - -");
				return;
			}
			
			switch ( currentMsg.getCommandType()) {
			
			
				case Message.NULL_COMMAND_TYPE: {
					sendToEveryone(currentMsg);
					break;
				}
			
				
				case Message.JOIN_CHATROOM_REQUEST: {
					thisClient.setConnectedChatroomID(Integer.parseInt(currentMsg.getMessageBody()));
					System.out.println("[" + this.getName() + "]\t" 
									   +thisClient.getUsername() 
									   + " has joined " + thisClient.getConnectedChatroomID());
					
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).addClient(thisClient);
						
					thisClient.send(true, new Message(Message.APPROVED, Message.FROM_SERVER, Message.JOIN_CHATROOM_REQUEST));
					sendToEveryone(new Message(thisClient.getUsername() + " has joined.", Message.FROM_SERVER));
					break;
				}
				
				
				case Message.EXIT_CHATROOM_REQUEST: {
					System.out.println("[" + this.getName() + "]\t" 
							   +thisClient.getUsername() 
							   + " has left " + thisClient.getConnectedChatroomID());
					
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);
					
					thisClient.send(true, new Message(Message.APPROVED, Message.FROM_SERVER, Message.EXIT_CHATROOM_REQUEST));
					sendToEveryone(new Message(thisClient.getUsername() + " has left.", Message.FROM_SERVER));
					
					thisClient.setConnectedChatroomID(-1);
					break;
				}
				
				
				case Message.LOGIN_REQUEST:{
					if (loginAttempts >MAX_LOGIN_ATTEMPTS){
						Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);;
						thisClient.disconnect();
						return;
					}
					
					if (authorised){
						break;
					}
					
					if (lookUpAccounts()){
						thisClient.send(true, new Message(Message.APPROVED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
						authorised = true;
					}
					else{
						thisClient.send(true, new Message(Message.REJECTED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
						authorised = false;
					}
					
					loginAttempts++;
					break;
				}
				
				
			}
		}	
	}
	
	
	private void sendToEveryone(Message message) {
		for (Client c : Server.chatrooms.get(thisClient.getConnectedChatroomID()).getParticipants()) {
			c.send(true, message);
		}
	}
	
	
	private boolean lookUpAccounts() {
		String username = currentMsg.getMessageBody().substring(0,currentMsg.getMessageBody().indexOf('?'));
		String password = currentMsg.getMessageBody().substring(currentMsg.getMessageBody().indexOf('?')+1);
		String usernameInDB, passwordInDB;
		try {
			BufferedReader br = new BufferedReader(new FileReader(Server.accountsFile));
			String st = br.readLine();

			while ((st != null && (int)st.charAt(0) <= (int)username.charAt(0))) {
				usernameInDB = st.substring(0,st.indexOf("|"));
				passwordInDB = st.substring(st.indexOf("|")+1);
				if (usernameInDB.equals(username) && passwordInDB.equals(password)) {
					thisClient.setUsername(username);
					br.close();
					return true;
				}
				st = br.readLine();
			}
			br.close();
			return false;
			
		}
		catch (FileNotFoundException e) {
			System.out.println("no accounts to search");
			return false;
		}
		catch (IOException e) {
			System.out.println("IOException in accounts file");
			return false;
		}
	}
}