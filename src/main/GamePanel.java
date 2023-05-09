package main;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import main.Game;

import inputs.*;

public class GamePanel extends JPanel implements Serializable {
    private MouseInputs mouseInputs;
    private Random random;
    private Color color;
    private float xDelta = 100, yDelta = 100;
    private float xDir = 1.5f, yDir = 1.5f;
    private int frames = 0;
    private long lastChecked = 0;
    protected boolean stopLoop = false;

    private ArrayList<MyRect> rects = new ArrayList<>();
    public GamePanel(){
        random = new Random();
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        JButton btn_StopLoop = new JButton("Stop movement");
        btn_StopLoop.addActionListener(e -> {
            if (!stopLoop) {
                stopLoop = true;
                System.out.println(stopLoop);
                btn_StopLoop.setText("Start movement");
            } else {
                stopLoop = false;
                System.out.println(stopLoop);
                btn_StopLoop.setText("Stop movement");
            }
        });

        JButton btn_Save = new JButton("Save");
        btn_Save.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    FileOutputStream fos = new FileOutputStream("rects.dat");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(rects);
                    oos.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        JButton btn_Load = new JButton("Load");
        btn_Load.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    FileInputStream fis = new FileInputStream("rects.dat");
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    rects = (ArrayList<MyRect>) ois.readObject();
                    ois.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        add(btn_StopLoop, BorderLayout.SOUTH);
        add(btn_Save, BorderLayout.SOUTH);
        add(btn_Load, BorderLayout.SOUTH);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
//        repaint();
    }

    public void changeYDelta(int value) {
        this.yDelta += value;
//        repaint();
    }

    public boolean getStopLoop() {
        return stopLoop;
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
//        repaint();
    }

    public void spawnRect(int x, int y) {
        rects.add(new MyRect(x, y));
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


    public class MyRect implements Serializable {
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
