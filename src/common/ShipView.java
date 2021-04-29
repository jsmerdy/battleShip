package common;

import MVC.View;

import java.awt.*;

public class ShipView extends View
{
    public ShipView(Grid grid)
    {
        super(grid);

    }

    public void draw()
    {
        for(int y = 0; y < modelGrid.size; y++) {
            for(int x = 0; x < modelGrid.size; x++)
            {
                if(modelGrid.getValue(x,y) == 1)
                {
                    buttonGrid.get(y*modelGrid.size + x).setBackground(Color.GRAY);
                    buttonGrid.get(y*modelGrid.size + x).setOpaque(true);
                }
                else
                {
                    buttonGrid.get(y*modelGrid.size + x).setBackground(Color.WHITE);
                    buttonGrid.get(y*modelGrid.size + x).setOpaque(true);
                }

            }
            System.out.println();
        }
    }

}
