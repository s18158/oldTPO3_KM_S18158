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

      log("sending: " + s);
      buffer.clear();

      Thread.sleep(3000);

    }

    client.close();

  }

  private static void log(String s){
    System.out.println(s);
  }

}
