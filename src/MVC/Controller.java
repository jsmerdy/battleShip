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
        public void actionPerformed(ActionEvent e) {




        }
    }


}
