package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class ServerReader extends Thread {
	Thread t;

	//Socket Staff
	private ServerAccept mainServ;
	private Socket clientSocket;
	
	private boolean threadAlive;
	
	// Network comm
	private InputStream inFromServer;
	private DataInputStream in;
	
	// Display Staff
	private String clientIP_txt;
	
	public ServerReader(Socket clientSocket, ServerAccept mainServ) {
		this.threadAlive = true;
		this.clientSocket = clientSocket;
		this.clientIP_txt = clientSocket.getRemoteSocketAddress().toString().substring(1);
		this.mainServ = mainServ;
		this.mainServ.appendLog(">>" + clientIP_txt + " Connected");
		try {
			inFromServer = clientSocket.getInputStream();
			in = new DataInputStream(inFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		while(this.threadAlive) {
			try {
				String inMsg = in.readUTF();
				System.out.println("inMsg:"+inMsg);
				String[] processMsg = inMsg.split(":");
				switch (processMsg[0]) {
				case "%$HitBorder":
					mainServ.checkLeftRight(clientSocket,processMsg[1]);
					break;
				default:
					mainServ.appendLog("What?");
					break;
				}
//				mainServ.appendLog(clientSocket + ":"+inMsg);
			} catch (IOException e) { // client disconnect
				System.out.println(e);
				this.mainServ.appendLog(">>" + clientIP_txt + " Disconnected");
				mainServ.removeClientList(this.clientSocket);
				this.shutdown();
				System.out.println("Thread Terminated");
			}
//			finally {
//			}
		}
	}
	

	public void start() {
		if (t == null) {
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
			mainServ.appendLog("Server is not open");
			System.out.println(e);
		}
	}
}
