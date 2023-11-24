package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

// represents a menu that allows user to either start a new session or load a saved session
public class Menu extends JFrame implements ActionListener {

    private AimTrainer game;
    private JComboBox sessions;
    private boolean over;

    // EFFECTS: Constructs a menu frame and sets size and background colour of the frame
    public Menu(AimTrainer a) {
        super("Menu");
        this.game = a;
        setPreferredSize(new Dimension(a.getDimX(), a.getDimY()));
        setBackground(Color.GRAY);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        drawMenu();
    }

    // Draw the menu
    // modifies: g
    // effects:  draws the menu onto g
    public void drawMenu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        over = false;
        setLayout(null);
        setSize(game.getDimX() + 200,game.getDimY() + 200);
        JLabel l1 = new JLabel();
        l1.setText("Would you like to load an old session or start a new session?");
        JPanel text = new JPanel();
        text.setBounds(200, 200, 400, 100);
        text.add(l1);
        add(text);
        JButton oldSession = new JButton("Old session");
        oldSession.setActionCommand("oldSession");
        oldSession.addActionListener(this);
        oldSession.setBounds(150, 300, 200, 50);
        add(oldSession);
        JButton newSession = new JButton("New session");
        newSession.setActionCommand("newSession");
        newSession.addActionListener(this);
        newSession.setBounds(450, 300, 200, 50);
        add(newSession);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: directs different actions to their consequences in the program
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("oldSession")) {
            oldSession();
        } else if (e.getActionCommand().equals("newSession")) {
            game.newSession();
            over = true;
            dispose();
        } else if (e.getActionCommand().equals("confirm")) {
            game.setCurrentSession(sessions.getSelectedIndex()); // set the old session as the current session
            dispose();
            over = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the selection of saved sessions and the confirm button
    public void oldSession() {
        JLabel sessionNum = new JLabel("Please select a session number");
        String[] options = new String[game.getNumSessions()];
        for (int i = 0; i < options.length; i++) {
            options[i] = "Session " + (i + 1);
        }
        sessions = new JComboBox(options);
        sessions.setBounds(450, 490, 100, 50);
        add(sessions);
        JPanel text = new JPanel();
        text.setBounds(100, 500, 400, 100);
        text.add(sessionNum);
        add(text);
        JButton confirm = new JButton("Confirm");
        add(confirm);
        confirm.addActionListener(this);
        confirm.setActionCommand("confirm");
        confirm.setBounds(300, 650, 200, 50);
        setVisible(true);

        game.setToOldSession(sessions.getSelectedIndex());
    }

    // EFFECTS: returns whether the user has finished using the menu window
    public boolean getOver() {
        return over;
    }

}
