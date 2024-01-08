package client;
import java.io.*;

public class ClientListenerThread extends Thread{
	private ClientNetworkManager nm;

	public ClientListenerThread(ObjectInputStream is, ClientNetworkManager nm) {
		System.out.println("starting ClientListenerThread...");
		this.nm = nm;
	}
	
	public void run() {
		while(true) {	
			try{
				nm.read();
			}
			catch (IOException e) {
				System.out.println("InputStream is closed");
				e.printStackTrace();
				System.out.println("Terminating ClientListenerThread");
				return;
			}
			catch (ClassNotFoundException e) {
				System.out.println("Incompatible object cast");
				e.printStackTrace();
				System.out.println("Terminating ClientListenerThread");
				return;
			}
		}
	}
}
