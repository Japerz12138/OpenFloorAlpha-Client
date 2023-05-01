package com.nyit.japerz.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PanelStatus extends javax.swing.JPanel {
    private JPanel panel1;
    private JProgressBar progressBar1;
    private JButton button1;

    public PanelStatus() {
        progressBar1.setStringPainted(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button1.getName().equals("R")) {
                    button1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/resume.png"))));
                    button1.setName("P");
                } else if (button1.getName().equals("P")) {
                    button1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/pause.png"))));
                    button1.setName("R");
                }
                event.actionPerformed(e);
            }
        });
    }

    public void showStatus(int values) {
        progressBar1.setValue(values);
    }

    public void done() {
        button1.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon/done.png"))).getImage()));
        button1.setName("D");
    }

    public boolean isPause() {
        return button1.getName().equals("P");
    }

    private ActionListener event;

    public void addEvent(ActionListener event) {
        this.event = event;
    }


}
