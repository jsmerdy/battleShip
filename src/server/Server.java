package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static server.Client playerA;
    static server.Client playerB;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true) {
                System.out.println("server> waiting for client to connect..");
                Socket client = serverSocket.accept();

                if(playerA == null)
                {
                    playerA = new server.Client();
                    playerA.socket = client;
                    playerA.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    playerA.printWriter = new PrintWriter(client.getOutputStream());
                    new Thread(playerA).start();
                }
                else if (playerB == null) {
                    playerB = new server.Client();
                    playerB.socket = client;
                    playerB.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    playerB.printWriter = new PrintWriter(client.getOutputStream());
                    new Thread(playerB).start();
                }
                else
                {
                    PrintWriter printWriter = new PrintWriter(client.getOutputStream());
                    printWriter.println("message;Game is full!");
                    printWriter.println("disconnect;");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
