package clientside;
import java.io.*;
import misc.Message;

public class ClientListenerThread extends Thread{
	
	private ObjectInputStream is;

	public ClientListenerThread(ObjectInputStream is)
	{
		System.out.println("starting ClientListenerThread...");
		this.is = is;
	}
	
	public void run()
	{
		Message currentMsg;
		while(true)
		{
			try
			{
				currentMsg = (Message)is.readObject();
				currentMsg.printMessageStatus();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				currentMsg = null;
			}
		}	
	}
}
