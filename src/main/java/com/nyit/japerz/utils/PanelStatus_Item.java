package com.nyit.japerz.utils;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PanelStatus_Item extends javax.swing.JLayeredPane {
    private JPanel panel1;
    private JButton downloadButton;
    private JProgressBar progressBar1;

    public PanelStatus_Item() {
        setOpaque(true);
        progressBar1.setStringPainted(true);
    }

    public void showStatus(int values) {
        progressBar1.setValue(values);
    }

    private ActionListener eventSave;
    private ActionListener eventPause;

    public void addEventSave(ActionListener eventSave) {
        this.eventSave = eventSave;
    }

    public void addEvent(ActionListener event) {
        this.eventPause = event;
    }

    public void done() {
        downloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/done.png")));
        downloadButton.setName("D");
    }

    public boolean isPause() {
        return downloadButton.getName().equals("P");
    }

    public void startFile() {
        downloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pause.png")));
        downloadButton.setName("R");
    }
}
