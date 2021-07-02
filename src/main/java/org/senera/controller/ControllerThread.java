/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.senera.controller;

import org.senera.Version;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author George
 */
public class ControllerThread implements Runnable {

    private static final Logger LOG = Logger.getLogger(ControllerThread.class.getName());

    /**
     * The port that the controller is listening for connections.
     */
    private final int port;

    private final Controller controller;

    private ServerSocket serverSocket = null;

    //indicates whether this thread should stop
    private boolean stop = false;

    public ControllerThread(int port, Controller controller) {
        this.port = port;
        this.controller = controller;
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        while (!stop) {
            try {
                Socket clientSocket = serverSocket.accept();

                CommandSession commandSession = new CommandSession(System.currentTimeMillis(),
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getInetAddress().getCanonicalHostName(),
                        clientSocket.getPort());
                LOG.log(Level.FINE, "Created a new command session: {0}", commandSession);
                ConnectionThread ct = new ConnectionThread(clientSocket, controller, commandSession.getSessionId(), Version.getVersion());
                commandSession.setConnectionthread(ct);
                Thread t = new Thread(ct);
                t.start();
                LOG.log(Level.INFO, "Started a new connection thread.");
                controller.addCommandSession(commandSession);
            } catch (SocketException ex) {
                if (stop) {
                    LOG.log(Level.INFO, "Thread terminated", ex);
                } else {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public void stopThread() {
        stop = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

}
