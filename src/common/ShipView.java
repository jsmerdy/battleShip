package common;

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
                }
                else
                {
                    buttonGrid.get(y*modelGrid.size + x).setBackground(Color.WHITE);
                }

            }
            System.out.println();
        }
    }

}
