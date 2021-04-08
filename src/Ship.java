public class Ship
{
    static int health = 0;
    static int x1 = 0;
    static int x2 = 0;
    static int y1 = 0;
    static int y2 = 0;

   public static void spawnShip(int x, int y)
   {
       x1 = x;
       y1 = y;
       System.out.printf("Ship X: %d Ship Y: %d", x, y);
   }
}
