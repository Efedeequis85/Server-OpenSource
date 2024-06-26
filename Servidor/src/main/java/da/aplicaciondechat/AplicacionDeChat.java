package da.aplicaciondechat;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class AplicacionDeChat {

    public static void main(String[] args) throws IOException, InterruptedException {

        CopyOnWriteArrayList<SocketChannel> socks = new CopyOnWriteArrayList<>();
        Receptor recv = new Receptor(socks);
	recv.run();
    }
}

