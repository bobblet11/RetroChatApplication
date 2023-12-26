package misc;
import java.io.*;

public class Message implements Serializable {	

	private static final long serialVersionUID = 1L;

	private final static int STANDARD = 0;
	private final static int SERVER_COMMAND = 1;
	private final static int NULL_COMMAND_TYPE = -1;
	
	//types of commands
	public final static int CHATROOM_LIST_REQUEST = 0;
	public final static int JOIN_CHATROOM_REQUEST = 1;
	public final static int EXIT_CHATROOM_REQUEST = 2;
	public final static int LOGIN_REQUEST = 3;
	
	public final static String FROM_SERVER = "SERVER";
	
	public final static String APPROVED ="APPROVE";
	public final static String REJECTED = "REJECT";
	
	private final int YEAR = 0;
	private final int MONTH = 1;
	private final int DAY = 2;
	private final int HOUR = 3;
	private final int MINUTE = 4;
	private final int SECOND = 5;
	
	private int commandType;
	private int messageType;
	private int[] timeStamp = {-1,-1,-1,-1,-1,-1};
	private String sender;
	private String message;
	
	//for server commands, prefixed with /
	public Message(String message, String sender, int commandType)
	{
		createTimestamp();
		this.messageType = SERVER_COMMAND;
		this.sender = sender;
		this.message = message;
		this.commandType = commandType;
	}
	
	//standard message
	public Message(String message, String sender)
	{
		createTimestamp();
		this.messageType = STANDARD;
		this.sender = sender;
		this.message = message;
		this.commandType = NULL_COMMAND_TYPE;
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
	
	public int getCommandType()
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
	
	public int getType()
	{
		return messageType;
	}
	
	private String formatTime()
	{
		return String.format("%d - %d - %d###%d:%d:%d", 
		timeStamp[YEAR], timeStamp[MONTH], timeStamp[DAY], 
		timeStamp[HOUR], timeStamp[MINUTE], timeStamp[SECOND]);
	}
	
	public void printMessageStatus()
	{
		String output = String.format("%s sent a %s containing %s at %s", 
		sender, messageType == STANDARD ? "MESSAGE" : "COMMAND", message, formatTime());
		
		System.out.println(output);
	}
	
	public boolean logInIsApproved()
	{
		if (message.equals(APPROVED))
		{
			return true;
		}
		return false;
	}
	
	
}
