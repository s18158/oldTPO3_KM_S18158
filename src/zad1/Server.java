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

  private static final int BUFFER_SIZE = 2048;
  private static Selector selector = null;
  private static boolean ifNewMessage = false;
  private static ByteBuffer newMessage = ByteBuffer.allocate(BUFFER_SIZE);

  @SuppressWarnings("unused")
  public static void main(String[] args) throws IOException, InterruptedException {

    boolean running = true;

    // Selector: multiplexer of SelectableChannel objects
    selector = Selector.open();

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
    while(running){

      log("waiting");
      // Selects a set of keys whose corresponding channels are ready for I/O operations
      selector.select();

      // token representing the registration of a SelectableChannel with a Selector
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();
      SelectionKey myKey = iterator.next();

      // Tests whether this key's channel is ready to accept a new socket connection
      if (myKey.isAcceptable()){

        SocketChannel client = socketChannel.accept();

        // Adjusts this channel's blocking mode to false
        client.configureBlocking(false);

        // Operation-set bit for read operations
        client.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        log("Connection accepted: " + client.getLocalAddress() + "\n");

        // Tests whether this key's channel is ready for reading
      }else if (myKey.isReadable()){

        SocketChannel client = (SocketChannel) myKey.channel();
        ByteBuffer byteBuffer64 = ByteBuffer.allocate(BUFFER_SIZE);
        client.read(byteBuffer64);
        String result = new String(byteBuffer64.array()).trim();

        log("Msg received: " + result);

        ifNewMessage = true;
        newMessage = byteBuffer64;

        if (result.equals("Luigi?")){

          client.close();

          log("end");
          running = false;

        }

      }else if (myKey.isWritable()) {

        //System.out.println("key is writable newMessage:" + ifNewMessage);

        if (ifNewMessage){

          //System.out.println("i'm here");

          SocketChannel client = (SocketChannel) myKey.channel();
          client.write(newMessage);
          ifNewMessage = false;
          newMessage.clear();

        }

      }

      Thread.sleep(500);

      while(iterator.hasNext()){

      }
      iterator.remove();

    }

  }

  private static void log(String s){
    System.out.println(s);
  }

}
