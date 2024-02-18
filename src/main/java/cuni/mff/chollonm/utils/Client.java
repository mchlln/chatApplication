package cuni.mff.chollonm.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a client that connects to the chat server.
 * Allows the user to enter their name, send messages to the server, and receive messages from other clients.
 */
public class Client {

    /**
     * The main method that starts the client application.
     * It establishes a connection to the chat server, sends the user's name to the server,
     * and handles sending and receiving messages.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if an I/O error occurs
     */public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 1234);

        // Create data input and output streams
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        // Get client name and send it to the server
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        output.writeUTF(name);
        System.out.println("To exit the chat, just type exit");
        System.out.println("To display help, type -hp");

        Thread thread = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    String received = input.readUTF();
                    System.out.println(received);
                } catch (IOException e) {
                    System.err.println("Disconnected from the server.");
                    break;
                }
            }
        });
        thread.start();

        // Infinite loop to send messages to the server.
        while (true) {
            String message = scanner.nextLine();
            output.writeUTF(message);
            if (message.equals("exit")) {
                try {
                    thread.join();
                    socket.close();
                    input.close();
                    output.close();
                    scanner.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
