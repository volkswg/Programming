package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import common.ClientSocketHolder;
import common.ServerLog;

public class ServerAccept extends Thread{
	Thread t;
	private volatile boolean threadAlive;
	private int count;
	
	//Socket Staff
	private ServerSocket serverSocket;
	private ArrayList<ServerReader> readerList;
	private ArrayList<ClientSocketHolder> clientSocketList;
	
	//Network comm
	private OutputStream outToClient;
	private DataOutputStream out;
	
	
	//Display Staff
	private ServerLog serverLog;

	public ServerAccept(ServerLog serverLog) {
		this(50000,serverLog);
	}
	public ServerAccept(int serverPort, ServerLog serverLog) {
		//Initial Variable
		this.threadAlive = true; 
		this.serverLog = serverLog;
		this.readerList = new ArrayList<>();
		this.clientSocketList = new ArrayList<>();
		this.count = 0;
		// Start Server
		try {
			this.serverSocket = new ServerSocket(serverPort);
			if(this.serverSocket != null) {
				appendLog("Server Started At "+ 
						InetAddress.getLocalHost().getHostAddress() + 
						":" + serverPort);
				
			}
		}
		catch (Exception e) {// Cannot Open server
			appendLog("Duplicated IP or Port number");
			this.threadAlive = false;
		}
	}
	
	
	public void run() {
		while(this.threadAlive) {
			// Doing my Staff
			try {
				// waiting for client to connect
				Socket clientSocket = this.serverSocket.accept();
				
				// Add client to ArrayList
				clientSocketList.add(new ClientSocketHolder(clientSocket, "node"+(count++)));
				ServerReader serverReader = new ServerReader(clientSocket, this);
				readerList.add(serverReader);
				serverReader.start();
			} 
			catch (IOException e) {
				//Server Closed
				appendLog("Closed");
				System.out.println(e);
			}
		}
	}
	
	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void appendLog(String txt) {
		serverLog.appendLog(txt);
	}
	
	public void printAllClientList() {
		for(ClientSocketHolder socketHolder : clientSocketList) {
			System.out.println(socketHolder.toString());
		}
	}
	
	public void checkLeftRight(Socket clientSocket,String parameter) {
		ClientSocketHolder currentNode = null;
		ClientSocketHolder leftNode = null;
		ClientSocketHolder rightNode = null;
		// parameter : generateSide, Speed, yPos, direction,FishType
		System.out.println("Got para:"+parameter);
		String sendCheck = parameter.split(",")[0];
		
		
		if(clientSocketList.size() == 1) {
//			appendLog(clientSocketList.get(0).getNodeName() + "olny1Node:HitBoder");
			sendMsg("%$GenBall:"+parameter, clientSocketList.get(0).getClientSocket());
		}
		else {
			for(int i = 0; i < clientSocketList.size();i++) {
				ClientSocketHolder tmp = clientSocketList.get(i);
				if(tmp.getClientSocket().equals(clientSocket)){
					currentNode = tmp;
					if(i+1 == clientSocketList.size()) { // Last Right Node
						leftNode = clientSocketList.get(i-1);
						rightNode = clientSocketList.get(0);
					}
					else if(i-1 < 0) { // Last Left Node
						leftNode = clientSocketList.get(clientSocketList.size()-1);
						rightNode = clientSocketList.get(i+1);
					}
					else {
						leftNode = clientSocketList.get(i-1);
						rightNode = clientSocketList.get(i+1);
					}
					break;
				}
			}
			if(clientSocketList.size() != 1) {
				if(sendCheck.equalsIgnoreCase("right")) 
					sendMsg("%$GenBall:"+parameter, leftNode.getClientSocket());
				else 
					sendMsg("%$GenBall:"+parameter, rightNode.getClientSocket());
			}
		}
	}
	
	public void sendMsg(String message,Socket client) {
		try {
			outToClient = client.getOutputStream();
			out = new DataOutputStream(outToClient);
			out.writeUTF(message);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bCastMsg(String message) {
		for(ClientSocketHolder clientHolder:clientSocketList) {
			sendMsg(message, clientHolder.getClientSocket());
		}
	}
	
	public void removeClientList(Socket delSocket) {
		if(clientSocketList.size() == 1) {
			clientSocketList.remove(0);
			return;
		}
		for(ClientSocketHolder socketHolder : clientSocketList) {
			if(socketHolder.getClientSocket().equals(delSocket)) {
				clientSocketList.remove(socketHolder);
			}
		}
	}
	
	public void shutdown() {
		this.threadAlive = false;
		
		// Close server
		try {
			this.serverSocket.close();
		} catch (Exception e) {
			// Server Socket is null
			appendLog("Server is not open");
			System.out.println(e);
		}
	}
}
