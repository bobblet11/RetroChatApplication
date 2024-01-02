package serverside;
import java.awt.event.ActionEvent;
import java.io.*;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import misc.Chatroom;
import misc.Client;
import misc.Message;


public class ServerListenerThread extends Thread{
	
	private Client client;
	private final int MAX_LOGIN_ATTEMPTS = 3;
	private int loginAttempts = 0;
	private Message currentMsg;
	private boolean authorised = false;
	private Timer timer;
	
	public ServerListenerThread(Client client)
	{
		System.out.println("starting ServerListenerThread...\n- - - -");
		this.client = client;
		ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt){
		    	  System.out.println(Server.chatrooms.get(0).getParticipants());
		          if (!client.sendChatroomList())
		          {
		        	  System.out.println("Lost connection to client");
		        	  timer.stop();
		          }
		        
		      }
		  };
		  timer = new Timer(600, taskPerformer);
		  timer.start();
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
			
			if (currentMsg == null || !timer.isRunning())
			{
				timer.stop();
				System.out.println("closing thread");
				return;
			}
			
			switch ( currentMsg.getCommandType() )
			{
				case Message.NULL_COMMAND_TYPE: //standard message
				{
					System.out.println(client.getConnectedChatroomID());
					System.out.println(Server.chatrooms.get(client.getConnectedChatroomID()).getParticipants());
					for (Client c : Server.chatrooms.get(client.getConnectedChatroomID()).getParticipants())
					{
						c.sendMessage(currentMsg);
					}
					break;
				}
				case Message.JOIN_CHATROOM_REQUEST:
				{
					int chatIDtoJoin = Integer.parseInt(currentMsg.getMessageBody());
					System.out.println("client " + client.getUsername() + " has joined " + chatIDtoJoin);
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
							if (checkLogin())
							{
								client.sendMessage(new Message(Message.APPROVED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
								authorised = true;
							}
							else
							{
								client.sendMessage(new Message(Message.REJECTED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
								authorised = false;
							}
						}
					}
					break;
				}
				default:
					System.out.println("Defaulted");
					break;
			}
		}	
	}
	
	private boolean checkLogin()
	{
		String username = currentMsg.getMessageBody().substring(0,currentMsg.getMessageBody().indexOf('?'));
		String password = currentMsg.getMessageBody().substring(currentMsg.getMessageBody().indexOf('?')+1);
		
		//USE DATA BASE HERE, COMPARE LOGIN ATTEMPT WITH ACCOUNTS IN DATABASE
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(Server.accountsFile));
			String st = br.readLine();
			//assuming user names are in alphabetical order
			while ((st != null && (int)st.charAt(0) <= (int)username.charAt(0)))
			{
				System.out.println(st.substring(0,st.indexOf("|")));
				if (st.substring(0,st.indexOf("|")).equals(username) && st.substring(st.indexOf("|")+1).equals(password))
				{
					client.setUsername(username);
					return true;
				}
				st = br.readLine();
			}
			br.close();
			return false;
			
		}
		catch (FileNotFoundException e)
		{
			System.out.println("no accounts to search");
			return false;
		}
		catch (IOException e)
		{
			System.out.println("IOException in accounts file");
			return false;
		}
		
	}
	
}
