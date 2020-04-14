/**
 *
 *  @author Kondej Mariusz S18158
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

  @SuppressWarnings("unused")
  public static void main(String[] args) throws IOException {

    // Selector: multiplexer of SelectableChannel objects
    Selector selector = Selector.open();

    // ServerSocketChannel: selectable channel for stream-oriented listening sockets
    ServerSocketChannel socketChannel = ServerSocketChannel.open();
    InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);

    // Binds the channel's socket to a local address and configures the socket to listen for connections
    socketChannel.bind(socketAddress);

    // Adjusts this channel's blocking mode.
    socketChannel.configureBlocking(false);

    int ops = socketChannel.validOps();
    SelectionKey selectionKey = socketChannel.register(selector, ops, null);

    // Keep server running
    while(true){

      log("waiting");
      // Selects a set of keys whose corresponding channels are ready for I/O operations
      selector.select();

      // token representing the registration of a SelectableChannel with a Selector
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      while(iterator.hasNext()){

        SelectionKey myKey = iterator.next();

        // Tests whether this key's channel is ready to accept a new socket connection
        if (myKey.isAcceptable()){

          SocketChannel client = socketChannel.accept();

          // Adjusts this channel's blocking mode to false
          client.configureBlocking(false);

          // Operation-set bit for read operations
          client.register(selector, SelectionKey.OP_READ);
          log("Connection accepted: " + client.getLocalAddress() + "\n");

          // Tests whether this key's channel is ready for reading
        }else if (myKey.isReadable()){

          SocketChannel client = (SocketChannel) myKey.channel();
          ByteBuffer byteBuffer64 = ByteBuffer.allocate(8*64);
          client.read(byteBuffer64);
          String rslt = new String(byteBuffer64.array()).trim();

          log("Msg received: " + rslt);

          if (rslt.equals("Luigi?")){

            client.close();

          }

        }

      }
      iterator.remove();

    }

  }

  private static void log(String s){
    System.out.println(s);
  }

}
