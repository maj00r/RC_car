package com.example.rc_car;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

public class UDPHandler implements Runnable {
    private String mMessage = "test";
    private String mIP;
    private int mPort;
    private int updateDelay;

    public UDPHandler( String mIP, int mPort, int updateDelay) {
        this.mIP = mIP;
        this.mPort = mPort;
        this.updateDelay = updateDelay;
    }
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @Override
    public void run() {
        DatagramSocket serverSocket;
        try {
            serverSocket = new DatagramSocket();
            while (!Thread.currentThread().isInterrupted())
            {
                try{
                    byte[] dataToSend = mMessage.getBytes();
                    DatagramPacket packetToSend = new DatagramPacket(dataToSend,
                            dataToSend.length,
                            InetAddress.getByName(mIP),
                            mPort);
                    serverSocket.send(packetToSend);
                    Thread.sleep(updateDelay);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                catch (IOException ignored) { }
            }
            serverSocket.close();

        } catch (SocketException ignored) { }

    }

}

