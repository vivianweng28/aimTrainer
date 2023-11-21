package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import model.CircleSession;
import model.Session;
import model.Suggestion;
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
    private JFrame sessionsList;
    private JComboBox sessions;

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
        add(menuPanel); // TODO: add a this is session x to menu panel
        add(gp);
        pack();
        menuBarSetUp();
        setUp();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void menuBarSetUp() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File"); // save
        fileAddMenuItems(file);

//        JMenu edit = new JMenu("Edit"); // change distance
//        editAddMenuItems(edit);

        JMenu view = new JMenu("View"); // view old sessions
        viewAddMenuItems(view);

        menuBar.add(file);
//        menuBar.add(edit);
        menuBar.add(view);

        menuBar.setPreferredSize(new Dimension(300,30));

        menuBar.setVisible(true);
        this.setJMenuBar(menuBar);
    }

//    public void editAddMenuItems(JMenu edit) {
//        JMenuItem changeDistance = new JMenuItem("Change Distance");
//        changeDistance.addActionListener(this);
//        changeDistance.setActionCommand("changeDist");
//        edit.add(changeDistance);
//    }

    public void viewAddMenuItems(JMenu view) {
        JMenuItem oldSession = new JMenuItem("View old sessions");
        oldSession.addActionListener(this);
        oldSession.setActionCommand("viewOldSession");
        view.add(oldSession);

        JMenuItem currentSession = new JMenuItem("View current session");
        currentSession.addActionListener(this);
        currentSession.setActionCommand("viewCurrentSession");
        view.add(currentSession);
    }

    public void fileAddMenuItems(JMenu file) {
        JMenuItem save = new JMenuItem("Save");
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

    public void showAllStats() {
        // TODO: see design drawing on ipad
        sessionsList = new JFrame();
        sessionsList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sessionsList.setLayout(new FlowLayout());
        sessionsList.setPreferredSize(new Dimension(500, 500));
        JLabel sessionNum = new JLabel("Please select a session number");
        String[] options = new String[aimTrainer.getNumSessions()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Session " + (i + 1);
        }
        sessions = new JComboBox(options);
        sessions.setBounds(300, 300, 100, 50);
        sessionsList.add(sessions);
        JPanel text = new JPanel();
        text.setBounds(100, 500, 400, 100);
        text.add(sessionNum);
        sessionsList.add(text);
        JButton confirm = new JButton("Confirm");
        sessionsList.add(confirm);
        confirm.addActionListener(this);
        confirm.setActionCommand("confirm");
        confirm.setBounds(300, 650, 200, 50);
        sessionsList.pack();
        sessionsList.setVisible(true);
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
        changeDist.setActionCommand("changeDistance");
        changeDist.setBounds(400, 50, 200, 50);
        menuPanel.add(changeDist);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changeDistance")) {
            aimTrainer.changeDist(distOptions.getSelectedIndex() + 1);
            repaint();
        } else if (e.getActionCommand().equals("confirm")) {
            Session selected = aimTrainer.getSession(sessions.getSelectedIndex());
            displaySelectedSessionStat(selected);
        } else if (e.getActionCommand().equals("viewOldSession")) {
            showAllStats();
        } else if (e.getActionCommand().equals("save")) {
            aimTrainer.saveSessions();
        } else if (e.getActionCommand().equals("viewCurrentSession")) {
            displaySelectedSessionStat(aimTrainer.getCurrentSession());
        }
    }

    public void displaySelectedSessionStat(Session selected) {
        Suggestion summary = selected.updateSummarySuggestion();
        JLabel summaryFeedback = new JLabel();
        summaryFeedback.setText("Session " + selected.getSessionNum() + " feedback: "
                + summary.giveSuggestion());
        sessionsList.add(summaryFeedback);
        JLabel totalAccuracy = new JLabel();
        totalAccuracy.setText("Total accuracy: " + selected.getAccuracy() + "%");
        sessionsList.add(totalAccuracy);
        JComboBox shotsAndSuggestion = new JComboBox();

        String[] options = new String[selected.getAllSuggestions().size()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Shot " + (i + 1) + ": X= " + selected.getAllSuggestions().get(i).getCompX() + "Y= "
                    + selected.getAllSuggestions().get(i).getCompY() + "Suggestion: "
                    + selected.getAllSuggestions().get(i).giveSuggestion();
        }

        sessionsList.add(shotsAndSuggestion);
        sessionsList.setVisible(true);
    }

    public void immediateFeedback(Session s) {
        JLabel feedback = new JLabel();
        feedback.setBounds(200, 50, 200, 50);
        feedback.setText("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
        menuPanel.add(feedback);
    }
}
