package org.example.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class Client implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            Thread reader = new Thread(() -> {
                try {
                    String content;
                    while ((content = in.readLine()) != null) {
                        System.out.println("Server: " + content);
                    }
                } catch (IOException e) {
                    logException(e);
                }

            }
            );
            reader.start();
            String dataFromConsole;
            while ((dataFromConsole = console.readLine()) != null) {
                out.println(dataFromConsole);
            }


        } catch (IOException e) {
            logException(e);
        }

    }

    private static void logException(Exception e) {
        log.error("Error occurred: {}", e.getMessage());
    }
}
