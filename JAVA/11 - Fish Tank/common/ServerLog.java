package common;

import javafx.scene.control.TextArea;

public class ServerLog extends TextArea{
	
	public ServerLog(int width,int height) {
		this.setEditable(false);
		this.setPrefWidth(width);
		this.setPrefHeight(height);
	}
	
	public void appendLog(String txt) {
		this.appendText(txt+"\n");
	}
}
