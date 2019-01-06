package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerReader extends Thread {
	private Socket clientSocket;
	private String clientIP_txt;
	private ServerAccept mainServ;
	
	private String clientName;
	
	private boolean running;
	
	private InputStream inFromServer;
	private DataInputStream in;

	ServerReader(Socket clientSocket, ServerAccept mainServ) throws IOException {
		this.clientSocket = clientSocket;
		this.mainServ = mainServ;
		
		inFromServer = clientSocket.getInputStream();
		in = new DataInputStream(inFromServer);
		running= true;
	}

	public void run() {
		// Save IP:Port To String
		this.clientIP_txt = this.clientSocket.getRemoteSocketAddress().toString();
		
		this.mainServ.appendLog(">>" + this.clientIP_txt + " Connected");
		
		while(running) {
			try {
				// Waiting for in message
				String inMsg = in.readUTF();
				String[] message = inMsg.split(":");
				
				switch (message[0]) {
				case "%$Msg":
					mainServ.bCastMsg(clientName +": "+ message[1]);
					mainServ.appendLog(clientName + "[" + this.clientIP_txt + "]: " + message[1]);
					break;
				case "%$SetName":
					// check if client did not enter anything 
					clientName = "default";
					if(message.length == 2) {
						clientName = message[1];
					}
				
					// avoid collision
					Thread.sleep(10);
					
					// Broadcast join message
					mainServ.appendLog(clientIP_txt+"-->"+clientName);
					mainServ.bCastMsg(">>>>[" + clientName + "] joined the room<<<<");
					break;
				case "%$GoodBye":
					if(clientName != null)
						mainServ.bCastMsg(">>>>[" + clientName + "] Disconnect<<<<");
					mainServ.appendLog(">>"+clientIP_txt+"[" + clientName + "] Disconnect");
					mainServ.removeMember(clientSocket);
					this.shutdown();
					break;

				default:
					mainServ.appendLog("Where message come from?");
				}
			} catch (IOException | InterruptedException e) {
				//client disconnect for some reason
				this.shutdown();
				System.out.println(Thread.currentThread().getName() + " Disconnect With Client");
			}
		}
	}
	public void shutdown() {
		try {
			running = false;
			this.in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}