package com.company;

//import java.io.IOException;
import java.io.*;
import static java.lang.Math.abs;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Controller;
import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

import java.util.ArrayList;
import java.util.List;

class SampleListener extends Listener {

    private static final String RIGHT_SWIPE = "RIGHT_SWIPE";
    private static final String LEFT_SWIPE = "LEFT_SWIPE";
    private static final String BOOM = "BOOM";
    private static final String CLAP = "CLAP";



    /*vars used in testing*/
    int testnum = 4;
    boolean printmsg = false; //controls inititial querry to perform a gesture
    public boolean testing = false; //controls if its a test or not

    final static String testpath = "tst\\test1.txt"; //stores test storage file path
    List<Frame> frames = new ArrayList<Frame>(100); //used to store each frame during a test run

    /*other vars*/
    HandList handsInFrame;
    Frame frame;

    /*instantiate custom gestures*/
    ClapGesture clap = new ClapGesture();
    BoomGesture boom = new BoomGesture();
    RSwipeGesture rSwipe = new RSwipeGesture();
    LSwipeGesture lSwipe = new LSwipeGesture();
    UpX upX = new UpX();
    DownX downX = new DownX();

/*used to break down frame list into relevant components and store to file during test*/
    public void write(){
        File testOut = new File(testpath);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(testOut, true)));
            out.println("test: "+testnum++);
            //write to file here
            for(Frame frame : frames){
                if(frame.hands().count()==2) {
                    long id = frame.id();
                    float rhp = frame.hands().rightmost().palmPosition().getX();
                    float lhp = frame.hands().leftmost().palmPosition().getX();
                    float rhv = frame.hands().rightmost().palmVelocity().getX();
                    float lhv = frame.hands().leftmost().palmVelocity().getX();
                    String dataString = Long.toString(id) +"\t"+ Float.toString(rhp)+"\t" + Float.toString(lhp) +"\t"+ Float.toString(rhv) +"\t"+ Float.toString(lhv);
                    out.println(dataString);
                }
            }
            out.println();
            out.println();
            out.close();
            System.out.println("write succesefull");
            return;
        } catch (IOException e) {
            System.out.println("Main:write() - couldn't write to file, check path(?)");
            return;
        }

    }

/*actions performed initialization of listener*/
    public void onInit(Controller controller) {
        if(testing){
            System.out.println("Initialized testing");
        }
        else {
            System.out.println("Initialized gesture recognition");
        }
    }

/*cooldown used after a gesture has been completed*/
    public void cooldown(int time){
        try {
            Thread.sleep(time * 2);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("cooldown over..");
    }

/*actions performed when LM hardware is detected, used to instantiate customized gesture classes*/
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        //Commented out during original gesture implementation
        /*
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        */
    }

/*actions performed if LM hardware is disconnected*/
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

/*actions performed when listener is exited*/
    public void onExit(Controller controller) {
        if(testing){
            write();
        }
        System.out.println("Exited");
    }

