package com.remote.app;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class IOSocket {

    private static Socket socket;

    private static void connect() {
        try {
            URI uri = URI.create(Constants.SOCKET_URL);
            IO.Options options = IO.Options.builder()
                    .setForceNew(false)
                    .setReconnection(true)
                    .setReconnectionDelay(1_000)
                    .setReconnectionDelayMax(5_000)
                    .setReconnectionAttempts(Integer.MAX_VALUE)
                    .setRandomizationFactor(0.5d)
                    .setTimeout(20_000)
                    .setUpgrade(true)
                    .build();
            socket = IO.socket(uri, options);
            socket = socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        if (socket == null || !socket.isActive())
            connect();
        return socket;
    }
}