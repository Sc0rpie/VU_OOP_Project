package main;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import inputs.*;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Random random;
    private Color color;
    private float xDelta = 100, yDelta = 100;
    private float xDir = 1.5f, yDir = 1.5f;
    private int frames = 0;
    private long lastChecked = 0;

    private ArrayList<MyRect> rects = new ArrayList<>();
    public GamePanel(){
        random = new Random();
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        JButton btn_StopLoop = new JButton("Stop movement");
        btn_StopLoop.addActionListener(e -> {
            System.out.println("Button clicked!");
        });

        add(btn_StopLoop, BorderLayout.SOUTH);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
//        repaint();
    }

    public void changeYDelta(int value) {
        this.yDelta += value;
//        repaint();
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
//        repaint();
    }

    public void spawnRect(int x, int y) {
        rects.add(new MyRect(x,y));
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // For testing I/O (making more rects)
        for (MyRect rect : rects) {
            rect.updateRect();
            rect.draw(g);
        }

//        updateRectangle();
//        g.setColor(color);
//        g.fillRect((int)xDelta,(int)yDelta,200,50);

    }
    private void updateRectangle() {
        xDelta += xDir;
        if (xDelta < 0 || xDelta > 400) {
            xDir *= -1;
            color = getRandomColor();

        }
        yDelta += yDir;
        if (yDelta < 0 || yDelta > 400) {
            yDir *= -1;
            color = getRandomColor();
        }
    }

    private Color getRandomColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        return new Color(r,g,b);
    }


    public class MyRect {
        int x, y, h, w;
        int xDir = 1, yDir = 1;
        Color color;

        public MyRect(int x, int y) {
            this.x = x;
            this.y = y;
            this.h = random.nextInt(50)+5;
            this.w = h;
            color = newColor();
        }

        public void updateRect() {
            x += xDir;
            y += yDir;

            if (x < 0 || (x+w) > 400) {
                xDir *= -1;
                color = newColor();
            }
            if (y < 0 || (y+h) > 400) {
                yDir *= -1;
                color = newColor();
            }
        }

        private Color newColor() {
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);

            return new Color(r,g,b);
        }

        public void draw (Graphics g) {
            g.setColor(color);
            g.fillRect(x,y,w,h);
        }
    }
}
