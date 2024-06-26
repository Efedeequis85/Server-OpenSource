package da.aplicaciondechat;

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
import java.util.concurrent.CopyOnWriteArrayList;

public class Receptor{
    
    private static CopyOnWriteArrayList<SocketChannel> socks;
    
    public Receptor(CopyOnWriteArrayList<SocketChannel> socks) {
        this.socks = socks;
    }
    
    public void run() {
        try {
            // Crear un selector
            Selector selector = Selector.open();
            
            // Crear un canal de servidor y configurarlo como no bloqueante
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
            serverSocketChannel.configureBlocking(false);
            
            // Registrar el canal de servidor con el selector y configurarlo para aceptar conexiones
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            // En esta lista se van a almacenar los conjuntos de datos que se van a enviar
            CopyOnWriteArrayList<String> mensajes = new CopyOnWriteArrayList<>();
            
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
                        // Almacenar SocketChannel para envio de datos
                        socks.add(socketChannel);
                        
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
                            socks.remove(socketChannel);
                            continue;
                        }
                        
                        // Procesar los datos recibidos
                        buffer.flip();
                        byte[] data = new byte[bytesRead];
                        buffer.get(data);
                        String mensaje = new String(data, StandardCharsets.UTF_8);
                        
                        // Difusion de mensaje
                        mensajes.add(mensaje);
                        Sender sender = new Sender(socks, mensajes, socketChannel);
                        sender.start();
                    }
                    
                    // Eliminar la clave seleccionada para que no se vuelva a procesar
                    keyIterator.remove();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
