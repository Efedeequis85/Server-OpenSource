package com.da.irc.Servicios;

import java.nio.channels.SocketChannel;

public class Controlador {  
    private final Registrador reg;
    
    public Controlador(Registrador reg) {
        this.reg = reg;
    }
    
    public void registrarSocket(SocketChannel s, String datos) {
        reg.registrar(s, datos);
    }
    
    public void eliminarSocket(SocketChannel s) {
        reg.eliminar(s);
    }
    
    public void enviar(SocketChannel s, String user, String datos) {
        reg.enviar(s, user, datos);
    }
    
    public void procesarDatos(SocketChannel socket, String datos) {
        String isRegistred = reg.isRegistred(socket);
        if(isRegistred == null) {
            registrarSocket(socket, datos);
        }
        else {
            enviar(socket, isRegistred, datos);
        }
    }
}
