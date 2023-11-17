package ui;

// main class to start the application
public class Main {
    public static void main(String[] args) {
//        AimTrainer aimTrainer = new AimTrainer();
//        aimTrainer.start();
        GamePanel gp = new GamePanel(new AimTrainer());
        gp.drawMenu();
    }
}
