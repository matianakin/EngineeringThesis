package project.view;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.NumberFormatter;

public class View extends JFrame {

    public View()
    {
        initComponents();
    }

    private JFrame frame;

    public void startingWindow()
    {
        frame = new JFrame("EngThes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(dialogPane);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     *
     * Function preparing all elements of the window
     */
    private void initComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelData = new JLabel();
        textFieldData = new JTextField();
        textFieldData.setText("Example of a text");
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(Integer.MIN_VALUE);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        textFieldSeed = new JFormattedTextField(formatter);
        emptyLabelSW = new JLabel();
        textResult = new JTextArea();
        textResult.setLineWrap(true);
        textResult.setWrapStyleWord(true);
        textResult.setEnabled(false);
        textResult.setDisabledTextColor(Color.black);
        emptyLabelSE = new JLabel();
        button1 = new JButton();
        scrollPane2 = new JScrollPane(textResult);
        menuBar = new JMenuBar();

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

                textFieldData.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(textFieldData);

                textFieldSeed.setHorizontalAlignment(SwingConstants.CENTER);
                textFieldSeed.setToolTipText("Seed");
                contentPanel.add(textFieldSeed);
                contentPanel.add(emptyLabelSW);
                button1.setText("Proceed");
                contentPanel.add(emptyLabelSE);
            }
            labelData.setText("Put your data here");
            labelData.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(labelData, BorderLayout.NORTH);
            labelData.setMinimumSize(new Dimension(640, 90));
            labelData.setPreferredSize(new Dimension(640, 90));
            labelData.setMaximumSize(new Dimension(640, 90));
            dialogPane.add(contentPanel, BorderLayout.CENTER);
            textResult.setText("Result");
            dialogPane.add(scrollPane2, BorderLayout.SOUTH);
            scrollPane2.setMinimumSize(new Dimension(640, 90));
            scrollPane2.setPreferredSize(new Dimension(640, 90));
            scrollPane2.setMaximumSize(new Dimension(640, 90));
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());


    }


    /**
     * Container in the window
     */
    private JPanel dialogPane;

    /**
     * Container in the window
     */
    private JPanel contentPanel;


    /**
     * A text label on the top of the window
     */
    private JLabel labelData;


    private JTextField textFieldData;


    /**
     * The text field taking the seed
     */
    private JFormattedTextField textFieldSeed;


    /**
     * Empty label to center other elements correctly
     */
    private JLabel emptyLabelSW;

    /**
     * A text window showing results of the operations
     */
    private JTextArea textResult;

    /**
     * Empty label to center other elements correctly
     */
    private JLabel emptyLabelSE;

    /**
     * The only button in the window. Launching the program
     */
    private JButton button1;

    private JScrollPane scrollPane2;

    /**
     * The menu bar used in the window
     */
    private JMenuBar menuBar;


}
