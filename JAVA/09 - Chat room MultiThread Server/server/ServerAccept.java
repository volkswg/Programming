package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.TextArea;

public class ServerAccept extends Thread {
	private Thread t;
	private TextArea logDisplay;
	private ServerSocket serverSocket;

	private boolean running;

	// member of room
	private ArrayList<Socket> member;
	// reader thread
	private ArrayList<ServerReader> readerMember;

	public ServerAccept(TextArea logDisplay) throws IOException {
		this(50000, logDisplay);
	}

	public ServerAccept(int serverPort, TextArea logDisplay) throws IOException {
		running = true;
		this.logDisplay = logDisplay;
		// start Server Socket
		try {
			this.serverSocket = new ServerSocket(serverPort);
		} catch (Exception e) {
			System.out.println("Duplicated IP or Port");
			running = false;
		}

		// Set Running Flag to True
		appendLog("Server Started at " + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort);

		// initial ArrayList member, readerMember
		member = new ArrayList<>();
		readerMember = new ArrayList<>();
	}

	public void run() {
		try {
			while (running) {
				System.out.println("Waiting For Client...");
				// server accept the client connection request
				Socket clientSocket = this.serverSocket.accept();

				// Add client Socket to ArrayList
				member.add(clientSocket);

				// Create Reader Thread
				ServerReader readerThread = new ServerReader(clientSocket, this);
				
				// Add Reader Thread to ArrayList
				readerMember.add(readerThread);
				readerThread.start();
			}
		} 
		catch (Exception e) {
			System.out.println(e.toString());
		} 
		finally {
			if (this.serverSocket == null) {// cannot open socket at ip or port
				appendLog("Duplicated IP or Port");
			} 
			else {// start server then stop
				appendLog("Server Closed");
			}
			System.out.println(this.getName() + " Terminated");
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public void bCastMsg(String message) {
		// loop all member Socket
		for (Socket client : member) {
			OutputStream outToServer;
			try {
				outToServer = client.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				System.out.println("Bcast Sending..." + message);
				out.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Remove dead socket from ArrayList
	public void removeMember(Socket delSocket) {
		if(member.size() == 1) {
			member.remove(0);
			return;
		}
		for (Socket client : member) {
			if (delSocket.equals(client)) {
				member.remove(client);
			}
		}
	}

	// Display log
	public void appendLog(String txt) {
		this.logDisplay.appendText(txt+"\n");
	}
	
	public void printallmem() {
		for(Socket client:member) {
			System.out.println(client.toString());
		}
	}

	public void shutdown() {
		// cannot open socket at IP or Port
		if(this.serverSocket != null) {
			try {
				// close Server Socket
				this.serverSocket.close();
				
				// Set Running flag to break loop
				this.running = false;
				
				// Shutdown all ServerReader thread
				for (ServerReader reader : readerMember) {
					reader.shutdown();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
