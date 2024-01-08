package shared;
import java.io.*;

public class Message implements Serializable {	

	private static final long serialVersionUID = 1L;

	public enum type
	{
		STANDARD,
		COMMAND,
	}
	
	public enum command
	{
		CHATROOM_LIST_REQUEST,
		JOIN_CHATROOM_REQUEST,
		EXIT_CHATROOM_REQUEST,
		LOGIN_REQUEST,
		NULL
	}
	
	public enum states
	{
		APPROVED,
		REJECTED
	}
	
	private final int YEAR = 0;
	private final int MONTH = 1;
	private final int DAY = 2;
	private final int HOUR = 3;
	private final int MINUTE = 4;
	private final int SECOND = 5;
	
	command commandType;
	type messageType;
	
	public final static String SERVER = "SERVER";
	private int[] timeStamp = {-1,-1,-1,-1,-1,-1};
	
	private String sender;
	private String message;
	
	public Message(String message, String sender, command commandType)
	{
		createTimestamp();
		this.messageType = type.COMMAND;
		this.sender = sender;
		this.message = message;
		this.commandType = commandType;
	}
	
	//standard message
	public Message(String message, String sender)
	{
		createTimestamp();
		this.messageType = type.STANDARD;
		this.sender = sender;
		this.message = message;
		this.commandType = command.NULL;
	}
		
	private void createTimestamp()
	{
		java.time.LocalDateTime time = java.time.LocalDateTime.now();
		timeStamp[0] = time.getYear();
		timeStamp[1] = time.getMonthValue();
		timeStamp[2] = time.getDayOfMonth();
		timeStamp[3] = time.getHour();
		timeStamp[4] = time.getMinute();
		timeStamp[5] = time.getSecond();
	}
	
	public String getFormatedTimestamp()
	{
		return String.format("[%2d-%2d-%2d|%2d:%2d:%2d]", 
		timeStamp[DAY], timeStamp[MONTH], timeStamp[YEAR], 
		timeStamp[HOUR], timeStamp[MINUTE], timeStamp[SECOND]).replace(' ', '0');
	}
	
	public String getSimpleFormatedTimestamp()
	{
		return String.format("[%2d:%2d]", 
		timeStamp[HOUR], timeStamp[MINUTE]).replace(' ', '0');
	}
	
	public int getHour()
	{
		return timeStamp[HOUR];
	}
	
	public int getMinute()
	{
		return timeStamp[MINUTE];
	}
	
	public int getSecond()
	{
		return timeStamp[SECOND];
	}
	
	public command getCommandType()
	{
		return commandType;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public String getMessageBody()
	{
		return message;
	}
	
	public type getType()
	{
		return messageType;
	}
		
	public boolean logInIsApproved()
	{
		if (message.equals(states.APPROVED.toString()) && sender.equals(SERVER) && commandType == command.LOGIN_REQUEST)
		{
			return true;
		}
		return false;
	}
	
	public boolean joinChatroomIsApproved()
	{
		if (message.equals(states.APPROVED.toString()) && sender.equals(SERVER) && commandType == command.JOIN_CHATROOM_REQUEST)
		{
			return true;
		}
		return false;
	}
	
	public boolean exitChatroomIsApproved()
	{
		if (message.equals(states.APPROVED.toString()) && sender.equals(SERVER) && commandType == command.EXIT_CHATROOM_REQUEST)
		{
			return true;
		}
		return false;
	}
	
}
