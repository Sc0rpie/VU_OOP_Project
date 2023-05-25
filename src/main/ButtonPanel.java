package main;

import javax.swing.*;
import java.awt.*;

/**
 * Klasė, kuri atvaizduoja mygtukų panelę.
 */
public class ButtonPanel extends JPanel{
    JPanel buttonPanel = new JPanel();
    JButton startButton = new JButton("Start");
    JButton exitButton = new JButton("Exit");

    /**
     * Konstruktorius, kuris inicializuoja mygtukų panelę ir prideda mygtukus.
     */
    public ButtonPanel() {
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
    }

    /**
     * Gauna mygtukų panelę.
     *
     * @return Mygtukų panelė
     */
    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
