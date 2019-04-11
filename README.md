# WhammychatScreenStreamIntegration
This is an Android App which uses native screen mirroring paradigm to stream a screen. 

We have used **Socket.io** library on Java, Node as well as HTML,JS for the connection and communication.

## Instructions for integrating this functionality in your application
The folder screenAlike as a whole needs to be pasted.(rename according to your liking)

Containing files:

1. **Appdata.java**: Contains all the getter and setter functions

2. **ForgroundServiceHandler.java**: Contains cases for handling streaming.

3. **ImageGenerator.java**: Creates the images to be sent to the server.

4. **ImageDispatcher.java**: Sends the image which was created using ImageGenerator.java



**ScreenAlike.java**:  *Default file to be called when application starts. This would be your application_name.java file.*

*Every method needs to be copied and relevant lines from onCreate()*

**MainActivity.java**:  *Every method needs to be copied and relevant lines from onCreate()*

onPause() method detects every time control goes out of application.

The following line of code is used to Stop Streaming. Can be used according to your requirement:
```
mForegroundServiceTaskHandler.obtainMessage(ForegroundServiceHandler.HANDLER_STOP_STREAMING).sendToTarget()//Stops creating images
getAppData().stopDispatcher(); // Stops dispatching images
getAppData().disconnectSocket(); //disconnects the connected socket
```

The following line of code is used to Start Streaming from initial stage:
```
final MediaProjectionManager projectionManager = getProjectionManager();
if (projectionManager != null) {
	startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE); //Starts creating images
}
```

```
getAppData().createConnection(); // creates and connects to the socket
getAppData().startDispatcher(); // Stops dispatching images
```

All the required code needs to be copied from res folder

