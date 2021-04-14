import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
import java.net.MulticastSocket;



public class GUI extends JFrame{
    private JPanel mainPanel;
    private JPanel sendPanel;
    private JPanel namePanel;
    private JTextField nameField;
    private JButton joinChatButton;
    private JTextArea outputArea;
    private JTextField typeMessageTextField;
    private JButton sendButton;

    private static Client client;

    int seqNumber;

    /**
    * Creates instance of GUI Object, adds actionListener to buttons
    * Specifies what needs to be done when buttons pushed
    * @param title 
    **/
    private GUI(String title){
        super(title);
        init();

        joinChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // grab the text from name text field
                String name = nameField.getText();
                try {
                    client.sendMessage(name, 0,"192.168.1.56", 3568 );
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }

                mainPanel.remove(joinChatButton);
                mainPanel.remove(namePanel);
                mainPanel.add(sendPanel);

                seqNumber = 0;
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String message;
                    message =typeMessageTextField.getText();
                    typeMessageTextField.setText("");
                    client.sendMessage(message, seqNumber ,"192.168.1.56", 3568 );
                    seqNumber++;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        PrintStream printStream = new PrintStream(new CustomOutputStream(outputArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }


    /**
    *Initialises GUI
    **/
    
    public void init(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,2));
        mainPanel.setPreferredSize(new Dimension(500, 600));

        sendPanel = new JPanel();
        sendPanel.setBorder(BorderFactory.createTitledBorder("Type Message below:"));

        namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createTitledBorder("Please Enter Name:"));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        nameField = new JTextField();
        nameField.setText("");
        nameField.setColumns(20);

        JLabel header = new JLabel();
        header.setBackground(new Color(-5437184));
        header.setEnabled(true);
        header.setForeground(new Color(-5437184));
        header.setText("Our Chat Application:");

        outputArea = new JTextArea();
        outputArea.setText("");
        outputArea.setColumns(200);
        outputArea.setRows(10);

        joinChatButton = new JButton();
        joinChatButton.setText("Join Chat");

        typeMessageTextField = new JTextField();
        typeMessageTextField.setText("");
        typeMessageTextField.setColumns(50);

        sendButton = new JButton();
        sendButton.setText("Send");

        sendPanel.add(typeMessageTextField);
        sendPanel.add(sendButton);

        namePanel.add(nameField);
        namePanel.add(joinChatButton);

        mainPanel.add(namePanel);
        mainPanel.add(outputArea);
    }

    public static void main (String[] args){
        JFrame frame = new GUI("Chat App");
        frame.setVisible(true);
        try {
            //final String ip = args[0];

            System.setProperty("java.net.preferIPv4Stack", "true");
            client = new Client("225.6.7.8", 3456);




            // Create socket
            MulticastSocket mcsocket = new MulticastSocket(3456);
            mcsocket.joinGroup(InetAddress.getByName("225.6.7.8"));

            // Create and run receive thread
            Thread receiveThread = new Thread(new ReceiveThread(mcsocket));
            receiveThread.start();



            //client.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    //class from https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
    private class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char)b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }


    }


}
