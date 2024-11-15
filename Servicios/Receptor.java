package com.da.irc.Servicios;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Receptor{
    
    private final Controlador controlador;
    
    public Receptor(Controlador controlador) {
        this.controlador = controlador;
    }
    
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            
            serverSocketChannel.socket().bind(new InetSocketAddress("192.168.1.35", 8888));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            while (true) {
                // El metodo select bloquea la ejecucion del programa hasta que se reciban datos
                selector.select();
                
                // Obtener las claves seleccionadas
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    
                    if (key.isAcceptable()) { 
                        // Aceptar la nueva conexión
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        
                        // Registrar el nuevo canal con el selector para lectura
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        
                        // Leer datos del canal
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = socketChannel.read(buffer);
                        
                        
                        // Verifica si se cerro la conexion
                        if (bytesRead == -1) {
                            // Si el cliente cierra la conexión, cancelar la clave y cerrar el canal
                            key.cancel();
                            socketChannel.close();
                            continue;
                        }
                        
                        // Procesar los datos recibidos
                        buffer.flip();
                        byte[] data = new byte[bytesRead];            
                        controlador.procesarDatos(socketChannel, data);
                    }
                    
                    // Eliminar la clave seleccionada para que no se vuelva a procesar
                    keyIterator.remove();
                }
            }
        } catch (IOException ex) {
            System.out.println("Error en el receptor.");
        }
    }
}
