package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import model.Session;
import model.Suggestion;

// represents the main GUI of the application
public class MainGUI extends JFrame implements ActionListener {

    private AimTrainer aimTrainer;
    private GamePanel gp;
    private JComboBox distOptions;
    private Menu menu;
    private JPanel menuPanel;
    private JFrame viewSavedSessions;
    private JComboBox sessions;
    private JFrame filtered;
    private JLabel dist;
    private JLabel feedback;
    private JComboBox accuracyOptions;
    private JPanel filteredLabels;
    private JPanel sessionStats;
    private JPanel everything;
    private JPanel text;

    // EFFECTS: creates the main window that orchestrates all the windows of the application
    public MainGUI() {
        super("Aim Trainer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setUndecorated(false);
        setLayout(new FlowLayout());
        aimTrainer = new AimTrainer();
        aimTrainer.addGUI(this);
        setPreferredSize(new Dimension(aimTrainer.getDimX() + 100, aimTrainer.getDimY() + 200));
        menu = new Menu(aimTrainer);
        gp = new GamePanel(aimTrainer);
        menuBarSetUp();
        pack();
        setUp();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }


    public void menuPanelSetUp() {
        menuPanel = new JPanel();
        menuPanel.setBounds(300, 50, 500, 100);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(addSessionNum());
        menuPanel.add(addDistance());
        feedback = new JLabel();
        feedback.setBounds(200, 50, 200, 50);
        feedback.setText("Immediate Feedback: Nothing yet");
        menuPanel.add(feedback);

        add(menuPanel);
    }



    public void menuBarSetUp() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File"); // save
        fileAddMenuItems(file);

        JMenu view = new JMenu("View"); // view saved sessions
        viewAddMenuItems(view);

        menuBar.add(file);
        menuBar.add(view);

        menuBar.setPreferredSize(new Dimension(300,30));

        menuBar.setVisible(true);
        this.setJMenuBar(menuBar);
    }

    public void viewAddMenuItems(JMenu view) {
        JMenuItem savedSession = new JMenuItem("View saved sessions");
        savedSession.addActionListener(this);
        savedSession.setActionCommand("viewSavedSession");
        view.add(savedSession);

        JMenuItem currentSession = new JMenuItem("View current session");
        currentSession.addActionListener(this);
        currentSession.setActionCommand("viewCurrentSession");
        view.add(currentSession);

        JMenuItem filter = new JMenuItem("View filtered sessions");
        filter.addActionListener(this);
        filter.setActionCommand("viewFilteredSession");
        view.add(filter);
    }

    public void fileAddMenuItems(JMenu file) {
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("save");
        file.add(save);
    }

    public void setUp() {
        while (! menu.getOver()) {
            gp.waitFor(100);
        }
        if (menu.getOver()) {
            menuPanelSetUp();
            add(gp);
            aimTrainer.setTarget();
        }
    }

    public void showAllStats() {
        viewSavedSessions = new JFrame();
        viewSessionSetUp(viewSavedSessions, "confirmView");
    }


