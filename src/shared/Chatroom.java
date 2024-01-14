package shared;
import java.util.*;
import java.io.*;

public class Chatroom implements Serializable{
	
	private ArrayList<Client> participants = new ArrayList<Client>();
	private int chatroomID;
	private String chatroomName;
	
	public Chatroom(){}
	
	public Chatroom(int index, String name) {
		chatroomID = index;
		chatroomName = name;
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
	
	public String getChatroomName(){
		return chatroomName;
	}
	
	public void setChatroomName(String name){
		chatroomName = name;
	}
	
}
