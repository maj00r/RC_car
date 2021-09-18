package com.example.rc_car;

public class Controller {


    public void setmTurnValue(int mTurnValue) {
        this.mTurnValue = mTurnValue;
    }

    public void setmDriveObeyValue(char mDriveObeyValue) {
        this.mDriveObeyValue = mDriveObeyValue;
    }

    public void setmThrottleValue(int mThrottleValue) {
        this.mThrottleValue = mThrottleValue;
    }

    private int mTurnValue = 0;
    private char mDriveObeyValue = 'N';
    private int mThrottleValue = 0;
    private Thread mSenderThread;
    private UDPHandler mUdpSender;

    public void startServer(String ip, int port, int updateDelay){
        mUdpSender = new UDPHandler(ip, port, updateDelay);
        mSenderThread = new Thread(mUdpSender);
        mSenderThread.start();
    }

    public void updateControlState(){
        // angle;obey;throttle
        // angle [-100 ; 100]
        // obey {N, R, D}
        // throttle [0 ; 100]
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(mTurnValue);
        stringBuilder.append(';');
        stringBuilder.append(mDriveObeyValue);
        stringBuilder.append(';');
        stringBuilder.append(mThrottleValue);
        if (mUdpSender != null){
            mUdpSender.setmMessage(stringBuilder.toString());
        }

    }
    public void stopServer(){
        mSenderThread.interrupt();
    }
}
