package com.nyit.japerz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DownloadConfirm extends JFrame{
    private JPanel panel1;
    private JButton YESButton;
    private JButton NOButton;

    ChatRoom cr = new ChatRoom();

    String[] filename = cr.getfileName();
    byte[] fileData = cr.getfiledata();

    private String[] getfileData;


    public DownloadConfirm() {
        ImageIcon img = new ImageIcon("I:\\CODE\\OpenFloorAlpha-Client\\chat_icon.png");
        setContentPane(panel1);
        setTitle("Download Confirm");
        setSize(400,300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setResizable(false);


        YESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: When click yes, download the file that server transferred.
                File file = new File(Arrays.toString(filename));
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
