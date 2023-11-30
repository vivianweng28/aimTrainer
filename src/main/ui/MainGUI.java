package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import model.EventLog;
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
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                EventLog.getInstance().printLog(EventLog.getInstance());
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: creates the menu panel that will appear at the top of the main window
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


    // MODIFIES: this
    // EFFECTS: creates the menu bar of the main window
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

    // MODIFIES:this
    // EFFECTS: adds options under the view option on the menu bar
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

        JMenuItem filteredShots = new JMenuItem("View perfect shots for this session");
        filteredShots.addActionListener(this);
        filteredShots.setActionCommand("viewFilteredShots");
        view.add(filteredShots);
    }

    // MODIFIES:this
    // EFFECTS: adds options under the file option on the menu bar
    public void fileAddMenuItems(JMenu file) {
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("save");
        file.add(save);
    }

    // MODIFIES:this
    // EFFECTS: sets up the main aim trainer window after the menu window has closed
    public void setUp() {
        while (! menu.getOver()) {
            gp.waitFor(100);
        }
        if (menu.getOver()) {
            menuPanelSetUp();
            add(gp);
//            aimTrainer.setTarget();
        }
    }

    // MODIFIES:this
    // EFFECTS: sets up the window that will display all the saved sessions and their statistics
    public void showAllStats() {
        viewSavedSessions = new JFrame();
        viewSessionSetUp(viewSavedSessions, "confirmView");
    }


    // MODIFIES:this
    // EFFECTS: creates the basic framework for having a scroll wheel to select a
    // saved session number and confirm button
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

    // MODIFIES:this
    // EFFECTS: creates a scroll wheel with all the saved sessions as the options
    public String[] createSessionOptions() {
        String[] options = new String[aimTrainer.getNumSessions()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Session " + (i + 1);
        }
        return options;
    }

    // MODIFIES:this
    // EFFECTS: sets up confirm button to make it clickable and viewable
    public void confirmSetUp(JButton confirm, String confirmActionCommand) {
        confirm.addActionListener(this);
        confirm.setActionCommand(confirmActionCommand);
        confirm.setBounds(300, 650, 200, 50);
    }

    // MODIFIES:this
    // EFFECTS: creates a panel with the current the option to change distance to the menu panel and returns said panel
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

    // MODIFIES:this
    // EFFECTS: creates and returns a label with the current session number
    public JLabel addSessionNum() {
        JLabel sessionNum = new JLabel();
        sessionNum.setText("Session " + aimTrainer.getCurrentSession().getSessionNum() + ": ");
        sessionNum.setBounds(100, 10, 100, 20);
        return sessionNum;
    }

    // MODIFIES: this
    // EFFECTS: directs different actions to their consequences in the program
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changeDistance")) {
            aimTrainer.changeDist(distOptions.getSelectedIndex() + 1);
            dist.setText("Current distance: " + aimTrainer.getCurrentSession().getDistance() + "m");
            repaint();
        } else if (e.getActionCommand().equals("confirmView")) {
            Session selected = aimTrainer.getSession(sessions.getSelectedIndex());
            displaySelectedSessionStat(selected, false);
        } else if (e.getActionCommand().equals("viewSavedSession")) {
            showAllStats();
        } else if (e.getActionCommand().equals("save")) {
            aimTrainer.saveSessions();
        } else if (e.getActionCommand().equals("viewCurrentSession")) {
            currentSession(false);
            displaySelectedSessionStat(aimTrainer.getCurrentSession(), false);
        } else if (e.getActionCommand().equals("viewFilteredSession")) {
            filter();
        } else if (e.getActionCommand().equals("confirmFiltered")) {
            filterSessions();
        } else if (e.getActionCommand().equals("viewFilteredShots")) {
            currentSession(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a display of the statistics and all the shots for the whole current session if filtered is
    // false. otherwise, displays the statistics and shots for only perfect shots
    public void currentSession(boolean filtered) {
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
        displaySelectedSessionStat(aimTrainer.getCurrentSession(), filtered);
    }

    // MODIFIES: this
    // EFFECTS: creates a display for the filtered sessions that is above the user inputted accuracy
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

    // MODIFIES: this
    // EFFECTS: creates a display to let the user input an accuracy threshold to view all the sessions with an accuracy
    // above the inputted threshold.
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

    // MODIFIES: this
    // EFFECTS: sets up display for all the statistics of a selected session if filtered is true. Otherwise,
    // only displays statistics for perfect shots.
    public void displaySelectedSessionStat(Session selected, boolean filtered) {
        if (sessionStats != null) {
            everything.remove(sessionStats);
        }
        viewSavedSessions.repaint();
        sessionStatsSetUp();
        summaryFeedback(selected);
        JLabel totalAccuracy = new JLabel();
        totalAccuracy.setText("Total accuracy: " + selected.getAccuracy() + "%");
        totalAccuracy.setBounds(300, 400, 300, 50);
        sessionStats.add(totalAccuracy);
        if (filtered) {
            filteredAction();
        }
        JComboBox shotsAndSuggestion = new JComboBox(generateShotAndSuggestions(selected, filtered));
        shotsAndSuggestion.setBounds(350, 300, 300, 50);
        sessionStats.add(shotsAndSuggestion);
        everything.add(sessionStats);
        viewSavedSessions.add(everything);
        viewSavedSessions.pack();
        viewSavedSessions.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: generates and adds a label for the summary feedback and adds it to the panel that contains all the
    // sessions stats.
    public void summaryFeedback(Session selected) {
        Suggestion summary = selected.updateSummarySuggestion();
        JLabel summaryFeedback = new JLabel();
        summaryFeedback.setText("Session " + selected.getSessionNum() + " feedback: "
                + summary.giveSuggestion());
        summaryFeedback.setBounds(300, 300, 300, 50);
        sessionStats.add(summaryFeedback);
    }

    // MODIFIES: this
    // EFFECTS: sets up additional display to differentiate the gui for a filtered perfect shot list and normal list.
    public void filteredAction() {
        JLabel perfectShots = new JLabel();
        perfectShots.setText("These are the perfect shots: ");
        perfectShots.setBounds(300, 400, 300, 50);
        sessionStats.add(perfectShots);
    }

    // MODIFIES: this
    // EFFECTS: generates and returns an array of all shots and suggestions if filtered is true. Otherwise, the array
    // only contains the statistics of a perfect shot.
    public String[] generateShotAndSuggestions(Session selected, boolean filtered) {
        String[] options;
        if (filtered) {
            List<String> filteredShots = selected.getPerfectShots();
            options = new String[filteredShots.size()];
            for (int i = 0; i < options.length; i++) {
                options[i] = filteredShots.get(i);
            }
        } else {
            options = new String[selected.getAllSuggestions().size()];
            for (int i = 0; i < options.length; i++) {
                options[i] = "Shot " + (i + 1) + ": X= " + selected.getAllSuggestions().get(i).getCompX() + ", Y= "
                    + selected.getAllSuggestions().get(i).getCompY() + ", Suggestion: "
                    + selected.getAllSuggestions().get(i).giveSuggestion();
            }
        }
        return options;
    }

    // MODIFIES: this
    // EFFECTS: sets up basic display features for the panel that contains all the statistics
    public void sessionStatsSetUp() {
        sessionStats = new JPanel();
        sessionStats.setBounds(100, 300, 400, 400);
        sessionStats.setLayout(new BoxLayout(sessionStats, BoxLayout.Y_AXIS));
    }

    // MODIFIES: this
    // EFFECTS: displays the feedback for the last shot taken.
    public void immediateFeedback(Session s) {
        feedback.setText("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
    }
}
