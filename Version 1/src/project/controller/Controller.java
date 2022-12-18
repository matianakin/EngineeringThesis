package project.controller;
import project.model.*;
import project.view.*;

import java.util.Arrays;


/**
 * The type Controller.
 */
public class Controller {

    private Model model;

    private View view;


    /**
     * Instantiates a new Controller.
     */
    Controller() {
        this.model = new Model();
        this.view = new View();
        view.startingWindow();
        this.controller();
    }

    /**
     * Controller.
     */
    private void controller()
    {
        //model.GetXHTML("https://www.wp.pl");
        //model.readDictionary();

    }

}