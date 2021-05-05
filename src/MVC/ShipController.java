package MVC;

import common.Grid;
import common.Ship;

import javax.swing.*;
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
                int value = modelGrid.getValue(x,y);
                if((value & 1) == 1)
                {
                    view.buttonGrid.get(y* Grid.size + x).setBackground( (value & 2) == 2 ? Color.RED : Color.LIGHT_GRAY);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                    view.buttonGrid.get(y* Grid.size + x).setBorder(BorderFactory.createLineBorder(Color.YELLOW));

                }
                else
                {
                    view.buttonGrid.get(y* Grid.size + x).setBackground((value & 2) == 2 ? Color.WHITE : new Color(0,0,100));
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                    view.buttonGrid.get(y* Grid.size + x).setBorder(BorderFactory.createLineBorder(Color.YELLOW));

                }

            }
            System.out.println();
        }
    }
    public void addShip(Ship ship)
    {
        modelGrid.addShip(ship);
    }
    public void setKnown(int x, int y) {
        int v = modelGrid.getValue(x,y);
        v |= 2;
        modelGrid.setValue(x,y,v);
    }
}
