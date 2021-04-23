package common;

import java.util.Arrays;

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



    //The issue with ship placement is that sometime x1 == x2, or y1 == y2 ex: (5,5),(5,1)
    //Another issue is that x1 is sometime greater then x2 and vice versa
    //Possible fix is detect which way the placement loop needs to run
    public void addShip(Ship ship)
    {
        for (int y = ship.y1; y <= ship.y2; y++)
        {
            for(int x = ship.x1; x <= ship.x2; x++ )
            {
                grid[y][x] = 1;
            }
        }

        //todo: make sure no ship already exist
    }
}