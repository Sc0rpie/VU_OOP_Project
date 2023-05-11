package main;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel{
    JPanel buttonPanel = new JPanel();
    JButton startButton = new JButton("Start");
    JButton exitButton = new JButton("Exit");

    public ButtonPanel() {
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
