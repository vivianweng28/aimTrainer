package ui;

import model.Target;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

// represents a game panel that will have a target that appears for the user to "shoot"
public class GamePanel extends JPanel implements MouseListener {

    private AimTrainer aimTrainer;
    private int currentX;
    private int currentY;

    // EFFECTS: creates a game panel will draw a target for the user to "shoot"
    public GamePanel(AimTrainer a) {
        setPreferredSize(new Dimension(a.getDimX(), a.getDimY()));
        setBounds(a.getDimX() / 2, a.getDimY() / 2, 400, 400);
        setBackground(Color.GRAY);
        this.aimTrainer = a;
        addMouseListener(this);
    }

    // MODIFIES: this
    // EFFECTS: draws the target onto the screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);

    }

    // MODIFIES: this, aimTrainer
    // EFFECTS: updates the aim trainer object and repaints the game panel anytime a mouse clicks on the game panel
    public void updateGame(MouseEvent e) {
        if (e.getX() > 0 && e.getX() < aimTrainer.getDimX()
                && e.getY() > 0 && e.getY() < aimTrainer.getDimY()) { // mouse is in game panel
            currentX = e.getX();
            currentY = e.getY();
            aimTrainer.update(currentX, currentY);
            repaint();
        }
    }

    // EFFECTS: updates the game whenever there is a mouse click
    @Override
    public void mouseClicked(MouseEvent e) {
        updateGame(e);
    }

    // EFFECTS: need to override for superclass, but no use here
    @Override
    public void mousePressed(MouseEvent e) {
    }

    // EFFECTS: need to override for superclass, but no use here
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // EFFECTS: need to override for superclass, but no use here
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    // EFFECTS: need to override for superclass, but no use here
    @Override
    public void mouseExited(MouseEvent e) {

    }

    // MODIFIES: g
    // EFFECTS: need to override for superclass, but no use here
    public void drawGame(Graphics g) {
        drawTarget(g);
    }

    // MODIFIES: g
    // EFFECTS:  draws the target onto g
    public void drawTarget(Graphics g) {
        Target t = aimTrainer.getTarget();
        Color savedCol = Color.red;
        int x = t.getCenterX() - (t.getRadius() / 2);
        int y = t.getCenterY() - (t.getRadius() / 2);
        g.fillOval(x, y, t.getRadius(), t.getRadius());
        g.setColor(savedCol);
    }

    // EFFECTS: pauses the program for a given amount of milliseconds
    synchronized void waitFor(int i) {
        try {
            wait(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
