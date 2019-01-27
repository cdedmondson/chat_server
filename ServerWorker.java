package run_chat;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String name;
    private OutputStream outputStream;

    public ServerWorker(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        String options = "Type 'register' to get started or 'quit' to end chat\n";
        outputStream.write(options.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {

            String[] string = line.split(" ");
            String cmd = Arrays.toString(string)
                    .replace(",", "")  //remove commas
                    .replace("[", "")  //remove right bracket
                    .replace("]", "")  //remove left bracket
                    .trim();
            if (cmd.length() > 0) {

                if ("quit".equalsIgnoreCase(cmd)) {

                    break;

                } else if ("register".equalsIgnoreCase(cmd)) {
                    String msg = "Enter your alias: ";
                    outputStream.write(msg.getBytes());
                    BufferedReader temp = new BufferedReader(new InputStreamReader(inputStream));
                    this.name = temp.readLine();

                    List<ServerWorker> workerList = server.getWorkerList();

                    // send current user's status
                    for (ServerWorker worker : workerList) {
                        String onlineMessage = "Online " + this.name + "\n";
                        worker.send(onlineMessage);
                    }
                } else {
                    Date date = new Date();
                    List<ServerWorker> workerList = server.getWorkerList();
                    for (ServerWorker worker : workerList) {
                        worker.send(this.name + ": "+ cmd + " " + date.toString() + "\n");
                    }
                }
            }

        }

        clientSocket.close();
    }

    private void send(String msg) throws IOException {

        outputStream.write(msg.getBytes());

    }

}


