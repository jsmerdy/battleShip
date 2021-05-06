package MVC;

import common.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class ShotController extends Controller {

    PrintWriter socketWriter;
    public ShotController(Grid grid,View view, PrintWriter socketWriter) {
        super(grid,view);
        this.socketWriter = socketWriter;

        for (JButton button : view.buttonGrid) {
            button.addActionListener(new ShotButtonActionListener());

        }
    }
    public void draw() {
        for (int y = 0; y < Grid.size; y++) {
            for (int x = 0; x < Grid.size; x++) {
                JButton button = view.buttonGrid.get(y* Grid.size + x);
                int value = modelGrid.getValue(x,y);
                if(value == 1)
                {
                    button.setBackground(Color.RED);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                    view.buttonGrid.get(y* Grid.size + x).setBorder(BorderFactory.createLineBorder(Color.WHITE));

                }
                else if (value == 0)
                {
                    button.setBackground(Color.WHITE);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                    view.buttonGrid.get(y* Grid.size + x).setBorder(BorderFactory.createLineBorder(Color.WHITE));

                }
                else
                {
                    button.setBackground(Color.DARK_GRAY);
                    view.buttonGrid.get(y* Grid.size + x).setOpaque(true);
                    view.buttonGrid.get(y* Grid.size + x).setBorder(BorderFactory.createLineBorder(Color.WHITE));

                }
            }
        }
        System.out.println();
    }

    public void setValue(int x, int y, int v)
    {
        modelGrid.setValue(x,y,v);
    }

    public void turnSwitch()
    {
        view.setEnabled(false);
    }

    public void shots()
    {
        view.setEnabled(true);
    }

    class ShotButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            JButton button = (JButton) e.getSource();
            int i = (int)button.getClientProperty("grid_location");
            int y = i / Grid.size;
            int x = i % Grid.size;

            socketWriter.printf("shot:%d,%d\n",x,y);
            socketWriter.flush();

            button.setEnabled(false);
            button.putClientProperty("used", true);
        }
    }
}
