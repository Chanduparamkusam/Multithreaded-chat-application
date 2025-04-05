package com.chatapplication;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.print("Enter your name: ");
            String username = consoleInput.readLine();
            out.println(username + " has joined the chat!");

            System.out.println("Connected to the chat server as " + username);
            System.out.print("You: ");

            // Thread to read messages from server
            new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("\n" + serverMsg);
                        System.out.print("You: ");
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            // Main thread for sending messages
            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                out.println(username + ": " + userInput);  // Send with name
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