/*actions performed for each frame the listener */
    public void onFrame(Controller controller) {

        //fetch current frame and handlist using API
        frame = controller.frame();
        handsInFrame = frame.hands();


        /*Handles onFrame() when run in test mode*/
        if((testing)&&(frame.isValid()) && (!handsInFrame.isEmpty())) {
            if(!printmsg){
                System.out.println("Perform Gesture now");
                printmsg = true;
                cooldown(500);
            }
            else {
                frames.add(controller.frame());
            }
        }
        /*Handles gesture detection*/
        else {
            /*testing*/
            //System.out.println(handsInFrame.get(0).palmNormal());
            //cooldown(1000);
            //System.out.println("Fingers detected: " +handsInFrame.get(0).fingers().count() );



            /*Call custom gesture classes to check for gestures if hand is found/tracking data exists
            in a logical order. The exception to this being if 2 hands are in the frame, since
            only the ClapGesture uses 2 hands
            * */
            if (frame.isValid() && (!handsInFrame.isEmpty())) {
                if (handsInFrame.count() == 2) {
                    if(clap.detectGesture (frame)==true){
                        System.out.println("Clap detected");
                        ActionQueue.get().sendCommand(CLAP);
                        cooldown(500);
                    }
                }
                else {
                    //if boom.detectCount > 0, boom should be tested first (because no other gesture should pass BoomGesture test0
                    if(boom.detectCount>0) {
                        if (boom.detectGesture(frame) == true) {
                            System.out.println("Boom detected");
                            ActionQueue.get().sendCommand(BOOM);
                            cooldown(500);
                        }
                    }
                    /*implement other CustomGesture checks here
                    *
                    * thoughts on order.. should more common gestures go first, or ones with higher detect counts?
                    *
                    * */
                    else{
                        /*testing for swipes. only tests swipes if abs of palm normal Y <.7 */
                        if(abs(handsInFrame.get(0).palmNormal().getY()) < .7) {
                            if (rSwipe.detectCount >= lSwipe.detectCount) { //>= used because rswipe is more common that lswipe
                                //test rswipe first
                                if (rSwipe.detectGesture(frame) == true) {
                                    System.out.println("Right Swipe detected");
                                    ActionQueue.get().sendCommand(RIGHT_SWIPE);
                                    cooldown(500);
                                }
                                //test lswipe second
                                if (lSwipe.detectGesture(frame) == true) {
                                    System.out.println("Left Swipe detected");
                                    ActionQueue.get().sendCommand(LEFT_SWIPE);
                                    cooldown(500);
                                }
                            } else {
                                //test lswipe first
                                if (lSwipe.detectGesture(frame) == true) {
                                    System.out.println("Left Swipe detected");
                                    ActionQueue.get().sendCommand(LEFT_SWIPE);
                                    cooldown(500);
                                }
                                //test rswipe second
                                if (rSwipe.detectGesture(frame) == true) {
                                    System.out.println("Right Swipe detected");
                                    ActionQueue.get().sendCommand(RIGHT_SWIPE);
                                    cooldown(500);
                                }
                            }
                        }
                        /*else if (hand is parallel to device) palmNormal().getY is < -.5 or so:
                        * test the new fwd/bwd - X (hold up x fingers and move them up or down)
                        * test boom test0() last
                        * */
                        else if(handsInFrame.get(0).palmNormal().getY()<-.5) {
                            /*test downX and upX prioritized by current detectCounts*/
                            if(upX.detectCount>downX.detectCount) {
                                if (upX.detectGesture(frame) == true) {
                                    System.out.println("Up(" + upX.upBy + ") detected");
                                    for (int i = 0; i < upX.upBy; i++) {
                                        ActionQueue.get().sendCommand(RIGHT_SWIPE);
                                    }
                                    cooldown(500);
                                }
                                if (downX.detectGesture(frame) == true) {
                                    System.out.println("Down(" + downX.downBy + ") detected");
                                    for (int i = 0; i < downX.downBy; i++) {
                                        ActionQueue.get().sendCommand(LEFT_SWIPE);
                                    }
                                    cooldown(500);
                                }
                            }
                            else{
                                if (downX.detectGesture(frame) == true) {
                                    System.out.println("Down(" + downX.downBy + ") detected");
                                    for (int i = 0; i < downX.downBy; i++) {
                                        ActionQueue.get().sendCommand(LEFT_SWIPE);
                                    }
                                    cooldown(500);
                                }
                                if (upX.detectGesture(frame) == true) {
                                    System.out.println("Up(" + upX.upBy + ") detected");
                                    for (int i = 0; i < upX.upBy; i++) {
                                        ActionQueue.get().sendCommand(RIGHT_SWIPE);
                                    }
                                    cooldown(500);
                                }
                            }

                            /*testing boom test0() (boom test1 and test2 were caught in an earlier block*/
                            boom.detectGesture(frame);
                        }
                    }

                }


            }//end valid+nonempty frame
        }//end !testcase case
    }//end onFrame() method

}

public class Main {

    public static void main(String[] args) {
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        //SampleListner uses onFrame to fetch each new frame
        //it then uses the frame gestures() method to fetch a list of ongoing library gestures, and responds to them
        Controller controller = new Controller();

        controller.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);

    }
}



