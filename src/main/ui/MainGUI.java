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


public class MainGUI extends JFrame implements ActionListener {
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
        setLayout(new FlowLayout());
        aimTrainer = new AimTrainer();
        aimTrainer.addGUI(this);
        setPreferredSize(new Dimension(aimTrainer.getDimX() + 100, aimTrainer.getDimY() + 100));
        menu = new Menu(aimTrainer);
        gp = new GamePanel(aimTrainer);
        menuPanel = new JPanel();
        menuPanel.setBounds(300, 50, 500, 100);
        add(menuPanel);
        add(gp);
        pack();
        menuBarSetUp();
        setUp();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void menuBarSetUp() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu(); // save
        fileAddMenuItems(file);

        JMenu edit = new JMenu(); // change distance
        editAddMenuItems(edit);

        JMenu view = new JMenu(); // view old sessions
        viewAddMenuItems(view);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(view);

        menuBar.setVisible(true);
        setJMenuBar(menuBar);
        revalidate();
    }

    public void editAddMenuItems(JMenu edit) {
        JMenuItem changeDistance = new JMenuItem();
        changeDistance.addActionListener(this);
        changeDistance.setActionCommand("changeDist");
        edit.add(changeDistance);
    }

    public void viewAddMenuItems(JMenu view) {
        JMenuItem oldSession = new JMenuItem();
        oldSession.addActionListener(this);
        oldSession.setActionCommand("viewOldSession");
        view.add(oldSession);
    }

    public void fileAddMenuItems(JMenu file) {
        JMenuItem save = new JMenuItem();
        save.addActionListener(this);
        save.setActionCommand("save");
        file.add(save);
    }

    public void setUp() {
        while (! menu.getOver()) {
            gp.waitFor(1000);
        }
        if (menu.getOver()) {
            addDistance();
            showAllStats();
        }
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
//        distOptions = new JComboBox(options);
//        distOptions.setBounds(200, 50, 100, 50); // TODO: CHANGE
//        menuPanel.add(distOptions);
//
//        JButton changeDist = new JButton("Change distance");
//        changeDist.setActionCommand("changeDist");
//        changeDist.setBounds(400, 50, 200, 50);
//        menuPanel.add(changeDist);

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
