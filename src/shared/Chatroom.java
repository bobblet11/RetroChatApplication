package shared;
import java.util.*;
import java.io.*;

public class Chatroom implements Serializable{
	
	private ArrayList<Client> participants = new ArrayList<Client>();
	private int chatroomID;
	
	public Chatroom(){}
	
	public Chatroom(int index) {
		chatroomID = index;
	}
	
	public void addClient(Client client) {
		participants.add(client);
	}
	
	public void removeClient(Client client) {
		if (participants.contains(client)) {
			participants.remove(client);
		}
	}
	
	public ArrayList<Client> getParticipants() {
		return participants;
	}
	
	public int getChatroomID(){
		return chatroomID;
	}
	
}
