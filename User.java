/** User Class for maintaining all the information needed for each user object
 * @author Michael Scott, Michelle Lopes, Tuscany Botha
 */

import java.net.InetSocketAddress;

public class User {
        private String userName;
        //private int userNumber;
        private int messageCount;
        private InetSocketAddress IP;

    /** Constructor
     * @param userName
     * @param IP
     */
    public User(String userName, InetSocketAddress IP){
            this.userName = userName;
            //this.userNumber = userNumber;
            this.messageCount = 0;
            this.IP = IP;
        }

    /** Updates the users messagecount
     */
    public void upDateMessageCount(){
            messageCount ++;
        }

    /** Gets the users message count
     */
    public int getMessageCount(){
            return messageCount;
        }

    /** Gets the username
     */
    public String getUserName() {
            return userName;
        }

    /** Gets the IP address
     */
    public InetSocketAddress getInet(){
            return IP;
        }

}
