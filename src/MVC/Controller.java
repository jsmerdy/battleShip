package MVC;

import common.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller extends View {

    public Controller(Grid grid) {
        super(grid);

        for (JButton button : buttonGrid) {
            button.addActionListener(new ShotButtonActionListener());

        }
    }

    class ShotButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JButton button = (JButton) e.getSource();
            int i = (int)button.getClientProperty("grid_location");
            int y = i / Grid.size;
            int x = i % Grid.size;
            System.out.printf("x: %d, y: %d\n", x, y);
            button.setEnabled(false);
            button.setBackground(Color.RED);
            button.setOpaque(true);
        }
    }


}
