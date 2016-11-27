package detty.test.chat.client;

import java.util.Scanner;

public class ClientMain {
	public static final int PORT = 2255;
	public static final String ADDRESS = "localhost";
	
	public static void main(String[] args) throws Exception {
		System.out.println("Connecting to server...");
		System.out.println(ADDRESS + ":" + PORT);
		ChatClient client = new ChatClient("Tester");
		client.connect(ADDRESS, PORT);
		System.out.println("connected to server");
		try (Scanner in = new Scanner(System.in)) {
			for (;;) {
				String message = in.nextLine();
				
				if (message.equals("\\q")) {
					System.out.println("Shuting down client...");
					System.exit(0);
				}
				client.sendMessage(message);
			}
		}
	}
	
	
}
