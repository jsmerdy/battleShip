package common;

public class Ship {
    public int health = 0;
    public int length = 0;
    public int x1 = 0;
    public int x2 = 0;
    public int y1 = 0;
    public int y2 = 0;

    public Ship()
    {
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public Ship init(int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        normalize();
        return this;
    }

    private void normalize()
    {
        if (x2 < x1)
        {
            int swp = x1;
            x1 = x2;
            x2 = swp;
        }
        if (y2 < y1)
        {
            int swp = y1;
            y1 = y2;
            y2 = swp;
        }

    }

    public boolean thisYou(int x, int y)
    {
        System.out.printf("This you %d %d (%d,%d) (%d,%d)\n",x,y,x1,y1,x2,y2);
        if ((x >= x1 && x <= x2) && (y >= y1 && y <= y2))
        {
            System.out.printf("line 51\n");
            health--;
            return true;
        }

        System.out.printf("line 56\n");
        return false;
    }
}
