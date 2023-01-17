package project.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;


/**
 * The type View.
 */
public class View extends JFrame {

    /**
     * Instantiates a new View.
     */
    public View() {
        initComponents();
    }

    /**
     * The Frame.
     */
    private JFrame frame;

    /**
     * Starting window.
     */
    public void startingWindow() {
        frame = new JFrame("EngineeringProject");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(dialogPane);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Function preparing all elements of the window
     */
    private void initComponents() {
        dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        textFieldURL = new JTextField();
        fileChooser = new JFileChooser();
        textFieldURL.setText("Enter URL here");
        textResult = new JTextArea();
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);
        textResult.setEnabled(false);
        textResult.setDisabledTextColor(Color.black);
        buttonProceed = new JButton();
        JButton buttonFile = new JButton();
        JScrollPane scrollPane = new JScrollPane(textResult);
        setMinimumSize(new Dimension(640, 360));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setMinimumSize(new Dimension(640, 360));
            dialogPane.setPreferredSize(new Dimension(640, 360));
            dialogPane.setMaximumSize(new Dimension(640, 360));
            dialogPane.setLayout(new BorderLayout());

            {
                contentPanel.setLayout(new GridLayout(2, 3));

                contentPanel.add(textFieldURL);

                /*contentPanel.add(emptyLabelSW);*/
                buttonFile.addActionListener(this::buttonFileActionPerformed);
                buttonProceed.addActionListener(this::buttonProceedActionPerformed);
                buttonProceed.setText("Proceed");
                buttonFile.setText("Choose a file");
                /*contentPanel.add(emptyLabelSE);*/
                contentPanel.add(buttonFile);
                contentPanel.add(buttonProceed);
            }

            dialogPane.add(contentPanel, BorderLayout.CENTER);
            textResult.setText("Result");
            dialogPane.add(scrollPane, BorderLayout.SOUTH);
            scrollPane.setMinimumSize(new Dimension(640, 90));
            scrollPane.setPreferredSize(new Dimension(640, 90));
            scrollPane.setMaximumSize(new Dimension(640, 90));
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());


    }


    /**
     * Button proceed action performed.
     *
     * @param e the e
     */
    private void buttonProceedActionPerformed(ActionEvent e) {

        if (filePath == null || filePath.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please provide a text file with the desired requirements",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else if (textFieldURL.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please provide a URL to the chosen web app",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            synchronized (buttonProceed) {
                buttonProceed.notify();
            }
        }
    }

    /**
     * Button file action performed.
     *
     * @param e the e
     */
    private void buttonFileActionPerformed(ActionEvent e) {
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        /**
         * The File selector result.
         */
        int fileSelectorResult = fileChooser.showOpenDialog(null);
        if (fileSelectorResult == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            setFilePath(selectedFile.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Please provide a text file with the desired requirements",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Button proceed clicked.
     */
    public void buttonProceedClicked() {
        synchronized (buttonProceed) {
            try {
                buttonProceed.wait();
            } catch (InterruptedException ignored) {

            }
        }
    }


    /**
     * Container in the window
     */
    private JPanel dialogPane;


    /**
     * The Text field url.
     */
    private JTextField textFieldURL;


    /**
     * A text window showing results of the operations
     */
    private JTextArea textResult;


    /**
     * The Button proceed.
     */
    private JButton buttonProceed;

    /**
     * The File chooser.
     */
    private JFileChooser fileChooser;

    /**
     * The File path.
     */
    private String filePath;


    /**
     * Sets file path.
     *
     * @param filePath the file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets result text
     *
     * @param text the text
     */
    public void setText(String text)
    {
        String newText = textResult.getText() + text;
        textResult.setText(newText);

    }

    /**
     * Gets text to perform an operation on
     *
     * @return the text
     */
    public String getURL()
    {
        return textFieldURL.getText();
    }

    /**
     * Cleans result text
     */
    public void cleanText()
    {
        textResult.setText("");
    }

    /**
     * Prints error message in a popup window
     *
     * @param e the exception
     */
    public void printErrorMsg(Exception e)
    {
        JOptionPane.showMessageDialog(null,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
