package com.da.irc.Servicios;

import com.da.irc.Entidades.User;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;


public class Sender {
    private final CopyOnWriteArrayList<User> socks;
    
    public Sender(CopyOnWriteArrayList<User> socks) {
        this.socks = socks;
    }
    
    public void run(SocketChannel sock, String user, String m) {
        try {

            String mensaje = user + m + "\n";
            byte[] dataBytes = mensaje.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
            
            for(User u: socks) {
                if(u.socket != sock) {
                    u.socket.write(buffer);
                    buffer.clear();
                }
            }
        } catch (IOException ex) {          
            System.out.println("Error en el enviador");
        }
    }
}
