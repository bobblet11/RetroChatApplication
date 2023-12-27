package serverside;
import misc.Chatroom;
import misc.Client;
import misc.Message;

public class ServerListenerThread extends Thread{
	
	private Client client;
	private final int MAX_LOGIN_ATTEMPTS = 3;
	private int loginAttempts = 0;
	private Message currentMsg;
	private boolean authorised = false;
	
	public ServerListenerThread(Client client)
	{
		System.out.println("starting ServerListenerThread...\n- - - -");
		this.client = client;
	}
	
	public void run()
	{
		//client attempts to log-in, failure will result in the socket terminating		
		//send the client the list of chat-rooms
		
		//wait for client to choose a chat-room to join
		
		//relay messages
		while(true)
		{
			currentMsg = client.readMessage();
			
			switch ( currentMsg.getCommandType() )
			{
				case Message.NULL_COMMAND_TYPE: //standard message
				{
					for (Client c : Server.chatrooms.get(client.getConnectedChatroomID()).getParticipants())
					{
						if (!c.equals(client))
							c.sendMessage(currentMsg);
					}
					break;
				}
				case Message.CHATROOM_LIST_REQUEST:
				{
					client.sendChatroomList();
					break;
				}
				case Message.JOIN_CHATROOM_REQUEST:
				{
					int chatIDtoJoin = Integer.parseInt(currentMsg.getMessageBody());
					Server.chatrooms.get(chatIDtoJoin).addClient(client);
					client.setConnectedChatroomID(chatIDtoJoin);
					client.sendMessage(new Message(Message.APPROVED, Message.FROM_SERVER, Message.JOIN_CHATROOM_REQUEST));
					break;
				}
				case Message.EXIT_CHATROOM_REQUEST:
				{
					int chatIDtoJoin = Integer.parseInt(currentMsg.getMessageBody());
					Server.chatrooms.get(chatIDtoJoin).removeClient(client);
					client.setConnectedChatroomID(-1);
					client.sendMessage(new Message(Message.APPROVED, Message.FROM_SERVER, Message.EXIT_CHATROOM_REQUEST));
					break;
				}
				case Message.LOGIN_REQUEST:
				{
					if (!authorised)
					{
						if (loginAttempts >3)
						{
							client.disconnect();
							return;
						}
						else
						{
							loginAttempts++;
							authorised = checkLogin();
						}
					}
					break;
				}
			}
		}	
	}
	
	private boolean checkLogin()
	{
		String username = currentMsg.getMessageBody().substring(0,currentMsg.getMessageBody().indexOf('?'));
		String password = currentMsg.getMessageBody().substring(currentMsg.getMessageBody().indexOf('?')+1);
		
		//USE DATA BASE HERE, COMPARE LOGIN ATTEMPT WITH ACCOUNTS IN DATABASE
	
		if (username.equals("banned")) //unsuccessful login
		{
			client.sendMessage(new Message(Message.REJECTED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
		}
		else//successful login
		{
			client.sendMessage(new Message(Message.APPROVED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
			client.setUsername(username);
			return true;
		}
		return false;
	}
	
}
