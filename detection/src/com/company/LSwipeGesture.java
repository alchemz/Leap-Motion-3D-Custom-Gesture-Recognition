/*NOTES REGARDING LEFT SWIPE
* This is pretty much a clone of RSwipeGesture, with opposite directional conditions
* Implementation notes and comments will be made in the RSwipeGesture class
* */

package com.company;

import com.leapmotion.leap.*;


/**
 * Created by matt.raporte on 11/25/2015.
 */


public class LSwipeGesture extends CustomGesture{

    int handID;
    float xPos;


    public boolean test0(Frame frame){
        Hand hand = frame.hands().get(0);
        xPos = hand.palmPosition().getX();
        if(xPos<0){
            return false;
        }

        if(hand.grabStrength()>.1){
            return false;
        }
        /*
        for(Finger finger : hand.fingers()){
            if(finger.isExtended()==false){
                return false;
            }
        }
        */

        handID = hand.id();
        return true;
    }

    public boolean test1(Frame frame){
        Hand hand = frame.hands().get(0);
        float newX = hand.palmPosition().getX();

        if(newX > xPos - 20.0){ //inverse of RSwipe's (newX < xPos +20.0)
            return false;
        }

        if(handID!=hand.id()){
            detectCount--;
            return false;
        }

        if(hand.grabStrength()>.1){
            return false;
        }

        xPos = newX;

        return true;
    }


    public boolean test2(Frame frame){
        Hand hand = frame.hands().get(0);
        float newX = hand.palmPosition().getX();
        if(newX > xPos -20.0){ //inverse of RSwipe's (newX < xPos +10.0)
            return false;
        }

        if(handID!=hand.id()){
            detectCount=0;
            return false;
        }

        if(hand.grabStrength()>.1){
            return false;
        }

        return true;
    }


}
