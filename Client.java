import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;

public class Client {
    
    private static DatagramSocket dgSocket;
    // private MulticastSocket socket;
    // private ReceiveThread receiveThread;

    
    public Client(String ip, int port) throws IOException {
        
        // important that this is a multicast socket to receive from server

        
        //socket to send to server, random port assigned to socket
        dgSocket = new DatagramSocket();
        
        //create thread to receive messages
        // receiveThread = new ReceiveThread(socket = new MulticastSocket(port));


        // join by ip
        // socket.joinGroup(InetAddress.getByName(ip));
    }
    
    //send message to server
    public void sendMessage(String message, String ip, int port) throws IOException{
        System.out.println("send method invoked");
        // make datagram packet
        byte[] buffer = message.getBytes();
        byte[] seqNumberByte = intToByte(4);
        byte[] send = mergeSeqAndMessage(seqNumberByte, buffer);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                InetAddress.getByName(ip), port);
        // send packet
        dgSocket.send(packet);
    }

    //receive message moved to receive thread
    // public void printMessage() throws IOException{
    //     // make datagram packet to recieve
    //     byte[] message = new byte[1024*4];
    //     DatagramPacket packet = new DatagramPacket(message, message.length);
        
    //     // recieve the packet
    //     socket.receive(packet);
    //     System.out.println(new String(message, 0, packet.getLength()));
    // }

    public static void main(String[] args) {

    try {
        //final String ip = args[0];
        
        System.setProperty("java.net.preferIPv4Stack", "true");
        Scanner sc = new Scanner(System.in);
        //final int port = Integer.parseInt(args[1]);
        //Client client = new Client(ip, port);
        Client client = new Client("225.6.7.8", 3456);

        // Create socket
        MulticastSocket mcsocket = new MulticastSocket(3456);
        mcsocket.joinGroup(InetAddress.getByName("225.6.7.8"));

        // Create and run receive thread
        Thread receiveThread = new Thread(new ReceiveThread(mcsocket));
        receiveThread.start();
        System.out.println("enter name: ");
        String name = sc.nextLine();

        // Send name to server
        client.sendMessage(name,"192.168.43.200", 3568 );

        while(true){
            String message = sc.nextLine(); 
            client.sendMessage(message,"192.168.43.200", 3568 );
        }

        //client.close();
    } catch (IOException ex) {
        ex.printStackTrace();}
}
    public static byte[] intToByte(int num) {
        return ByteBuffer.allocate(4).putInt(num).array();
    }
    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
    public static byte[] mergeSeqAndMessage(byte[] seqNumber, byte[] message){
        int length = seqNumber.length + message.length;
        byte[] merged = new byte[length];
        System.arraycopy(seqNumber, 0, merged, 0, seqNumber.length);
        System.arraycopy(message, 0, merged, seqNumber.length, message.length);
        return merged;
    }
    public static int getSeqNumber(byte[] merged){
        byte[] seqNumber = new byte[4];
        System.arraycopy(merged, 0, seqNumber, 0, seqNumber.length);
        return byteArrayToInt(seqNumber);
    }
}