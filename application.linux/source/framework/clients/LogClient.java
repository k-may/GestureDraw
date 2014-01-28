package framework.clients;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogClient {
	protected String filePath;

	public LogClient(String filePath){
		this.filePath = filePath;
	}
	
	public void addLog(String time, String msg){
		append("[" + time + "] " + msg);
	}
	
	protected void append(String text) {
		File f = new File(filePath.trim());
		if (!f.exists()) {
			createFile(f);
		}
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			out.println(text);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFile(File f) {
		File parentDir = f.getParentFile();
		try {
			parentDir.mkdirs();
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
