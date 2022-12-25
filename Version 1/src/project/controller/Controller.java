package project.controller;
import project.model.*;
import project.view.*;

import java.util.Arrays;

import static java.lang.System.exit;


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
        model.readDictionary();
        view.buttonProceedClicked();
        view.cleanText();
        model.setURL(view.getURL());
        model.setAddress(view.getURL());
        model.GetXHTML();
        model.setTxtPath(view.getFilePath());
        model.setReqs(model.readFromFile(model.getTxtPath()));
        model.removeS();
        model.swapWords();
        model.iterateReqs();
        model.buttonSimulator();
        System.out.println(model.compare("xhtml.txt", "xhtmlAfterButton.txt"));
        view.setText(model.getErrors().toString());
        //exit(0);
    }

}