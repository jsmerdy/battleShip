package MVC;

import common.Grid;

public class Controller {
    protected Grid modelGrid;
    protected View view;

    public Controller(Grid grid, View view)
    {
        this.modelGrid = grid;
        this.view = view;

    }
}
