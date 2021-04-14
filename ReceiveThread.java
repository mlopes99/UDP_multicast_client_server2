/** Receive Thread class that receives messages sent from the server
 * @author Michael Scott, Michelle Lopes, Tuscany Botha
 */

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

class ReceiveThread implements Runnable {
    private MulticastSocket socket;
    // private InetAddress group;
    // private int port;

    /** Constructor
     * @param socket
     */
    ReceiveThread(MulticastSocket socket){
    // ReadThread(MulticastSocket socket, InetAddress group, int port){
        this.socket = socket;
        //this.group = group;
        //this.port = port;
    }

    /** Threaded run Method
     */
    public void run() {
        //System.setProperty("java.net.preferIPv4Stack", "true");
         while (true) {
            //byte[] buffer = new byte[1024*4];
            //DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            //String message;
            //try {
            //    socket.receive(packet);
            //    message = new String(buffer);
            //    System.out.println(message);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}


            try{
                byte[] message = new byte[1024*4];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                
                // receive the packet
                socket.receive(packet);
                System.out.println(new String(message, 0, packet.getLength()));
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /** Gets the Sequence Number From the merged byte array
     * @param merged
     */
    public static int getSeqNumber(byte[] merged){
        byte[] seqNumber = new byte[4];
        System.arraycopy(merged, 0, seqNumber, 0, seqNumber.length);
        return Client.byteArrayToInt(seqNumber);
    }

    /** Gets the message From the merged byte array
     * @param merged
     */
    public static String getMessage(byte[] merged) {
        byte[] message = new byte[merged.length - 4];
        System.arraycopy(merged, 4, message, 0, message.length);
        return new String(message);
    }
}
