package common;

import common.Ship;

public class PatrolBoat extends Ship
{
    public PatrolBoat(int x1, int y1, int x2, int y2)
    {
        this.health = 2;
        this.length = 2;
        this.x1 = 0;
        this.x2 = 0;
        this.y1 = 0;
        this.y2 = 0;
    }

    public PatrolBoat() {
        super();
        this.health = 2;
        this.length = 2;
    }
}
