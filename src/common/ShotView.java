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
    }

    public void draw() {
        for (int y = 0; y < Grid.size; y++) {
            for (int x = 0; x < Grid.size; x++) {
                buttonGrid.get(y* Grid.size + x).setBackground(Color.WHITE);
                buttonGrid.get(y* Grid.size + x).setOpaque(true);
            }
        }
        System.out.println();
    }
}

