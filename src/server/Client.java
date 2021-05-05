package server;

import common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Client implements Runnable {
    ArrayList<Ship> ships = new ArrayList<>();
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Socket socket;
    public Grid shipGrid = new Grid();
    public Grid shotGrid = new Grid(-1);
    public static final String battleshipName = "Battleship";
    public static final String patrolBoatName = "PatrolBoat";
    public static final String carrierName = "Carrier";
    public static final String destroyerName = "Destroyer";
    public static final String submarineName = "Submarine";
    private final Random randomGenerator = new Random();

    enum States {
        ships,
        shots,
        waiting,
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
                sendPrompt();
                line = bufferedReader.readLine();
                if(line == null) { break; }

                System.out.println("server> received: " + line);
                Command command = Commands.parse(line);
                switch(command.operation) {
                    case Commands.shipLocation: {
                        String[] coords = command.parameters.toArray(new String[0]);
                        String shipName = coords[0];
                        int x1 = Integer.parseInt(coords[1]);
                        int y1 = Integer.parseInt(coords[2]);
                        int x2 = Integer.parseInt(coords[3]);
                        int y2 = Integer.parseInt(coords[4]);
                        switch(shipName) {
                            case battleshipName:
                                Ship battleship = new Battleship().init(x1,y1,x2,y2);
                                ships.add(battleship);
                                Command shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(battleship);
                                shipGrid.printGrid();
                                sendCommand(shipConfirm.toString());
                                break;

                            case patrolBoatName:
                                Ship patrolBoat = new PatrolBoat().init(x1,y1,x2,y2);
                                ships.add(patrolBoat);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(patrolBoat);
                                shipGrid.printGrid();
                                sendCommand(shipConfirm.toString());
                                break;

                            case carrierName:
                                Ship carrier = new Carrier().init(x1,y1,x2,y2);
                                ships.add(carrier);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(carrier);
                                shipGrid.printGrid();
                                sendCommand(shipConfirm.toString());
                                break;
                            case submarineName:
                                Ship submarine = new Submarine().init(x1,y1,x2,y2);
                                ships.add(submarine);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(submarine);
                                shipGrid.printGrid();
                                sendCommand(shipConfirm.toString());
                                break;
                            case destroyerName:
                                Ship destroyer = new Destroyer().init(x1,y1,x2,y2);
                                ships.add(destroyer);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(destroyer);
                                shipGrid.printGrid();
                                sendCommand(shipConfirm.toString());
                                break;

                        }
                        if (ships.size() >= 5)
                        {
                            if (otherClient()!=null && otherClient().clientState == States.waiting)
                            {
                                int turnSelector = randomGenerator.nextInt(2);
                                if (turnSelector == 0)
                                {
                                    otherClient().clientState = States.shots;
                                    clientState = States.waiting;

                                }
                                else
                                {
                                    otherClient().clientState = States.waiting;
                                    clientState = States.shots;
                                }
                            }
                            else {
                                clientState = States.waiting;
                            }

                            if(otherClient() != null) {
                                otherClient().sendPrompt();
                            }
                            sendPrompt();
                        }

                    }
                    break;

                    case Commands.shot:
                        {
                        String[] coords = command.parameters.toArray(new String[0]);
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        otherClient().shipGrid.printGrid();
                        int value = otherClient().shipGrid.getValue(x,y);
                        Ship ship = null;
                        if(value == 1)
                        {
                            ship = otherClient().shipHit(x,y);
                        }
                        Command shotResult = Commands.create(Commands.shotResult, x, y, value);

                        if(ship != null && ship.health == 0)
                        {
                            shotResult.parameters.add(ship.getClass().getSimpleName());
                            otherClient().ships.remove(ship);
                            if(otherClient().ships.isEmpty())
                            {
                                shotResult.parameters.add("winner is you");
                            }
                        }
                        sendCommand(shotResult.toString());

                        Command shotReflection = Commands.create(Commands.shotReflection,x,y);
                        if(otherClient().ships.isEmpty())
                        {
                            shotReflection.parameters.add("winner is not you");
                        }

                        otherClient().sendCommand(shotReflection.toString());

                        if(otherClient().ships.isEmpty()) {
                            otherClient().clientState = States.waiting;
                        }
                        else
                        {
                            otherClient().clientState = States.shots;
                        }
                        clientState = States.waiting;
                        otherClient().sendPrompt();
                        sendPrompt();
                    }
                    break;

                }
            } catch (Exception e) {
                printWriter.println(-1);
                e.printStackTrace();
                break;
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

    public void sendCommand(String s) {
        printWriter.println(s);
        printWriter.flush();
    }

    private Ship shipHit(int x, int y)
    {
        for(Ship ship: ships)
        {
            if(ship.thisYou(x,y))
            {
                return ship;
            }
        }
        return null;
    }

    public void sendPrompt() {
        String prompt = Commands.state;
        switch(clientState) {
            case ships:
                prompt += ":ships";
                break;
            case shots:
                prompt += ":shots";
                break;
            case waiting:
                prompt += ":waiting";
                break;
        }
        sendCommand(prompt);
    }

    private Client otherClient() {
        if (this == Server.playerA) {
            return Server.playerB;
        }
        else {
            return Server.playerA;
        }
    }
}