    public void viewSessionSetUp(JFrame frame, String confirmActionCommand) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setPreferredSize(new Dimension(700, 700));
        everything = new JPanel();
        everything.setLayout(new BoxLayout(everything, BoxLayout.Y_AXIS));
        JLabel sessionNum = new JLabel("Please select a session number");
        text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.X_AXIS));
        text.setBounds(100, 500, 400, 100);
        sessions = new JComboBox(createSessionOptions());
        sessions.setBounds(300, 300, 100, 50);
        text.add(sessions);
        text.add(sessionNum);
        JButton confirm = new JButton("Confirm");
        text.add(confirm);
        everything.add(text);
        confirmSetUp(confirm, confirmActionCommand);
        frame.add(everything);
        frame.pack();
        frame.setVisible(true);
    }

    public String[] createSessionOptions() {
        String[] options = new String[aimTrainer.getNumSessions()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Session " + (i + 1);
        }
        return options;
    }

    public void confirmSetUp(JButton confirm, String confirmActionCommand) {
        confirm.addActionListener(this);
        confirm.setActionCommand(confirmActionCommand);
        confirm.setBounds(300, 650, 200, 50);
    }

    public JPanel addDistance() {
        JPanel distance = new JPanel();
        distance.setLayout(new BoxLayout(distance, BoxLayout.X_AXIS));
        dist = new JLabel();
        dist.setText("Current distance: " + aimTrainer.getCurrentSession().getDistance() + "m  ");
        distance.add(dist);

        String[] options = new String[500];
        for (int i = 0; i < options.length; i++) {
            options[i] = (i + 1) + "m";
        }
        distOptions = new JComboBox(options);
        distOptions.setBounds(200, 50, 100, 50);
        distance.add(distOptions);

        JButton changeDist = new JButton("Change distance");
        changeDist.setActionCommand("changeDistance");
        changeDist.addActionListener(this);
        changeDist.setBounds(400, 50, 200, 50);
        distance.add(changeDist);

        return distance;
    }

    public JLabel addSessionNum() {
        JLabel sessionNum = new JLabel();
        sessionNum.setText("Session " + aimTrainer.getCurrentSession().getSessionNum() + ": ");
        sessionNum.setBounds(100, 10, 100, 20);
        return sessionNum;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changeDistance")) {
            aimTrainer.changeDist(distOptions.getSelectedIndex() + 1);
            dist.setText("Current distance: " + aimTrainer.getCurrentSession().getDistance() + "m");
            repaint();
        } else if (e.getActionCommand().equals("confirmView")) {
            Session selected = aimTrainer.getSession(sessions.getSelectedIndex());
            displaySelectedSessionStat(selected);
        } else if (e.getActionCommand().equals("viewSavedSession")) {
            showAllStats();
        } else if (e.getActionCommand().equals("save")) {
            aimTrainer.saveSessions();
        } else if (e.getActionCommand().equals("viewCurrentSession")) {
            currentSession();
            displaySelectedSessionStat(aimTrainer.getCurrentSession());
        } else if (e.getActionCommand().equals("viewFilteredSession")) {
            filter();
        } else if (e.getActionCommand().equals("confirmFiltered")) {
            filterSessions();
        }
    }

    public void currentSession() {
        if (everything != null && text != null) {
            everything.remove(text);
        } else {
            viewSavedSessions = new JFrame();
            viewSavedSessions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewSavedSessions.setLayout(new FlowLayout());
            viewSavedSessions.setPreferredSize(new Dimension(700, 700));
            everything = new JPanel();
            everything.setLayout(new BoxLayout(everything, BoxLayout.Y_AXIS));
        }
        displaySelectedSessionStat(aimTrainer.getCurrentSession());
    }

    public void filterSessions() {
        if (filteredLabels != null) {
            filtered.remove(filteredLabels);
        }
        filtered.repaint();
        filteredLabels = new JPanel();
        filteredLabels.setBounds(300, 300, 400, 400);
        filteredLabels.setLayout(new BoxLayout(filteredLabels, BoxLayout.Y_AXIS));

        for (Session s : aimTrainer.getAllSessions()) {
            if (s.getAccuracy() > (accuracyOptions.getSelectedIndex() + 1)) {
                JLabel sessionAccuracy = new JLabel();
                sessionAccuracy.setText("Session " + s.getSessionNum() + " Accuracy: " + s.getAccuracy() + "%");
                filteredLabels.add(sessionAccuracy);
            }
        }
        filtered.add(filteredLabels);
        filtered.setVisible(true);
    }

    public void filter() {
        filtered = new JFrame();
        filtered.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        filtered.setLayout(new FlowLayout());
        filtered.setPreferredSize(new Dimension(500, 500));
        JLabel sessionNum = new JLabel("View all sessions above this accuracy: ");
        String[] options = new String[100];
        for (int i = 0; i < options.length; i++) {
            options[i] = (i + 1) + "%";
        }
        accuracyOptions = new JComboBox(options);
        accuracyOptions.setBounds(300, 300, 100, 50);
        filtered.add(accuracyOptions);
        JPanel text = new JPanel();
        text.setBounds(100, 500, 400, 100);
        text.add(sessionNum);
        filtered.add(text);
        JButton confirm = new JButton("Confirm");
        filtered.add(confirm);
        confirm.addActionListener(this);
        confirm.setActionCommand("confirmFiltered");
        confirm.setBounds(300, 650, 200, 50);
        filtered.pack();
        filtered.setVisible(true);
    }

    public void displaySelectedSessionStat(Session selected) {
        if (sessionStats != null) {
            everything.remove(sessionStats);
        }
        viewSavedSessions.repaint();
        sessionStatsSetUp();
        Suggestion summary = selected.updateSummarySuggestion();
        JLabel summaryFeedback = new JLabel();
        summaryFeedback.setText("Session " + selected.getSessionNum() + " feedback: "
                + summary.giveSuggestion());
        summaryFeedback.setBounds(300, 300, 300, 50);
        sessionStats.add(summaryFeedback);
        JLabel totalAccuracy = new JLabel();
        totalAccuracy.setText("Total accuracy: " + selected.getAccuracy() + "%");
        summaryFeedback.setBounds(300, 400, 300, 50);
        sessionStats.add(totalAccuracy);
        JComboBox shotsAndSuggestion = new JComboBox(generateShotAndSuggestions(selected));
        shotsAndSuggestion.setBounds(350, 300, 300, 50);
        sessionStats.add(shotsAndSuggestion);
        everything.add(sessionStats);
        viewSavedSessions.add(everything);
        viewSavedSessions.pack();
        viewSavedSessions.setVisible(true);
    }

    public String[] generateShotAndSuggestions(Session selected) {
        String[] options = new String[selected.getAllSuggestions().size()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Shot " + (i + 1) + ": X= " + selected.getAllSuggestions().get(i).getCompX() + ", Y= "
                    + selected.getAllSuggestions().get(i).getCompY() + ", Suggestion: "
                    + selected.getAllSuggestions().get(i).giveSuggestion();
        }
        return options;
    }

    public void sessionStatsSetUp() {
        sessionStats = new JPanel();
        sessionStats.setBounds(100, 300, 400, 400);
        sessionStats.setLayout(new BoxLayout(sessionStats, BoxLayout.Y_AXIS));
    }

    public void immediateFeedback(Session s) {
        feedback.setText("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
    }
}
