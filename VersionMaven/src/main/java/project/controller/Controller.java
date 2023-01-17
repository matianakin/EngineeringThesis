package project.controller;

import project.model.Model;
import project.view.View;


/**
 * The type Controller.
 */
public class Controller {

    private final Model model;

    private final View view;


    /**
     * Instantiates a new Controller.
     */
    public Controller() {
        this.model = new Model();
        this.view = new View();

        this.controller();
    }

    /**
     * Controller.
     */
    private void controller() {
        view.startingWindow();
        model.readDictionary();
        view.buttonProceedClicked();
        view.cleanText();
        model.setAddress(view.getURL());
        model.getXHTML();
        model.setTxtPath(view.getFilePath());
        model.setReqs(model.readFromFile(model.getTxtPath()));
        model.removeS();
        model.swapWords();
        model.iterateReqs();
        view.setText(model.getErrors().toString());
    }

}