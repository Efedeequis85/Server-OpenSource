package com.da.irc.Servicios;


import com.da.irc.Entidades.User;
import com.da.irc.Entidades.UserDirectory;
import java.nio.channels.SocketChannel;

public class Registrador {

    private final UserDirectory<User> usuarios;
    private final Sender env;
    
    public Registrador(UserDirectory<User> usuarios, Sender env) {
        this.usuarios = usuarios;
        this.env = env;
    }
    
    public void eliminar(SocketChannel s) {
        usuarios.deleteUser(s);
    }
    
    public void registrar(SocketChannel s, String username) {
        User user = new User(s, username);
        usuarios.setUser(user);
    }
    
    public String isRegistred(SocketChannel socket) {
        for(User u: usuarios.getUserDirectory()) {
            if(socket == u.socket)
                return u.username;
        }
        return null;
    }
    
    public void enviar(SocketChannel s, String user, String datos) {
        env.run(s, user, datos);
    }
}
