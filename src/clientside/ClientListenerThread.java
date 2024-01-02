package clientside;
import java.io.*;

import javax.swing.SwingUtilities;

import misc.Message;

public class ClientListenerThread extends Thread{
	
	private ObjectInputStream is;
	private ClientNetworkManager nm;

	public ClientListenerThread(ObjectInputStream is, ClientNetworkManager nm)
	{
		System.out.println("starting ClientListenerThread...");
		this.is = is;
		this.nm = nm;
	}
	
	public void run()
	{

		while(true)
		{
			nm.read();
		}
	}
}
