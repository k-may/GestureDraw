GestureDraw Application
GestureTek

Installation
============

Application only needs to be unzipped. If the tracks specified exist outside of the main application package (ex. My music, Documents, etc.) make sure to include the path in the configuration file.

Configuration File
==================

All configuration is done via an XML file ("config.xml") located in the bin folder of the main application package. This file contains absolute paths for tracks and specifies interaction region ("mouse", "gesttracker" or "soni"), as well as other fields relevant to the application which will be revised and added over future releases.

Logging
=======

There exists both an error and logging text file in the logs folder of the main application package. These should be checked in the application fails and may provide insight into a broken path or input error.

Input Regions
=============

The application was designed to use multiple frameworks including GestTracker 3D Hand tracker, and The Simple Open NI framework for Processing. The active framework should be defined in the configuration file for 'input'.


GestTracker 3D Hand Tracker Region

This region uses normalized values sent by hand tracker using the gestOSC Sender application. Both the GestTrack3DHand tracker and the sender applications must be installed and running for this region to work. For the tracker, make sure you've the security key plugged in to access the camera. For the GestOSC Sender, select the box for "Send normalized coordinates". Currently the region covers up to 3 hands but further testing is required. If the Simple OpenNI framework was previously installed you may find the tracker won't be able to find the data stream of the Kinect camera. Un/install the Kinect API drivers to switch between regions.

The hand data being sent does not identify users, and there is no way to explicitly set which or how many users get registered. Because of this it's sometimes difficult to map the hand to the user and can create a confusing experience. Also, the tracker tends to loose registration more when the hand passes in front on the user's body and the user must be standing for the tracking to work. The camera can be quite sensitive to light conditions, which can be optimized using the "Advanced" options via the tracker interface.


Simple OpenNI

This framework requires that Microsoft Kinect SDK (v 1.8) be installed: http://www.microsoft.com/en-us/kinectforwindowsdev/Downloads.aspx 

To register a hand the framework requires a gesture from the user. Simply waving one's hand should do the trick. User's will probably find the Simple OpenNI framework easier to use and a better experience.


Mouse

This is the default region for the application, and provides no multi-user, only the used for debugging the application.


Debug
=====

SimpleOpenNI

For testing purposes use the 'Hand' Processing application in the debug folder. The application will output both the depthstream image and will track a users hand (inititated with the wave gesture). Alternatively, there are a few tools suplied with the Kinect SDK, such as the KinectStudio application which will allow you to record and execute gestures while testing the application.


GestTracker

Use the advanced options via the tracker.


Exit
====

Pressing the 'esc' key or clicking with the mouse will terminate the application