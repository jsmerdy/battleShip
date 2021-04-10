package server;

import common.Battleship;
import common.Ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Client implements Runnable
{
    ArrayList<Ship> ships = new ArrayList<>();
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Socket socket;

    enum States
    {
        ships,
        shots
    }

    public States clientState;

    @Override
    public void run() {
        String line;
        String clientConnected = String.format("server> connected to client Socket[addr=/127.0.0.1,port=%d,localport=5000]", socket.getPort());
        System.out.println(clientConnected);
        clientState = States.ships;
        while(true) {
            try {
                //todo: switch on client state
                printWriter.println("state:ships");
                printWriter.flush();
                line = bufferedReader.readLine();
                if(line == null)
                {
                    break;
                }
                System.out.println("server> received: " + line);
                line = line.trim();
                String[] inputArray = line.split(":");
                switch(inputArray[0])
                {
                    case "ship_location":
                    {
                        String[] coords = inputArray[1].split(",");
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
                                printWriter.println("ship_confirm:" + String.join(",",coords));
                                printWriter.flush();
                                break;
                        }
                        //todo: track number of ships
                    }
                    break;
                    case "shot":
                    {
                        String[] coords = inputArray[1].split(",");
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        int value = Server.grid.getValue(x,y);
                        //todo: send grid state reply
                    }
                    break;

                }
            } catch (Exception e) {
                printWriter.println(-1);
                e.printStackTrace();
            } finally {
                printWriter.flush();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
