import java.net.InetSocketAddress;

public class User {
        private String userName;
        //private int userNumber;
        private int messageCount;
        private InetSocketAddress IP;


        public User(String userName, InetSocketAddress IP){
            this.userName = userName;
            //this.userNumber = userNumber;
            this.messageCount = 0;
            this.IP = IP;
        }
        public void upDateMessageCount(){
            messageCount ++;
        }

        public int getMessageCount(){
            return messageCount;
        }


        public String getUserName() {
            return userName;
        }

        public InetSocketAddress getInet(){
            return IP;
        }

}
