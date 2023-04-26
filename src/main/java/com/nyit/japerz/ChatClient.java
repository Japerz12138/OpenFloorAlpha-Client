package com.nyit.japerz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient {
    private static String hostName = "108.6.126.141";
    private static int port = 2333;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("[INFO] Connected to the chat server via " + hostName + ":" + port);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);
        System.out.print("[SYSTEM] Enter your name: ");
        String userName = scanner.nextLine();
        out.println(userName);
        System.out.println("[SYSTEM] Welcome! " + userName + ", you can chat now! Type \"/exit\" to exit the chat!");

        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (SocketException e) {
                System.out.println("[INFO] Connection lost");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("/exit")) {
                break;
            }
            out.println(message);
        }

        socket.close();
    }
}
