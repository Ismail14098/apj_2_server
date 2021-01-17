package com.company;

import java.util.ArrayList;
import java.util.List;

public class Room extends Thread {
    private Integer id;
    private UserThread user1;
    private UserThread user2;

    public Room(Integer id, UserThread user1, UserThread user2){
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Integer getIdRoom() {
        return id;
    }

    public void setIdRoom(Integer id) {
        this.id = id;
    }

    public List<UserThread> getUsers(){
        List<UserThread> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        return users;
    }

    public UserThread getUser1() {
        return user1;
    }

    public void setUser1(UserThread user1) {
        this.user1 = user1;
    }

    public UserThread getUser2() {
        return user2;
    }

    public void setUser2(UserThread user2) {
        this.user2 = user2;
    }
}
