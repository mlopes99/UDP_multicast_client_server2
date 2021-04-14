/** Server Class That Receives messages from  clients and sends the message out to all the clients in the same multicast Group
 * Manages and maintains a list of ip Addresses and port numbers with the corresponding user name
 * @author Michael Scott, Michelle Lopes, Tuscany Botha
 * https://riptutorial.com/java/example/14572/multicasting
 */

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.net.SocketException;


public class Server {

    private static DatagramSocket serverSocket;

    private String ip;

    private int port;

    public ArrayList<InetSocketAddress> previousConnections;
    public ArrayList<User> users;

    /** Constructor
     * @param ip
     * @param port
     * @throws SocketException
     * @throws IOException
     */
    public Server(String ip, int port) throws SocketException, IOException{

        this.ip = ip;
        this.port = port;
        // socket used to send
        serverSocket = new DatagramSocket(port );
        previousConnections = new ArrayList<InetSocketAddress>();
        users = new ArrayList<User>();
    }


    /** Send using multicast SocketException
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException{
        // make datagram packet
        byte[] message = (msg).getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length,
        InetAddress.getByName("225.6.7.8"), 3456);
        // send packet
        serverSocket.send(packet);
    }

    /** Closes Socket
     */
    public void close(){
        serverSocket.close();
    }

    /** Receives messages from client and manages sequence numbers from the client and for those in the user object.
     * Tells the user if a message is missing
     * @throws IOException
     */
    public void receiveMessage() throws IOException{

        while(true){
            // make datagram packet to recieve
            byte[] message = new byte[256];
            DatagramPacket packet = new DatagramPacket(message, message.length);

            // recieve the packet
            serverSocket.receive(packet);
            int receivedSeqNumber = getSeqNumber(message); // Extracts Sequence Number
            int receivedHashNumber = getHashNumber(message); // Extract received hash number

            String receivedData = getMessage(message); // Extracts Message
            receivedData = receivedData.trim();

            if (receivedHashNumber != receivedData.hashCode()){   // Checks whether recieved has is equal to the hash of the received message
                System.out.println("Your Message was corrupted. Please resend");
                continue;
            }
            //String receivedData = new String(packet.getData());
            //receivedData = receivedData.trim();

            //get sender information
            int clientPort = packet.getPort();
            InetAddress clientAdd = packet.getAddress();
            InetSocketAddress clientSocAdd = new InetSocketAddress(clientAdd, clientPort);
            System.out.println(receivedData);
            System.out.println("Received Sequence Number From Client: "+ receivedSeqNumber);




            //if client not connnected before initialise user, print new user to server and send new user name to all clients
            if (!previousConnections.contains(clientSocAdd)){
                System.out.println("new user");
                send("new user " + receivedData + " has joined chat");
                previousConnections.add(clientSocAdd);
                //TODO: add new Client to User array
                previousConnections.add(clientSocAdd);

                User user = new User(receivedData, clientSocAdd);
                users.add(user);
            }
            //TODO: else get user name from USER array? then send message with user name
            else{

                for (int i = 0; i <= users.size() - 1; i ++){
                    if (clientSocAdd.equals(users.get(i).getInet())){
                        String name = users.get(i).getUserName();
                        int userMessageCount = users.get(i).getMessageCount(); // Gets message count from user Object
                        if (receivedSeqNumber != userMessageCount){   // Tests to see whether the received sequence number matches that of the user object
                            System.out.println("Please resend message: " + userMessageCount); // Asks to resend the missing message
                            users.get(i).upDateMessageCount(); // Updates User Object message count so program can continue as usual
                        }

                        System.out.println("Sequence Number in User Object: " + userMessageCount);
                        send(name +": "+ receivedData);
                        users.get(i).upDateMessageCount();
                    }
                }
            }




            //send("reply");
        }
    }

    public static void main(String[] args) {
        try {
            // final String ip = args[0];
            // final int port = Integer.parseInt(args[1]);
            // Server server = new Server(ip, port);
            System.setProperty("java.net.preferIPv4Stack", "true");

            //TODO: Change to no longer be localhost
            Server server = new Server("192.168.0.103", 3568);

            server.receiveMessage();



            //server.send();
            //server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
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
        byte[] message = new byte[merged.length - 8];
        System.arraycopy(merged, 8, message, 0, message.length);
        return new String(message);
    }

    public static int getHashNumber(byte[] merged){
        byte[] hash = new byte[4];
        System.arraycopy(merged, 4, hash, 0, hash.length);
        return Client.byteArrayToInt(hash);
    }
}
