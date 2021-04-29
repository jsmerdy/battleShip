package MVC;

import common.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View {
    protected Grid modelGrid = null;
    JFrame frame = new JFrame();
    JPanel buttonPanel = new JPanel();
    JPanel containerPanel = new JPanel();
    public ArrayList<JButton> buttonGrid = new ArrayList<>();
    View()
    {
        frame.setSize(400,400);
        buttonPanel.setLayout(new GridLayout(8,8));

        for (int i = 0; i < 64; i++) {
            buttonGrid.add(new JButton(String.valueOf(i)));
            buttonPanel.add(buttonGrid.get(i));
        }

        buttonPanel.setPreferredSize(new Dimension(400,400));
        containerPanel.add(buttonPanel);

        frame.add(containerPanel);
        frame.pack();
        frame.setVisible(true);
    }

    protected void draw()
    {

    }

    public View(Grid grid) {
        this();
        modelGrid = grid;
    }



    public static void main(String[] args) {
        new View();
    }


}
