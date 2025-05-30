package org.example.utils;


import lombok.Getter;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter

public class ClientInfo {
    private final Integer id;
    private final String name = "client";
    private final LocalDateTime time;
    private final Socket socket;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM.dd-HH:mm:ss");

    public ClientInfo(Integer id, LocalDateTime time, Socket socket) {
        this.id = id;
        this.time = time;
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "[" + dtf.format(time) + "]" + name + " " + id;
    }
}
