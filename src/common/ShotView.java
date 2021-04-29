package common;

import MVC.Controller;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ShotView extends View
{
    public ShotView(Grid grid)
    {
        super(grid);
        Controller controller = new Controller(grid);

    }

    public void draw() {
        for (int y = 0; y < modelGrid.size; y++) {
            for (int x = 0; x < modelGrid.size; x++) {
                buttonGrid.get(y*modelGrid.size + x).setBackground(Color.WHITE);
                buttonGrid.get(y*modelGrid.size + x).setOpaque(true);
            }
        }
        System.out.println();
    }
}

