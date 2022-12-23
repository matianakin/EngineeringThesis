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
        //view.startingWindow();
        this.controller();
    }

    /**
     * Controller.
     */
    private void controller()
    {
        model.readDictionary();
        //view.buttonProceedClicked();
        //view.cleanText();
        //model.setURL(view.getURL());
        //model.setTxtPath(view.getFilePath());
        model.setTxtPath("afile.txt");
        model.setReqs(model.readFromFile(model.getTxtPath()));
        model.removeS();
        model.swapWords();
        System.out.println(Arrays.toString(model.getReqs()));
    }

}