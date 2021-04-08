import java.util.Arrays;

public class Grid {
    public static int size = 8;
    public int[][] grid;

    Grid() {
        grid = new int[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid[i][j] = 1;
            }
        }
    }

    public void printGrid() {
        System.out.println(Arrays.deepToString(grid));
    }


}