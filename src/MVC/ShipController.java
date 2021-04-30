package MVC;

import common.Grid;
import common.Ship;

import java.awt.*;

public class ShipController extends Controller{
    public ShipController(Grid grid, View view) {
        super(grid, view);
    }

    public void draw()
    {
        for(int y = 0; y < Grid.size; y++) {
            for(int x = 0; x < Grid.size; x++)
            {
                if(modelGrid.getValue(x,y) == 1)
                {
                    view.buttonGrid.get(y* Grid.size + x).setBackground(Color.GRAY);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                }
                else
                {
                    view.buttonGrid.get(y* Grid.size + x).setBackground(Color.WHITE);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                }

            }
            System.out.println();
        }
    }

    public void addShip(Ship ship)
    {
        modelGrid.addShip(ship);
    }
}
