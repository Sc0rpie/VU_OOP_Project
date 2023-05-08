package main;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;

    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
//        System.out.println("Game created!");
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now;
        int frames = 0;
        long lastChecked = System.currentTimeMillis();

        while(true) {
            now = System.nanoTime();
            if(now - lastFrame >= timePerFrame) {

                gamePanel.repaint();
                lastFrame = System.nanoTime();
                frames++;
            }

            if(System.currentTimeMillis() - lastChecked >= 1000) {
                lastChecked = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
}
