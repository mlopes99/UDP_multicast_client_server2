//https://riptutorial.com/java/example/14572/multicasting

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

    public Server(String ip, int port) throws SocketException, IOException{

        this.ip = ip;
        this.port = port;
        // socket used to send
        serverSocket = new DatagramSocket(port );
        previousConnections = new ArrayList<InetSocketAddress>();
        users = new ArrayList<User>();
    }


    //send using multicast SocketException
    public void send(String msg) throws IOException{
        // make datagram packet
        byte[] message = (msg).getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length,
        InetAddress.getByName("225.6.7.8"), 3456);
        // send packet
        serverSocket.send(packet);
    }

    public void close(){
        serverSocket.close();
    }


    public void receiveMessage() throws IOException{

        while(true){
            // make datagram packet to recieve
            byte[] message = new byte[256];
            DatagramPacket packet = new DatagramPacket(message, message.length);

            // recieve the packet
            serverSocket.receive(packet);
            String receivedData = new String(packet.getData());
            receivedData = receivedData.trim();

            //get sender information
            int clientPort = packet.getPort();
            InetAddress clientAdd = packet.getAddress();
            InetSocketAddress clientSocAdd = new InetSocketAddress(clientAdd, clientPort);
            System.out.println(receivedData);





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
                        send(name +": "+ receivedData);
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
            Server server = new Server("192.168.43.200", 3568);

            server.receiveMessage();



            //server.send();
            //server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
