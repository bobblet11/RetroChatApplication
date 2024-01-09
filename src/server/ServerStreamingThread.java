package server;
import shared.Client;
import java.util.ArrayList;

public class ServerStreamingThread extends Thread{
	
	static ArrayList<Client> allClients = new ArrayList<Client>();
	
	public ServerStreamingThread(){
		this.setName("SERVER_STREAMING_THREAD");
		System.out.println("starting " + this.getName()  + "\n- - - - - - -");
	}
	
	public void run(){
		try{	
			while (true){
				streamChatroomToClients();
				Thread.sleep(600);
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
			System.out.println("Streaming thread terminated");
		}
	
	}
	
	
	public static void addClient(Client client)
	{
		allClients.add(client);
	}
	
	
	private void streamChatroomToClients() throws InterruptedException
	{
		for (int i = 0; i<allClients.size(); i++) {
			if (!allClients.get(i).sendData(Server.chatrooms)) {
	        	  System.out.println("Lost connection to client");
	        	  allClients.remove(i);
	        }
		}
		
	
	}
	
	
}
