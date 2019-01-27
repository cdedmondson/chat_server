/*

       **************Instructions for running program*******************

       First run "ServerMain", then open up two or more terminals
       and telnet localhost port 8818 each terminal will act as a client

 */

package run_chat;

public class ServerMain {

    public static void main(String[] args) {

        int port = 8818;
        Server server = new Server(port);
        server.start();

    }

}
