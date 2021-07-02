package org.senera.controller;

import android.content.Context;

import org.senera.ControllerService;
import org.senera.SeneraConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by George on 8/1/2018.
 */

public class ControllerImpl extends AbstractController {

    private static final Logger LOG = Logger.getLogger(ControllerImpl.class.getName());

    private ControllerService controllerService;
    private ControllerThread controllerThread;

    public ControllerImpl(Context context, ControllerService controllerService) {
        this.controllerService = controllerService;
        createSocket(SeneraConfiguration.getCONTROL_PORT(context));
    }

    public void stopThread() {
        controllerThread.stopThread();
    }

    private void createSocket(int port) {
        controllerThread = new ControllerThread(port, this);
        Thread t = new Thread(controllerThread);
        t.start();
        LOG.log(Level.INFO, "Controller listening on port {0}", port);
    }

    /**
     * This library method is overwritten for the android so that we can notify the main
     * activity about the progress of the measurements.
     *
     * @param commandSessionId
     * @param message
     */
    @Override
    public void logSensorMessage(long commandSessionId, String message) {
        super.logSensorMessage(commandSessionId, message);
        controllerService.reportStatus(message);
    }
}
