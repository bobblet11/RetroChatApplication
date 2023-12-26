package serverside;
import misc.Chatroom;
import misc.Message;

public class ServerListenerThread extends Thread{
	
	private Client client;
	private Chatroom chatroom;
	private boolean authorised  = false;
	private final int MAX_LOGIN_ATTEMPTS = 3;
	private int loginAttempts = MAX_LOGIN_ATTEMPTS;
	
	
	public ServerListenerThread(Client client)
	{
		System.out.println("starting ServerListenerThread...\n- - - -");
		this.client = client;
	}
	
	public void run()
	{
		//login
		if (!logIn())
		{
			client.disconnect();
			return;
		}
		
		//serverList request
		sendCurrentChatroomList();
		
		//chatroom join
		
		//relay messages
		Message currentMsg;
		while(true)
		{
			try
			{	
				currentMsg = client.readMessage();
				currentMsg.printMessageStatus();
								
				//replace clients with the particpants in chatroom
				for (Client c : Server.clients)
				{
					if (!c.equals(client))
					{
						c.sendMessage(currentMsg);
					}
				}
			}
			catch (Exception e)
			{
				currentMsg = null;
			}
		}	
	}
	
	private boolean logIn()
	{
		Message currentMsg;
		for (int i = MAX_LOGIN_ATTEMPTS; i >1;i--)
		{
			try
			{
				currentMsg = client.readMessage();
				String loginDetails = currentMsg.getMessageBody();
				String username = loginDetails.substring(0,loginDetails.indexOf('?'));
				String password = loginDetails.substring(loginDetails.indexOf('?')+1);
				//check txt file or something
				//add database here!
				if (username.equals("banned"))
				{
					client.sendMessage(new Message(Message.REJECTED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
					continue;
				}
				client.sendMessage(new Message(Message.APPROVED, Message.FROM_SERVER, Message.LOGIN_REQUEST));
				client.setUsername(username);
				authorised = true;
				return true;
			}
			catch (Exception e)
			{
				System.out.println("error in login attempt " + i);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	private void sendCurrentChatroomList()
	{
		//replace this with a txt file that is updated everytime a chatroom is removed/added, use the id to find the correct line
		System.out.println("sending chatroom list to  " + client.getUsername() + "...");
		String allChatrooms = "";
		for (Chatroom room: Server.chatrooms)
		{
			allChatrooms = allChatrooms + room.getChatroomProperties() + "\n";
		}
		System.out.println(allChatrooms);
		
		try
		{
			client.sendChatroomList();
		}
		catch(Exception e)
		{
			System.out.println("failed to send chatroom list to " + client.getUsername());
			e.printStackTrace();
		}
		
	}
}
