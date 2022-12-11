package project.controller;
import project.model.*;
import project.view.*;


public class Controller {

    private Model model;

    private View view;


    Controller() {
        this.model = new Model();
        this.view = new View();
        view.startingWindow();
        this.controller();
    }

    private void controller()
    {

    }

}