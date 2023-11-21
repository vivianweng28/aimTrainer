package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Target;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/*
 * The panel in which the game is rendered.
 */

public class Menu extends JFrame implements ActionListener {

    private AimTrainer game;
    private JComboBox sessions;
    private boolean over;

    // Constructs a game panel
    // effects:  sets size and background colour of panel,
    //           updates this with the game to be displayed
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
        setSize(game.getDimX(),game.getDimY());
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

    //This is the method that is called when the JButton btn is clicked
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

    public boolean getOver() {
        return over;
    }

}
