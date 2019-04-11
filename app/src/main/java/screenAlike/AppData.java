package screenAlike;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.example.screenalike.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentLinkedDeque;
public final class AppData {
    private final WindowManager mWindowManager;
    private static final String DEFAULT_JPEG_QUALITY = "80";
    private final int mDensityDpi;
    private final float mScale;
    private ImageDispatcher mImageDispatcher;
    private final ConcurrentLinkedDeque<byte[]> mImageQueue = new ConcurrentLinkedDeque<>();
    private volatile boolean isStreamRunning;
    private final Context mContext;
    private volatile int mJpegQuality;
    private final SharedPreferences mSharedPreferences;
    private Socket mSocket;
    public AppData(final Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDensityDpi = getDensityDpi();
        mScale = getScale(context);
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mJpegQuality = Integer.parseInt(mSharedPreferences.getString(mContext.getString(R.string.pref_key_jpeg_quality), DEFAULT_JPEG_QUALITY));
    }

    public void setStreamRunning(final boolean streamRunning) {
        isStreamRunning = streamRunning;
    }

    public ConcurrentLinkedDeque<byte[]> getImageQueue() {
        return mImageQueue;
    }

    public boolean isStreamRunning() {
        return isStreamRunning;
    }

    public WindowManager getWindowsManager() {
        return mWindowManager;
    }

    public int getScreenDensity() {
        return mDensityDpi;
    }

    public float getDisplayScale() {
        return mScale;
    }
    public int getJpegQuality() {
        return mJpegQuality;
    }
    public Point getScreenSize() {
        final Point screenSize = new Point();
        mWindowManager.getDefaultDisplay().getRealSize(screenSize);
        return screenSize;
    }

    public void createConnection(){
        try{
            mSocket = IO.socket("https://comparepdftesseeract.herokuapp.com/"); //AWS Public Server Address
    } catch (
    URISyntaxException e) {}
            mSocket.connect();
    }

    public  String getTwitchID(){
        return "TWITCH_CLIENT_ID";

    }

    public Socket getSocketConnection()
    {
        return mSocket;
    }
    public void disconnectSocket()
    {
        mSocket.emit("forceDisconnect",getTwitchID());
    }
    public void startDispatcher()
    {
        mImageDispatcher = new ImageDispatcher();
        mImageDispatcher.start();
    }

    public void stopDispatcher()
    {
        mImageDispatcher.stop();
        mImageDispatcher = null;
    }

    private int getDensityDpi() {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    private float getScale(final Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

}
