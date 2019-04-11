package screenAlike;
import static com.example.screenalike.ScreenAlike.getAppData;

final class ImageDispatcher {
    private final Object mLock = new Object();
    private JpegStreamerThread mJpegStreamerThread;
    private volatile boolean isThreadRunning;

    private class JpegStreamerThread extends Thread {
        private byte[] mCurrentJpeg;
        private byte[] mLastJpeg;
        private int mSleepCount;

        JpegStreamerThread() {
            super(JpegStreamerThread.class.getSimpleName());
        }

        public void run() {

            while (!isInterrupted()) {
                if (!isThreadRunning) break;
                mCurrentJpeg = getAppData().getImageQueue().poll();
                if (mCurrentJpeg == null) {
                    try {
                        sleep(24);
                    } catch (InterruptedException ignore) {
                        continue;
                    }
                    mSleepCount++;
                    if (mSleepCount >= 20) sendLastJPEGToClients();
                } else {
                    mLastJpeg = mCurrentJpeg;
                    getAppData().getSocketConnection().emit("imageStream", getAppData().getTwitchID(),mLastJpeg);
                }
                }
            }
        }

        private void sendLastJPEGToClients() {
//            mSleepCount = 0;
//            synchronized (mLock) {
//                if (!isThreadRunning) return;
//                for (final Client currentClient : getAppData().getClientQueue()) {
//                    currentClient.sendClientData(Client.CLIENT_IMAGE, mLastJpeg, false);
//                }
//            }
//        }
    }

    void start() {
        synchronized (mLock) {
            if (isThreadRunning) return;
            mJpegStreamerThread = new JpegStreamerThread();
            mJpegStreamerThread.start();
            isThreadRunning = true;
        }
    }

    void stop() {
        synchronized (mLock) {
            if (!isThreadRunning) return;
            isThreadRunning = false;
            mJpegStreamerThread.interrupt();
        }
    }
}