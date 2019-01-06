package common;

import java.net.Socket;

public class ClientSocketHolder {
	private Socket clientSocket;
	private String nodeName;
	
	public ClientSocketHolder(Socket clientSocket, String nodeName) {
		this.clientSocket = clientSocket;
		this.nodeName = nodeName;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
//	public void setClientSocket(Socket clientSocket) {
//		this.clientSocket = clientSocket;
//	}
	public String getNodeName() {
		return nodeName;
	}
//	public void setNodeName(String nodeName) {
//		this.nodeName = nodeName;
//	}
	@Override
	public String toString() {
		return nodeName + ":" + clientSocket.toString();
		
	}
	
}
