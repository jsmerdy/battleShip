package common;

import common.Ship;

public class Battleship extends Ship {
    public Battleship(int x1, int y1, int x2, int y2)
    {
        super();
        Ship.health = 4;
        Ship.length = 4;
        Ship.x1 = x1;
        Ship.y1 = y1;
        Ship.x2 = x2;
        Ship.y2 = y2;
    }
}
