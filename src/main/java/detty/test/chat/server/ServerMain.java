package detty.test.chat.server;

import detty.test.chat.client.ClientMain;

public class ServerMain {
	public static void main(String[] args) {
		ChatServer server = new ChatServer(ClientMain.PORT);
		server.start();
	}
}
