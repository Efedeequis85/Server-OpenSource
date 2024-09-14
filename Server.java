package com.da.irc;

import com.da.irc.Entidades.User;
import com.da.irc.Entidades.UserDirectory;
import com.da.irc.Servicios.Controlador;
import com.da.irc.Servicios.Receptor;
import com.da.irc.Servicios.Registrador;
import com.da.irc.Servicios.Sender;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static void main(String[] args) {
        CopyOnWriteArrayList<User> usuarios = new CopyOnWriteArrayList<>();
        UserDirectory<User> userDirectory = new UserDirectory<>(usuarios);
        Sender env = new Sender(usuarios);
        Registrador reg = new Registrador(userDirectory, env);
        Controlador controlador = new Controlador(reg);
        Receptor recv = new Receptor(controlador);
        recv.run();
    }
}
