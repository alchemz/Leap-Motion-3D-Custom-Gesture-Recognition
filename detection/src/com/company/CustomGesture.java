/*NOTES REGARDING CUSTOM GESTURE/ THIS ARCHITECTURE (WHY I'M SO GREAT)
* -Easy to implement new gesture classes
* -Easy to implement more robust gestures by adding tests and having more basic classes simply advance detectCount through empty tests
* -Facilitates testing individual stages of each gesture
* -Provides a protocol for testing order in Main.onFrame()
* */


package com.company;

//import com.leapmotion.leap.*;


import com.leapmotion.leap.Frame;

/**
 * Created by matt.raporte on 10/29/2015.
 */



/*abstract superclass for creating custom Gesture subclasses*/
public abstract class CustomGesture {
    public int detectCount; //detectCount tracks how many stages of the gesture have been chronologically detected
    public int frameCount; //keeps track of frames since last test passed

    public int frameReset = 30; //max number of frames since last test passed before a detectCount reset. Will be lowered as more midpoint tests are included. Keep in mind fps is between 40-110

    //public Frame lastFrame; //is this better than using multithreaded callbacks? is this better than saving gesture-specific relevant info such as normal vectors

    /*constructor*/
    public CustomGesture(){
        detectCount = 0;
        frameCount = 0;
    }
    /*cooldown method from Main, for use during testing*/
    public void cooldown(int time){
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //System.out.println("listening..");
    }
    /*
    detectGesture():
    *   Is called for each gesture, by each onFrame iteration.
    *
    *   uses gesture's current detectCount value to determine which test to run
    *   adjusts detectCount according to test results of the frame
    *   returns true when the last test of a gesture is passed
    *
    *   resets detectCount to 0 if the next test hasn't been passed in a certain number of frames
    *   */
    public boolean detectGesture(Frame frame){
        assert((0<=detectCount)&&(detectCount<=2)); //asserts detectCount is in possible range
        assert(frame.isValid()); //asserts the frame passed is a valid frame

        frameCount++; //with each test-call we increment the frameCount

        detectCount = frameCount>frameReset ? 0 : detectCount; //resets detectCount when frameCount passes frameReset threshold

        switch(detectCount) { //switch to prepare for additional midpoint tests
            case 0:
                detectCount = test0(frame) ? 1 : detectCount; //increments detectCount if test is passed
                frameCount = (detectCount==1) ? 0 : frameCount; //resets frameCount if detectCount was just incremented
                return false; //return false regardless
            case 1:
                detectCount = test1(frame) ? 2 : detectCount;
                frameCount = (detectCount==2) ? 0: frameCount;
                return false;
            case 2:
                detectCount = test2(frame) ? 0 : detectCount;
                frameCount = (detectCount==0) ? 0 : frameCount;
                return (detectCount==0);
            default:
                System.err.println("CustomGesture:detectGesture(): assert circumvention - possible multithreading issue");
                System.exit(1);
        }

        System.err.println("CustomGesture:detectGesture(): switch default circumvention - possible logical errors");
        System.exit(1);
        return false; //because the compiler wouldn't let me exclude a return message..

    }

    /*
    abstract testX methods:
    *   1 is called with each iteration of detectGesture() based on current detectCount
    *   are overridden by each inheriting Gesture class in order to test conditions specific to each gesture
    *   more midpoint test methods may be added later
    * */

    /*abstract test0: tests the start condition of a gesture*/
    abstract public boolean test0(Frame frame);
    /*abstract test1: tests the midpoint condition of a gesture*/
    abstract public boolean test1(Frame frame);
    /*abstract test2: tests the final condition of a gesture*/
    abstract public boolean test2(Frame frame);


}
