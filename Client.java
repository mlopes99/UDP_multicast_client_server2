/** Client Class for sending and receiving messages sent to and from the server
 * @author Michael Scott, Michelle Lopes, Tuscanny Botha
 */

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Scanner;

public class Client {
    
    private static DatagramSocket dgSocket;
    private String ipAdd;
    int portNo;


    /** Creates an instance of Client Object
    **/
    public Client(String ip, int port) throws IOException {
       
        // Socket to send to server, random port assigned to socket
        dgSocket = new DatagramSocket();
    }

    /** send message and sequence number to server
     * @param message
     * @param seqNumber
     * @param ip
     * @param port
     * @throws IOException
     */
    public void sendMessage(String message, int seqNumber, String ip, int port) throws IOException{

        // Make datagram packet
        byte[] buffer = message.getBytes();
        byte[] seqNumberByte = intToByte(seqNumber);
        int hash = message.hashCode();
        byte[] hashByte = intToByte(hash);
        byte[] send = mergeSeqAndMessage(seqNumberByte, hashByte, buffer);
        DatagramPacket packet = new DatagramPacket(send, send.length,
        InetAddress.getByName(ip), port);
        
        // Send packet
        dgSocket.send(packet);
    }


    // Receive message moved to receive thread
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
       
        // Send name to server
        System.out.println("enter name: ");
        String name = sc.nextLine();
        client.sendMessage(name,0,"192.168.0.103", 3568 );

        int seqNumber = 0; // initialise seqNumber;
        while (true){
            String message = sc.nextLine(); 
            client.sendMessage(message, seqNumber, "192.168.0.103", 3568 );
            seqNumber ++;
        }

        //client.close();
    } catch (IOException ex) {
        ex.printStackTrace();}
}

    /** Converts an integer to a byte array
     * @param num
     */
    public static byte[] intToByte(int num) {
        return ByteBuffer.allocate(4).putInt(num).array();
    }

    /** Converts a byte array to an integer
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /** Merges the byte array for the integer sequence number with the byte array for the String message
     * @param seqNumber
     * @param message
     */
    public static byte[] mergeSeqAndMessage(byte[] seqNumber,byte[] hash, byte[] message){
        int length = seqNumber.length + hash.length + message.length;
        byte[] merged = new byte[length];
        System.arraycopy(seqNumber, 0, merged, 0, seqNumber.length);
        System.arraycopy(hash, 0, merged, seqNumber.length,  hash.length);
        System.arraycopy(message, 0, merged, seqNumber.length+hash.length, message.length);
        return merged;
    }

    /** Gets the Sequence Number From the merged byte array
     * @param merged
     * @return
     */
    public static int getSeqNumber(byte[] merged){
        byte[] seqNumber = new byte[4];
        System.arraycopy(merged, 0, seqNumber, 0, seqNumber.length);
        return byteArrayToInt(seqNumber);
    }
}
