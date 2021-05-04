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
        result,
    }

    public States clientState;

    public int shotsTaken = 0;
    public boolean thatGuy = false;

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
                if(line == null) { break; }

                System.out.println("server> received: " + line);
                Command command = Commands.parse(line);
                switch(command.operation) {
                    case Commands.shipLocation: {
                        String[] coords = command.parameters;
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
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;

                            case patrolBoatName:
                                Ship patrolBoat = new PatrolBoat().init(x1,y1,x2,y2);
                                ships.add(patrolBoat);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(patrolBoat);
                                shipGrid.printGrid();
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;

                            case carrierName:
                                Ship carrier = new Carrier().init(x1,y1,x2,y2);
                                ships.add(carrier);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(carrier);
                                shipGrid.printGrid();
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;
                            case submarineName:
                                Ship submarine = new Submarine().init(x1,y1,x2,y2);
                                ships.add(submarine);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(submarine);
                                shipGrid.printGrid();
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;
                            case destroyerName:
                                Ship destroyer = new Destroyer().init(x1,y1,x2,y2);
                                ships.add(destroyer);
                                shipConfirm = Commands.create(Commands.shipConfirm, coords);
                                shipGrid.addShip(destroyer);
                                shipGrid.printGrid();
                                printWriter.println(shipConfirm.toString());
                                printWriter.flush();
                                break;

                        }
                        if (ships.size() >= 5)
                        {
                            if (otherClient()!=null && otherClient().clientState == States.waiting)
                            {
                                int turnSelector = randomGenerator.nextInt(2);
                                if (turnSelector == 0)
                                {
                                    otherClient().thatGuy = true;
                                    thatGuy = false;
                                    otherClient().clientState = States.shots;
                                    clientState = States.waiting;

                                }
                                else
                                {
                                    otherClient().clientState = States.waiting;
                                    clientState = States.shots;
                                    thatGuy = true;
                                    otherClient().thatGuy = false;
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
                        String[] coords = command.parameters;
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        otherClient().shipGrid.printGrid();
                        int value = otherClient().shipGrid.getValue(x,y);
                        Ship ship = null;
                        if(value == 1)
                        {
                            ship = otherClient().shipHit(x,y);
                        }
                        Command shotResult = null;
                        if (ship != null && ship.health == 0)
                        {
                            shotResult = Commands.create(Commands.shotResult, x, y, value, ship.getClass().getSimpleName());
                        }
                        else
                        {
                            shotResult = Commands.create(Commands.shotResult, x, y, value);
                        }
                        printWriter.println(shotResult.toString());
                        printWriter.flush();
                        shotsTaken += 1;

                        otherClient().clientState = States.shots;
                        otherClient().sendPrompt();
                        clientState = States.waiting;
                        sendPrompt();


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
        printWriter.println(prompt);
        printWriter.flush();
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
