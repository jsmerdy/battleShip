package common;

import java.util.Arrays;

public class Grid {
    public static int size = 8;
    public int[][] grid;

    public Grid() {
        int value = 0;
        grid = new int[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid[i][j] = value;
            }
        }
    }

    public void printGrid() {
        System.out.println(Arrays.deepToString(grid));
    }

    public int getValue(int x, int y) {
        return grid[x][y];
    }

    public void setValue(int x, int y, int value) {
        grid[x][y] = value;
    }
    public void addShip(Ship ship)
    {
        for(int x = ship.x1; x <= ship.x2; x++)
        {
            for (int y = ship.y1; y <= ship.y2; y++)
            {
                grid[x][y] = 1;
            }
        }
        //todo: make sure no ship already exist
    }
}