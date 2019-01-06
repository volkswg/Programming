package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class ClientReader extends Thread{
	Thread t;
	//io staff
	private InputStream inFromServer;
	private DataInputStream in;
	
	private volatile boolean threadAlive;

	// Socket Staff
	private ClientConnect mainConn;
	
	public ClientReader(Socket mySocket, ClientConnect mainConn) {
		threadAlive =true;
		this.mainConn = mainConn;
		try {
			inFromServer = mySocket.getInputStream();
			in = new DataInputStream(inFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		// Doing my staff
		while(this.threadAlive) {
			try {
				String msgIn = in.readUTF();
				String[] processMsg = msgIn.split(":");
				switch (processMsg[0]) {
					case "%$GenBall":
						mainConn.generateBall(processMsg[1]);
						break;
				}
//				System.out.println("Recv: "+msgIn);
			} catch (IOException e) {
//				e.printStackTrace();
				shutdown();
				System.out.println(e);
				System.out.println("[System] Disconnected");
			}
		}
	}
	
	public void start(){
		if(t== null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void shutdown() {
		this.threadAlive = false;
		// Close server
		try {
			this.inFromServer.close();
			this.in.close();
		} catch (Exception e) {
			// Server Socket is null
			System.out.println(e);
		}
	}
}
