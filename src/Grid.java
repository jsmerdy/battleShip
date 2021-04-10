import java.util.Arrays;

public class Grid {
    public static int size = 8;
    public int[][] grid;

    Grid() {
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
}