package detty.test.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import detty.test.chat.client.ClientMain;

public class DummyServer {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Dummy server started.");
		ServerSocket serverSocket = new ServerSocket(ClientMain.PORT);
		for (;;) {
			System.out.println("Waiting for client.");
			Socket socket = serverSocket.accept();
			for (int i = 0; i < 10; i++) {
				socket.getOutputStream().write(("Poruka broj " + i).getBytes());
				socket.getOutputStream().flush();
				System.out.println("Sent message: " + i);
				Thread.sleep(1000);
			}
		}
	}
	
}
