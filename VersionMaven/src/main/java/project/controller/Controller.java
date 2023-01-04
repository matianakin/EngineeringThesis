package project.controller;

import project.model.Model;
import project.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


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
        model.setAddress(view.getURL());
        model.GetXHTML();
        model.setTxtPath(view.getFilePath());
        model.setReqs(model.readFromFile(model.getTxtPath()));
        model.removeS();
        model.swapWords();
        model.iterateReqs();
        model.buttonSimulator();
        //System.out.println(model.compare("xhtml.txt", "xhtmlAfterButton.txt"));
        try {
            PrintWriter writer = new PrintWriter("compare.txt", StandardCharsets.UTF_8);
            writer.println(model.compare("xhtml.txt", "xhtmlAfterButton.txt"));
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.setText(model.getErrors().toString());
        //exit(0);
    }

}