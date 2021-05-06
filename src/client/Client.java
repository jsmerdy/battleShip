package client;

import MVC.Controller;
import MVC.ShipController;
import MVC.ShotController;
import MVC.View;
import common.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static server.Client.*;

public class Client {
    private PrintWriter socketWriter;
    private BufferedReader socketReader;
    private Socket socket;
    ArrayList<common.Ship> ships = new ArrayList<>();
    public LinkedList<Ship> shipList;
    private final Random randomGenerator = new Random();
    private ShotController shotController;
    private ShipController shipController;
    private String waitingMessage;
    private final String serverAddress;

    public static void main(String[] args) {

        String address = getServerAddress();

        Client client = new Client(address);
        client.go();

        System.out.println("Client closing");
        return;
    }

    private static String getServerAddress() {
        String address = (String)JOptionPane.showInputDialog(
                null,
                "Server Address",
                "Server Picker",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "127.0.0.1");
        return address;
    }

    public Client(String address)
    {
        serverAddress = address;
    }

    private void generateShipList() {
        shipList = new LinkedList<Ship>();
        Grid testGrid = new Grid();
        Ship ship = new Battleship();

        placeShipRandomly(ship,testGrid);
        shipList.add(ship);

        ship = new PatrolBoat();
        placeShipRandomly(ship,testGrid);
        shipList.add(ship);

        ship = new Carrier();
        placeShipRandomly(ship,testGrid);
        shipList.add(ship);

        ship = new Submarine();
        placeShipRandomly(ship,testGrid);
        shipList.add(ship);

        ship = new Destroyer();
        placeShipRandomly(ship,testGrid);
        shipList.add(ship);
    }

    private void placeShipRandomly(Ship ship, Grid testGrid) {
        while(true) {
            int x1 = randomGenerator.nextInt(Grid.size);
            int y1 = randomGenerator.nextInt(Grid.size);
            int x2 = 0;
            int y2 = 0;
            int direction = randomGenerator.nextInt(4);

            switch(direction) {
                case 0:
                    x2 = x1 + ship.length - 1;
                    y2 = y1;
                    break;
                case 1:
                    x2 = x1 - ship.length + 1;
                    y2 = y1;
                    break;
                case 2:
                    x2 = x1;
                    y2 = y1 + ship.length - 1;
                    break;
                case 3:
                    x2 = x1;
                    y2 = y1 - ship.length + 1;
                    break;
            }

            if(x2 >= Grid.size || x2 < 0) {
                continue;
            }
            if(y2 >= Grid.size || y2 < 0) {
                continue;
            }

            ship.init(x1,y1,x2,y2);

            boolean clear = true;
            for (int y = ship.y1; y <= ship.y2; y++) {
                for(int x = ship.x1; x <= ship.x2; x++) {
                    if(testGrid.getValue(x,y) == 1) {
                        clear = false;
                        break;
                    }
                }
            }

            if(!clear) {
                continue;
            }

            testGrid.addShip(ship);
            break;
        }
    }

    private void go() {
        generateShipList();

        try {
            System.out.printf("client> connecting to %s:5000...",serverAddress);
            socket = new Socket(InetAddress.getByName(serverAddress), 5000);
            System.out.println("client> success!");

            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Grid shipGrid = new Grid();
        Grid shotGrid = new Grid(-1);
        View shipView = new View();
        View shotView = new View();
        JFrame containerFrame = new JFrame();
        containerFrame.setSize(400,800);
        containerFrame.setLayout(new GridLayout(2,1));
        containerFrame.setResizable(false);
        containerFrame.add(shipView.containerPanel);
        containerFrame.add(shotView.containerPanel);
        containerFrame.pack();
        containerFrame.setVisible(true);

        shotController = new ShotController(shotGrid, shotView, socketWriter);
        shipController = new ShipController(shipGrid,shipView);
        shotController.draw();


        BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(System.in)));

        try {
            while(true) {
                String serverMessage = socketReader.readLine();
                System.out.println("client> received: "+ serverMessage);
                Command command = Commands.parse(serverMessage);
                switch(command.operation) {
                    case "state":
                        if(command.parameters.get(0).equals("ships")) {
                            /*
                            System.out.println("Enter ship & spawn location: ");
                            String line = bufferedReader.readLine();
                             */
                            Ship ship = shipList.removeFirst();
                            String shipLocation = String.format("%s,%d,%d,%d,%d", ship.getClass().getSimpleName(), ship.x1,ship.y1,ship.x2,ship.y2);
                            socketWriter.println("ship_location:" + shipLocation);
                            socketWriter.flush();
                        }
                        if(command.parameters.get(0).equals("shots")) {
                            waitingMessage = null;
                            shotController.shots();
                            containerFrame.setTitle("Take a shot");
                        }
                        if(command.parameters.get(0).equals("waiting"))
                        {
                            shotController.turnSwitch();
                            if (waitingMessage != null)
                            {
                                containerFrame.setTitle(waitingMessage);
                            }
                            else {
                                containerFrame.setTitle("slow down there speed racer");
                            }
                        }
                        break;

                    case Commands.shipConfirm:
                        String[] coords = command.parameters.toArray(new String[0]);
                        String shipName = coords[0];
                        int x1 = Integer.parseInt(coords[1]);
                        int y1 = Integer.parseInt(coords[2]);
                        int x2 = Integer.parseInt(coords[3]);
                        int y2 = Integer.parseInt(coords[4]);
                        Ship ship = null;
                        switch(shipName) {
                            case battleshipName:
                                ship = new Battleship().init(x1,y1,x2,y2);
                                break;
                            case patrolBoatName:
                                ship = new PatrolBoat().init(x1,y1,x2,y2);
                                break;
                            case submarineName:
                                ship = new Submarine().init(x1,y1,x2,y2);
                                break;
                            case carrierName:
                                ship = new Carrier().init(x1,y1,x2,y2);
                                break;
                            case destroyerName:
                                ship = new Destroyer().init(x1,y1,x2,y2);
                                break;
                        }
                        if (ship != null) {
                            ships.add(ship);
                            shipController.addShip(ship);
                            shipController.draw();
                        }
                        break;
                    case Commands.shotResult:
                        String[] shotCoords = command.parameters.toArray(new String[0]);
                        int x = Integer.parseInt(shotCoords[0]);
                        int y = Integer.parseInt(shotCoords[1]);
                        int v = Integer.parseInt(shotCoords[2]);
                        shotController.setValue(x,y,v);
                        shotController.draw();
                        if (shotCoords.length > 3)
                        {
                            waitingMessage = "You sunk the "+shotCoords[3];
                        }
                        if (shotCoords.length > 4)
                        {
                            waitingMessage = "You done did the thing";
                        }
                        break;
                    case Commands.shotReflection:
                        shotCoords = command.parameters.toArray(new String[0]);
                        x = Integer.parseInt(shotCoords[0]);
                        y = Integer.parseInt(shotCoords[1]);
                        shipController.setKnown(x,y);
                        shipController.draw();
                        if (shotCoords.length > 2)
                        {
                            waitingMessage = "You got your ship wrecked";
                        }
                        break;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
