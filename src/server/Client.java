package server;

import common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable
{
    ArrayList<Ship>  ships = new ArrayList<>();
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Socket socket;
    public Grid shipGrid = new Grid();
    public Grid shotGrid = new Grid();

    public static final String battleshipName = "Battleship";

    enum States
    {
        ships,
        shots,
        result,
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
                sendPrompt();
                line = bufferedReader.readLine();
                if(line == null)
                {
                    break;
                }
                System.out.println("server> received: " + line);
                Command command = Commands.parse(line);
                switch(command.operation)
                {
                    case Commands.shipLocation:
                    {
                        String[] coords = command.parameters;
                        String shipName = coords[0];
                        int x1 = Integer.parseInt(coords[1]);
                        int y1 = Integer.parseInt(coords[2]);
                        int x2 = Integer.parseInt(coords[3]);
                        int y2 = Integer.parseInt(coords[4]);
                        switch(shipName)
                        {
                            case battleshipName:
                                Ship ship = new Battleship(x1,y1,x2,y2);
                                ships.add(ship);
                                if (ships.size() > 0)
                                {
                                    clientState = States.shots;
                                }
                                Command shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(ship);
                                shipGrid.printGrid();
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;
                        }
                    }
                    break;
                    case Commands.shot:
                    {
                        String[] coords = command.parameters;
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        otherClient().shipGrid.printGrid();
                        int value = otherClient().shipGrid.getValue(x,y);
                        Command shotResult = Commands.create(Commands.shotResult, x, y, value);
                        printWriter.println(shotResult.toString());
                        printWriter.flush();
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

    public void sendPrompt() {
        String prompt = Commands.state;
        switch(clientState)
        {
            case ships:
                prompt += ":ships";
                break;
            case shots:
                prompt += ":shots";
                break;
        }
        printWriter.println(prompt);
        printWriter.flush();
    }

    private Client otherClient() {
        if (this == Server.playerA)
        {
            return Server.playerB;
        }
        else
        {
            return Server.playerA;
        }
    }
}
