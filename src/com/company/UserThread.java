package com.company;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private Integer roomId;
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Integer roomId,Socket socket, ChatServer server) {
        this.roomId = roomId;
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            System.out.println("User connection started");
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, roomId, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, roomId,this);
//                System.out.println(serverMessage);
            } while (!clientMessage.equals("bye"));

            server.removeUser(userName,roomId, this);
            socket.close();

//            serverMessage = userName + " has quitted.";
//            server.broadcast(serverMessage, roomId,this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
//        if (server.hasUsers()) {
//            writer.println("Connected users: " + server.getUserNames());
//        } else {
//            writer.println("No other users connected");
//        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
