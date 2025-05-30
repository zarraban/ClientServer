package org.example.server;

import org.example.utils.ClientInfo;
import org.example.utils.ConnectionCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final ConcurrentHashMap<Integer, ClientInfo> activeConnections = new ConcurrentHashMap<>();
    private static final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    private static final Logger log = LoggerFactory.getLogger(Server.class);


    public static void start(final int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on " + serverSocket.getInetAddress().getHostAddress() + " port:" + serverSocket.getLocalPort());
            log.info("Server started on {} port:{}", serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());


            System.out.println("Write \"all\" to see all connections");


            Thread reader = new Thread(() -> {
                try {
                    String context;

                    while ((context = console.readLine()) != null) {
                        if (context.equalsIgnoreCase("All")) {
                            activeConnections.forEach((k, v) -> System.out.println(v));
                        }
                    }

                } catch (IOException e) {
                    logErrorMessage(e);

                }
            }
            );
            reader.start();


            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handleCommand(client)).start();
            }


        } catch (IOException e) {
            logErrorMessage(e);

        }


    }

    private static void handleCommand(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true)) {
            Integer clientsNumber = new ConnectionCounter().getDetails();

            ClientInfo clientInfo = new ClientInfo(clientsNumber, LocalDateTime.now(), client);

            addToActiveConnections(clientsNumber, clientInfo);

            log.info("{} connected", activeConnections.get(clientsNumber));
            System.out.println(activeConnections.get(clientsNumber) + " connected");


            out.println("Write command EXIT if you want to leave");
            String command;
            while ((command = in.readLine()) != null) {
                if (command.equalsIgnoreCase("exit")) {
                    out.println("Goodbye!");
                    activeConnections.remove(clientsNumber);
                    log.info("{}disconnected", activeConnections.get(clientsNumber));
                    break;
                }


                out.println("You wrote " + command);
                log.info("{} Message: {}", activeConnections.get(clientsNumber), command);

            }


        } catch (IOException e) {
            logErrorMessage(e);
        }
    }


    private static void addToActiveConnections(Integer id, ClientInfo clientInfo) {
        if (clientInfo != null) {
            activeConnections.put(id, clientInfo);
        }
    }

    private static void logErrorMessage(Exception e) {
        log.error("Error occurred: {}", e.getMessage());

    }
}
