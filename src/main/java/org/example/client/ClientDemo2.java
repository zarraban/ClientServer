package org.example.client;

public class ClientDemo2 {
    public static void main(String[] args) {
        Thread client = new Thread(new Client());
        client.start();
    }
}
