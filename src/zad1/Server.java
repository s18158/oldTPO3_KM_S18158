/**
 *
 *  @author Kondej Mariusz S18158
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Server {

  @SuppressWarnings("unused")
  public static void main(String[] args) throws IOException {

    //Selector
    Selector selector = Selector.open();

    //ServerSocketChannel
    ServerSocketChannel socketChannel = ServerSocketChannel.open();
    InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);

  }
}
