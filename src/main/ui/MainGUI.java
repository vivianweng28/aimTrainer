package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import model.CircleSession;
import model.Session;
import model.Target;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;


public class MainGUI extends JFrame {
    // Constructs main window
    // effects: sets up window in which Space Invaders game will be played

    private AimTrainer aimTrainer;
    private GamePanel gp;
    private JComboBox distOptions;
    private Menu menu;
    private JPanel menuPanel;

    public MainGUI() {
        super("Aim Trainer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setUndecorated(false);
        aimTrainer = new AimTrainer();
        setPreferredSize(new Dimension(aimTrainer.getDimX() + 100, aimTrainer.getDimY() + 100));
        menu = new Menu(aimTrainer);
        gp = new GamePanel(aimTrainer);
        JFrame thisFrame = this;
        menuPanel = new JPanel();
        menuPanel.setBounds(300, 50, 500, 100);
        add(gp);
        pack();
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                showCurrentStats();
                int confirm = JOptionPane.showOptionDialog(thisFrame,
                        "Do you want to save this application?",
                        "Save?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    aimTrainer.saveSessions();
                    System.exit(0);
                } else if (confirm == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        };
        addWindowListener(exitListener);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        while (! menu.getOver()) {
            gp.waitFor(1000);
        }
        if (menu.getOver()) {
            addDistance();
            showAllStats();
        }
        //centreOnScreen();
        setVisible(true);
        //addTimer();
    }

    public void showCurrentStats() {
        //TODO
    }

    public void showAllStats() {
        // TODO: see design drawing on ipad
    }


    public void addDistance() {
        JLabel dist = new JLabel();
        dist.setText("Current distance: " + aimTrainer.getCurrentSession().getDistance() + "m");
        menuPanel.add(dist);

        String[] options = new String[500];
        for (int i = 0; i < options.length; i++) {
            options[i] = (i + 1) + "m";
        }
        distOptions = new JComboBox(options);
        distOptions.setBounds(200, 50, 100, 50); // TODO: CHANGE
        menuPanel.add(distOptions);

        JButton changeDist = new JButton("Change distance");
        changeDist.setActionCommand("changeDist");
        changeDist.setBounds(400, 50, 200, 50);
        menuPanel.add(changeDist);

        add(menuPanel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changeDist")) {
            aimTrainer.changeDist(distOptions.getSelectedIndex() + 1);
        }
    }

    public void immediateFeedback(Session s) {
        JLabel feedback = new JLabel();
        feedback.setBounds(200, 50, 200, 50);
        feedback.setText("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
        menuPanel.add(feedback);
    }
}
