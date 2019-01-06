package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ClientConnect extends Thread {
	private Thread t;
	private String ipAddress;
	private int port;
	private TextArea chat_log;

	private boolean setName;

	private volatile boolean running;
	private ClientReader clientReader;

	private Socket mySocket;
	private Label title_label;

	public ClientConnect(TextArea chat_log, Label title_label) {
		this("127.0.0.1", 50000, chat_log,title_label);
	}
	
	public ClientConnect(String ipAddress, int port, TextArea chat_log,Label title_label) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.chat_log = chat_log;
		this.running = true;
		this.setName = false;
		this.title_label = title_label;
	}
	
	public void run() {
		try {
			// System.out.println("connecting to "+this.ipAddress);
			//Connect To Server
			mySocket = new Socket(this.ipAddress, this.port);
			appendLog("[System] Connected");
			appendLog("[System] What is your name?");

			// Create Reader
			this.clientReader = new ClientReader(mySocket, this);
			clientReader.start();
			
			// Keep thread alive
			while (this.running) {
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			appendLog("[System] Server Not Available");
			// e.printStackTrace();
		}
	}
	
	public void sendMsg(String sendmsg) {
		OutputStream outToServer;
		try {
			if (mySocket != null) {
				outToServer = this.mySocket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				System.out.println("Sending...");
				if(sendmsg.equalsIgnoreCase("%$GoodBye")) {
					out.writeUTF(sendmsg);
					return;
				}
				if (setName)
					out.writeUTF("%$Msg:"+sendmsg);
				else {
					out.writeUTF("%$SetName:" + sendmsg);
					title_label.setText("Chat Room("+sendmsg+")");
					setName = true;
				}
			}
			else {
				System.out.println("");
			}
		} catch (IOException e) {
			appendLog("[System] Server was Shutdown");
			e.printStackTrace();
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void appendLog(String txt) {
		this.chat_log.appendText(txt+"\n");
	}

	public void shutdown() {
		this.running = false;
		if (clientReader != null) {
			this.clientReader.shutdown();
		}
	}
}
