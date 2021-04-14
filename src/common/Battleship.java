package common;

import common.Ship;

public class Battleship extends Ship {
    public Battleship(int x1, int y1, int x2, int y2)
    {
        super();
        this.health = 4;
        this.length = 4;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
