package misc;
import java.util.*;

import serverside.Client;

import java.io.*;

public class Chatroom implements Serializable{
	private ArrayList<Client> participants;
	private int chatroomID;
	
	public Chatroom(int index)
	{
		chatroomID = index;
		participants = new ArrayList<Client>();
	}
	
	public void addClient(Client client)
	{
		if (!participants.contains(client))
		{
			participants.add(client);
		}
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
	
	public String getChatroomProperties()
	{
		return participants.toString() + "?" + chatroomID;
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
	
}
