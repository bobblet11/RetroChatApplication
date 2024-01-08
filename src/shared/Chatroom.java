package shared;
import java.util.*;
import java.io.*;

public class Chatroom implements Serializable{
	
	private ArrayList<Client> participants;
	private int chatroomID;
	
	public Chatroom()
	{
		
	}
	
	public Chatroom(int index)
	{
		chatroomID = index;
		participants = new ArrayList<Client>();
	}
	
	public void addClient(Client client)
	{
		participants.add(client);
	}
	
	public void removeClient(Client client)
	{
		if (participants.contains(client))
		{
			participants.remove(client);
		}
	}
	
	public ArrayList<Client> getParticipants()
	{
		return participants;
	}
	
	public ArrayList<String> getChatroomProperties()
	{
		ArrayList<String> out = new ArrayList<String>();
		out.add(Integer.toString(chatroomID));
		int count=0;
		for (Client client : participants)
		{
			out.add(client.getUsername());
			count++;
		}
		out.add(Integer.toString(count));
		return out;
		
	}
	
	public int getChatroomID()
	{
		return chatroomID;
	}
	
	public void printParticipants()
	{
		for (Client c: participants)
		{
			System.out.println(c.getUsername() + "\t" + c.getSocket());
		}
	}
	
//	static ArrayList<String> formatChatroomList(ArrayList<ArrayList<String>> chatroomList)
//	{
//		String chatroom = "";
//		ArrayList<String> out = new ArrayList<String>();
//		for (ArrayList<String> room : chatroomList)
//		{
//			chatroom = room.get(0) + "?" + room.get(room.size()-1);
//			out.add(chatroom);
//		}
//		return out;
//	}
	
}
