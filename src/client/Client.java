package client;

import common.Battleship;
import common.Commands;
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
    public static Grid myGrid = new Grid();
    public static Grid theirGrid = new Grid();

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
                String parts[] = serverMessage.split(":");
                switch(parts[0]) {
                    case "state":
                        if(parts[1].equals("ships")) {
                            System.out.println("Enter ship & spawn location: ");
                            String line = bufferedReader.readLine();
                            socketWriter.println("ship_location:" + line);
                            socketWriter.flush();
                        }
                        if(parts[1].equals("shots"))
                        {
                            System.out.println("Enter shot location: ");
                            String line = bufferedReader.readLine();
                            socketWriter.println("shot:" + line);
                            socketWriter.flush();
                        }
                        break;
                    case Commands.shipConfirm:
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
                                myGrid.addShip(ship);
                                myGrid.printGrid();
                                break;
                        }
                        break;
                        //todo: process shot result
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
