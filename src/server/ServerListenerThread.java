package server;
import java.io.*;
import shared.*;
import shared.Message.command;
import shared.Message.type;
import shared.Message.states;


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
		while(true) {
			currentMsg = thisClient.readMessage();

			if (currentMsg == null){
				System.out.println("terminating " + this.getName()  + "\n- - - - - - -");
				ServerStreamingThread.allClients.remove(thisClient);
				
				if (thisClient.getConnectedChatroomID() != -1)
				{
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);;
				}
				
				thisClient.disconnect();
				return;
			}
			
			if (!Server.serverStreamingThread.isAlive())
			{
				System.out.println("terminating " + this.getName()  + "\n- - - - - - -");
				if (thisClient.getConnectedChatroomID() != -1)
				{
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);;
				}
				thisClient.disconnect();
				return;
			}
			
			switch ( currentMsg.getCommandType()) {
		
				case NULL: {
					sendToEveryone(currentMsg);
					break;
				}
			
				
				case JOIN_CHATROOM_REQUEST: {
					thisClient.setConnectedChatroomID(Integer.parseInt(currentMsg.getMessageBody()));
					System.out.println("[" + this.getName() + "]\t" 
									   +thisClient.getUsername() 
									   + " has joined " + thisClient.getConnectedChatroomID());
					
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).addClient(thisClient);
						
					Message succesfulJoinReply = new Message(states.APPROVED.toString(), Message.SERVER, command.JOIN_CHATROOM_REQUEST);
					thisClient.sendData(succesfulJoinReply);
					
					Message serverAnnouncement = new Message(thisClient.getUsername() + " has joined.", Message.SERVER);
					sendToEveryone(serverAnnouncement);
					break;
				}
				
				
				case EXIT_CHATROOM_REQUEST: {
					System.out.println("[" + this.getName() + "]\t" 
							   +thisClient.getUsername() 
							   + " has left " + thisClient.getConnectedChatroomID());
					
					Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);
					
					Message succesfulExitReply = new Message(states.APPROVED.toString(), Message.SERVER, command.EXIT_CHATROOM_REQUEST);
					thisClient.sendData(succesfulExitReply);
					
					Message serverAnnouncment = new Message(thisClient.getUsername() + " has left.", Message.SERVER);
					sendToEveryone(serverAnnouncment);
					
					thisClient.setConnectedChatroomID(-1);
					break;
				}
				
				
				case LOGIN_REQUEST: {
					if (loginAttempts >MAX_LOGIN_ATTEMPTS){
						Server.chatrooms.get(thisClient.getConnectedChatroomID()).removeClient(thisClient);;
						thisClient.disconnect();
						return;
					}
					
					if (authorised){	break;    }
					
					Message reply = null;
					if (lookUpAccounts()){
						reply = new Message(states.APPROVED.toString(), Message.SERVER, command.LOGIN_REQUEST);
						thisClient.sendData(reply);
						authorised = true;
					}
					else{
						reply = new Message(states.REJECTED.toString(), Message.SERVER, command.LOGIN_REQUEST);
						thisClient.sendData(reply);
						authorised = false;
					}
					
					loginAttempts++;
					break;
				}
				
				default: break;
				
				
			}
		}	
	}
	
	
	private void sendToEveryone(Message message) {
		for (Client c : Server.chatrooms.get(thisClient.getConnectedChatroomID()).getParticipants()) {
			c.sendData(message);
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