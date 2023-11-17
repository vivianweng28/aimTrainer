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

public class GamePanel extends JFrame implements ActionListener {

    private static final String OVER = "Game Over!";
    private static final String REPLAY = "R to replay";
    private AimTrainer game;
    private JFrame menu;
    private JComboBox sessions;

    // Constructs a game panel
    // effects:  sets size and background colour of panel,
    //           updates this with the game to be displayed
    public GamePanel(AimTrainer a) {
        super("The title");
        this.game = a;
        setPreferredSize(new Dimension(a.getDimX(), a.getDimY()));
        setBackground(Color.GRAY);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13) );
        setLayout(new FlowLayout());
    }

    // Draws the game
    // modifies: g
    // effects:  draws the game onto g
    private void drawGame(Graphics g) {
        drawMenu();
        drawDistance(g);
        drawTarget(g);
    }

    // Draw the target
    // modifies: g
    // effects:  draws the target onto g
    public void drawTarget(Graphics g) {
        Target t = game.getTarget();
        Color savedCol = g.getColor();
        g.setColor(t.COLOR);
        g.fillOval(t.getCenterX(), t.getCenterY(), t.getRadius(), t.getRadius());
        g.setColor(savedCol);
    }

    public void drawDistance(Graphics g) {
        Target t = game.getTarget();
        Color savedCol = g.getColor();
        g.setColor(t.COLOR);
        g.fillOval(t.getCenterX(), t.getCenterY(), t.getRadius(), t.getRadius());
        g.setColor(savedCol);
    }


    // Draw the menu
    // modifies: g
    // effects:  draws the menu onto g
    public void drawMenu() {
        menu = new JFrame("Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(null);
        menu.setSize(game.getDimX(),game.getDimY());
        JLabel l1 = new JLabel();
        l1.setText("Would you like to load an old session or start a new session?");
        JPanel text = new JPanel();
        text.setBounds(200, 200, 400, 100);
        text.add(l1);
        menu.add(text);
        JButton oldSession = new JButton("Old session");
        oldSession.setActionCommand("oldSession");
        oldSession.addActionListener(this);
        oldSession.setBounds(150, 300, 200, 50);
        menu.add(oldSession);
        JButton newSession = new JButton("New session");
        newSession.setActionCommand("newSession");
        newSession.addActionListener(this);
        newSession.setBounds(450, 300, 200, 50);
        menu.add(newSession);

        menu.setVisible(true);
    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("oldSession")) {
            game.setToOldSession();
            JLabel sessionNum = new JLabel("Please select a session number");
            String[] options = new String[game.getNumSessions()];
            for (int i = 0; i < options.length; i++) {
                options[i] = "Session " + (i + 1);
            }
            sessions = new JComboBox(options);
            sessions.setBounds(450, 490, 100, 50);
            menu.add(sessions);
            JPanel text = new JPanel();
            text.setBounds(100, 500, 400, 100);
            text.add(sessionNum);
            menu.add(text);

            JButton confirm = new JButton("Confirm");
            menu.add(confirm);
            confirm.addActionListener(this);
            confirm.setActionCommand("confirm");
            confirm.setBounds(300, 650, 200, 50);

            menu.setVisible(true);


        } else if (e.getActionCommand().equals("oldSession")) {
            // nothing happens, default is new session
        } else if (e.getActionCommand().equals("confirm")) {
            game.setCurrentSession(sessions.getSelectedIndex()); // set the old session as the current session
            menu.dispose();
        }
    }


//    // Draws the "game over" message and replay instructions
//    // modifies: g
//    // effects:  draws "game over" and replay instructions onto g
//    private void gameOver(Graphics g) {
//        Color saved = g.getColor();
//        g.setColor(new Color( 0, 0, 0));
//        g.setFont(new Font("Arial", 20, 20));
//        FontMetrics fm = g.getFontMetrics();
//        centreString(OVER, g, fm, SIGame.HEIGHT / 2);
//        centreString(REPLAY, g, fm, SIGame.HEIGHT / 2 + 50);
//        g.setColor(saved);
//    }

//    // Centres a string on the screen
//    // modifies: g
//    // effects:  centres the string str horizontally onto g at vertical position yPos
//    private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
//        int width = fm.stringWidth(str);
//        g.drawString(str, (SIGame.WIDTH - width) / 2, yPos);
//    }
}
