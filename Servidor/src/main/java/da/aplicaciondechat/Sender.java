package da.aplicaciondechat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sender extends Thread {
    
    private final CopyOnWriteArrayList<SocketChannel> socks;
    private final CopyOnWriteArrayList<String> mensajes;
    private SocketChannel sock;
    
    public Sender(CopyOnWriteArrayList<SocketChannel> socks, CopyOnWriteArrayList<String> mensajes, SocketChannel sock) {
        this.socks = socks;
        this.mensajes = mensajes;
        this.sock = sock;
    }
    
    @Override
    public void run() {
        
        for(String m: mensajes) {        
            for(SocketChannel s: socks) {
                if(s == sock)
                    continue;
                    
                try {
                    
                    String mensaje = sock.getRemoteAddress().toString() + " " + m;
                    byte[] dataBytes = mensaje.getBytes();
                    ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
                    
                    s.write(buffer);        
                    mensajes.remove(m);
                } catch (IOException ex) {
                    System.out.println("Error al enviar datos");
                }
            }
        }           
    }
}
