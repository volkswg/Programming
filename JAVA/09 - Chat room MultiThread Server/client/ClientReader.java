package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientReader extends Thread {
	private Thread t;
	private InputStream inFromServer;
	private DataInputStream in;
	
	private volatile boolean running;
	
	private ClientConnect mainConn;

	public ClientReader(Socket mySocket, ClientConnect mainConn) {
		try {
			inFromServer = mySocket.getInputStream();
			in = new DataInputStream(inFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = true;
		this.mainConn = mainConn;
	}

	public void run() {
		while (running) {
			try {
				System.out.println("Wait for Msg");
				String msgIn = in.readUTF();
				mainConn.appendLog(msgIn);
			} catch (IOException e) {
				mainConn.shutdown();
				mainConn.appendLog("[System] Server was Shutdown");
				System.out.println("Closed");
//				e.printStackTrace();
			}
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void shutdown() {
		try {
			this.in.close();
			running = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
