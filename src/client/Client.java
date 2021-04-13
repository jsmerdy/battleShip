package client;

import common.Battleship;
import common.Grid;
import common.Ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private static PrintWriter socketWriter;
    private static BufferedReader socketReader;
    private static Socket socket;
    ArrayList<common.Ship> ships = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.println("client> connecting to 127.0.0.1:5000...");
            socket = new Socket("127.0.0.1", 5000);
            System.out.println("client> success!");

            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Client().go();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client closing");
        return;
    }

    private void go() {
        Grid grid = new Grid();
        BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(System.in)));

        try {
            while(true) {
                String serverMessage = socketReader.readLine();
                System.out.println("client> received: "+ serverMessage);
                String[] parts = serverMessage.split(":");
                switch(parts[0]) {
                    case "state":
                        if(parts[1].equals("ships")) {
                            System.out.println("Enter ship & spawn location: ");
                            System.out.println("Ex: ship,x1,y1,x2,y2");
                            String line = bufferedReader.readLine();
                            socketWriter.println("ship_location:" + line);
                            socketWriter.flush();
                        }
                        break;
                    case "ship_confirm":
                        String[] coords = parts[1].split(",");
                        String shipName = coords[0];
                        int x1 = Integer.parseInt(coords[1]);
                        int y1 = Integer.parseInt(coords[2]);
                        int x2 = Integer.parseInt(coords[3]);
                        int y2 = Integer.parseInt(coords[4]);
                        switch(shipName)
                        {
                            case "battleship":
                                Ship ship = new Battleship(x1,y1,x2,y2);
                                ships.add(ship);
                                break;
                        }
                    /*case "print_grid":
                        {
                            //socketWriter.println(("print_grid:" + parts[1]));
                            System.out.println(parts[1]);
                        }
                        break;*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
