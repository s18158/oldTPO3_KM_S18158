/**
 *
 *  @author Kondej Mariusz S18158
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Client {

  private static final int BUFFER_SIZE = 2048;

  @SuppressWarnings("unused")
  public static void main(String[] args) throws IOException, InterruptedException {

    InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);
    SocketChannel client = SocketChannel.open(socketAddress);

    log("Connecting on port 1111");

    ArrayList<String> msgToSend = new ArrayList<>();

    //some test vars
    msgToSend.add("Hi");
    msgToSend.add("Its me");
    msgToSend.add("Mario");
    msgToSend.add("are you");
    msgToSend.add("Luigi?");

    for (String s : msgToSend){

      byte[] msg = new String(s).getBytes();
      ByteBuffer buffer = ByteBuffer.wrap(msg);
      client.write(buffer);
      Thread.sleep(100);
      ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
      client.read(readBuffer);

      log("sending: " + s);
      buffer.clear();

      String result = new String(readBuffer.array()).trim();
      System.out.println(s);


      Thread.sleep(400);

    }

    client.close();

  }

  private static void log(String s){
    System.out.println(s);
  }

}
