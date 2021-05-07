package common;

public class Grid {
    public static int size = 8;
    public int[][] grid;

    public Grid() {
        int value = 0;
        grid = new int[size][size];
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                grid[y][x] = value;
            }
        }
    }

    public Grid(int initialValue) {
        grid = new int[size][size];
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                grid[y][x] = initialValue;
            }
        }
    }

    public void printGrid() {
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                System.out.print(grid[y][x] + " ");
            }
            System.out.println();
        }
    }

    public int getValue(int x, int y) {
        return grid[y][x];
    }

    public void setValue(int x, int y, int value) {
        grid[y][x] = value;
    }

    public void addShip(Ship ship)
    {
        for (int y = ship.y1; y <= ship.y2; y++)
        {
            for(int x = ship.x1; x <= ship.x2; x++ )
            {
                grid[y][x] = 1;
            }
        }
    }
}