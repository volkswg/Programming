package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.control.TextField;

public class ClientConnect extends Thread {
	Thread t;
	
	// Socket Staff
	private Socket mySocket;
	private volatile boolean threadAlive;
	private ClientReader clientReader;
	
	//Network comm
	private OutputStream outToServer;
	private DataOutputStream out;
	private TextField genBall;
	private TextField delBall;
		
	public ClientConnect(TextField genBall,TextField deleteBalltest) {
		this("127.0.0.1", 50000,genBall,deleteBalltest);
	}
	public ClientConnect(String ipAddress,int port,TextField genBall,TextField delBall) {
//		Platform.runLater(arg0);
		this.genBall = genBall;
		this.delBall = delBall;
		this.threadAlive = true;
		try {
			mySocket = new Socket(ipAddress, port);
			if(mySocket != null)
				System.out.println("Connected to server");
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
//			e.printStackTrace();
			System.out.println(e);
			System.out.println("[System] Server Not Available");
			this.shutdown();
		}
	}
	public void run() {
		if(mySocket != null) {
			clientReader = new ClientReader(mySocket,this);
			clientReader.start();
			
		}
		while(this.threadAlive) {
		}
	}
	
	public void start() {
		if(t==null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void sendMsg(String message) {
		try {
			if(mySocket!= null) {
				outToServer = mySocket.getOutputStream();
				out = new DataOutputStream(outToServer);
				out.writeUTF(message);
			}
		} catch (IOException e) {
			System.out.println("Cannot Send Message To Server");
//			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		this.threadAlive = false;
		
		try {
			if(mySocket != null)
				mySocket.close();
		} catch (IOException e) {
			System.out.println("Server is not open");
			System.out.println(e);
//			e.printStackTrace();
		}
	}
	
	public void generateBall(String parameterIn)
	{
		genBall.setText(parameterIn);
	}
	public void deleteBall(String ballId) {
		delBall.setText(ballId);
	}
	
	public boolean checkStatus() {
		return mySocket == null?false:true;
	}
}
