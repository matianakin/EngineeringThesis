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
        model.setAddress(view.getURL());
        model.GetXHTML();
        model.setTxtPath(view.getFilePath());
        model.setReqs(model.readFromFile(model.getTxtPath()));
        model.removeS();
        model.swapWords();
        model.iterateReqs();
        model.buttonSimulator("btnDypl");
        //model.compare("xhtml.txt", "xhtmlAfterButton.txt");
        view.setText(model.getErrors().toString());
        //exit(0);
    }

}