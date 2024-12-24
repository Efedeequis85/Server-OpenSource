package com.da.irc.Entidades;

import java.nio.channels.SocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserDirectory<U> {
    private final CopyOnWriteArrayList<U> listado;
    
    public UserDirectory(CopyOnWriteArrayList<U> listado) {
        this.listado = listado;
    }
    
    public CopyOnWriteArrayList<U> getUserDirectory() {
        return listado;
    }
    
    public void setUser(U user) {
        listado.add(user);
    }
    
    public void deleteUser(SocketChannel s) {
        listado.remove(user);
    }
}
