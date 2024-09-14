package com.da.irc.Entidades;

import java.nio.channels.SocketChannel;

public class User {
    public SocketChannel socket;
    public String username;

    public User(SocketChannel socket, String username) {
        this.socket = socket;
        this.username = username;
    }
}
