package client;

import common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Client {
    private PrintWriter socketWriter;
    private BufferedReader socketReader;
    private Socket socket;
    ArrayList<common.Ship> ships = new ArrayList<>();
    public Grid shipGrid = new Grid();
    public Grid shotGrid = new Grid();

    public static void main(String[] args) {

        Client client = new Client();
        client.go();

        System.out.println("Client closing");
        return;
    }

    private LinkedList<Ship> randoShips() {
        LinkedList<Ship> rando = new LinkedList<>();
        Grid testGrid = new Grid();
        Ship ship = new Battleship();
        randoShip(ship,testGrid);
        rando.add(ship);
        ship = new Carrier();
        randoShip(ship,testGrid);
        rando.add(ship);
        ship = new PatrolBoat();
        randoShip(ship,testGrid);
        rando.add(ship);
        ship = new Submarine();
        randoShip(ship,testGrid);
        rando.add(ship);
        ship = new Destroyer();
        randoShip(ship,testGrid);
        rando.add(ship);
        return rando;
    }

    private void randoShip(Ship ship, Grid grid) {
        while(true) {
            int x1 = (int) (Math.random() * Grid.size);
            int y1 = (int) (Math.random() * Grid.size);
            int x2 = 0;
            int y2 = 0;
            int direction = (int) (Math.random() * 4);
            switch(direction)
            {
                case 0:
                    x2 = x1 + ship.length;
                    y2 = y1;
                    break;
                case 1:
                    x2 = x1 - ship.length;
                    y2 = y1;
                    break;
                case 2:
                    x2 = x1;
                    y2 = y1 + ship.length;
                    break;
                case 3:
                    x2 = x1;
                    y2 = y1 - ship.length;
                    break;

            }
            if(x2 >= Grid.size || x2 < 0)
            {
                continue;
            }
            if(y2 >= Grid.size || y2 < 0)
            {
                continue;
            }
            boolean clear = true;
            for(int x = x1; x <= x2; x++)
            {
                for (int y = y1; y <= y2; y++)
                {
                    if(grid.getValue(x,y) == 1)
                    {
                        clear = false;
                        break;
                    }
                }
            }
            if(!clear)
            {
                continue;
            }
            ship.x1 = x1;
            ship.x2 = x2;
            ship.y1 = y1;
            ship.y2 = y2;
            grid.addShip(ship);
            break;
        }

    }

    private void go() {
        Grid grid = new Grid();

        LinkedList<Ship> rando = randoShips();

        try {
            System.out.println("client> connecting to 127.0.0.1:5000...");
            socket = new Socket("127.0.0.1", 5000);
            System.out.println("client> success!");

            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(System.in)));

        try {
            while(true) {
                String serverMessage = socketReader.readLine();
                System.out.println("client> received: "+ serverMessage);
                String[] parts = serverMessage.split(":");
                switch(parts[0]) {
                    case "state":
                        if(parts[1].equals("ships")) {
                            /*
                            System.out.println("Enter ship & spawn location: ");
                            String line = bufferedReader.readLine();
                             */
                            Ship ship = rando.removeFirst();
                            String line = String.format("%s,%d,%d,%d,%d", ship.getClass().getSimpleName(), ship.x1,ship.y1,ship.x2,ship.y2);
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
                            //todo: fix this shit
                            case "battleship":
                                Ship ship = new Battleship(x1,y1,x2,y2);
                                ships.add(ship);
                                shipGrid.addShip(ship);
                                shipGrid.printGrid();
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
