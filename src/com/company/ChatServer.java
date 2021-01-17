package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private List<Room> userRooms = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);
            int roomId = 0;
            int userCounter = 1;
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(roomId, socket, this);
                newUser.start();

                if (userCounter%2==0){
                    Room roomThread1 = userRooms.get(userRooms.size()-1);
                    roomThread1.setUser2(newUser);
                    userRooms.remove(userRooms.size()-1);
                    userRooms.add(roomThread1);
                } else {
                    Room roomThread = new Room(roomId, newUser, null);
                    userRooms.add(roomThread);
                    System.out.println("New room created with id = " + roomId);
                }
                userCounter++;
                roomId++;
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("Syntax: java ChatServer <port-number>");
//            System.exit(0);
//        }

//        int port = Integer.parseInt(args[0]);
        int port = 5005;
        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, Integer roomId, UserThread author) {
        System.out.println(message);
        for (Room userRoom : userRooms) {
            if (userRoom.getIdRoom() == roomId){
                for (UserThread user : userRoom.getUsers()) {
                    if (user != null && user.equals(author)) {
                        user.sendMessage(message);
                    }
                }
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, Integer roomId, UserThread author) {
        boolean removed = userNames.remove(userName);
        if (removed){
            for (Room userRoom : userRooms) {
                if (userRoom.getIdRoom().equals(roomId)){
                    userRoom.getUsers().removeIf(user -> user == author);
                }
            }
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
