package ui;

import model.Target;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


public class GamePanel extends JPanel implements MouseListener {

    private AimTrainer aimTrainer;
    private int distance;
    private int currentX;
    private int currentY;

    public GamePanel(AimTrainer a) {
        setPreferredSize(new Dimension(a.getDimX(), a.getDimY()));
        setBounds(300, 350, 500, 500);
        setBackground(Color.GRAY);
        this.aimTrainer = a;
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);

        if (aimTrainer.isOver()) {
            gameOver(g);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() > 0 && e.getX() < 800 && e.getY() > 0 && e.getY() < 800) { // mouse is in game panel
            currentX = e.getX();
            currentY = e.getY();
            aimTrainer.update(currentX, currentY);
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void drawGame(Graphics g) {
        drawTarget(g);
    }

    // Draw the target
    // modifies: g
    // effects:  draws the target onto g
    public void drawTarget(Graphics g) {
        Target t = aimTrainer.getTarget();
        Color savedCol = Color.red;
        int x = t.getCenterX() - (t.getRadius() / 2);
        int y = t.getCenterY() - (t.getRadius() / 2);
        g.fillOval(x, y, t.getRadius(), t.getRadius());
        g.setColor(savedCol);
    }

//    //This is the method that is called when the JButton btn is clicked
//    public void actionPerformed(ActionEvent e) {
//        if (e.getActionCommand().equals("changeDist")) {
//        }
//    }

    public void gameOver(Graphics g) {

    }

    synchronized void waitFor(int i) {
        try {
            wait(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
