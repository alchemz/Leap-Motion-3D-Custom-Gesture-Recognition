/*NOTES REGARDING RIGHT SWIPE
*-https://community.leapmotion.com/t/testing-if-each-finger-is-extended-vs-testing-grab-strength/4404
* ...need to test efficiency of grabstrength vs finger.isExtended(), test0 currently implements both
* ...and the following tests only implement grab strength, will adjust after accuracy/efficiency testing
*
* -RSwipeGesture and LSwipeGesture need to be tested (in main) in order of highest detectCount
* -RSwipeGesture and LSwipeGesture are extremely common cases and need to be prioritized in efficiency and accuracy
*
* -RSwipe and LSwipe need to test for position normals, to make sure hand is somewhat perpendicular to controller
* */

package com.company;

import com.leapmotion.leap.*;


/**
 * Created by matt.raporte on 11/25/2015.
 */


public class RSwipeGesture extends CustomGesture{

    int handID;
    float xPos;


    public boolean test0(Frame frame){
        Hand hand = frame.hands().get(0);
        //test x hand position first, should be at least negative
        xPos = hand.palmPosition().getX();
        if(xPos>0){
            return false;
        }

        //test grab strength
        if(hand.grabStrength()>.1){
            return false;
        }

        /*
        //test each finger to make sure it's extended
        for(Finger finger : hand.fingers()){
            if(finger.isExtended()==false){
                return false;
            }
        }
        */

        //after test passes: store hand id (x pos already stored)
        handID = hand.id();
        //System.out.println("rswipe0 passed");
        return true;
    }

    public boolean test1(Frame frame){
        //test hand id is the same, grab strength, and positive change in x position
        //test change in x first to eliminate common case of left-swipes early
        Hand hand = frame.hands().get(0);
        float newX = hand.palmPosition().getX();
        //System.out.println("r-swipe:test1: old x: "+xPos+"\tnew x: "+newX);
        //cooldown(100);

        if(newX < xPos +20.0){ //should at least advance by 20mm from original position
            return false;
        }
        //System.out.println("rswipe1 xPos test passed");
        //test hand iD.. is this necessary? i
        if(handID!=hand.id()){
            detectCount--; //hand left frame, test for gesture back from test0()
            return false;
        }
        //test grab strength (will replace with finger extension if that proves to be more efficient
        if(hand.grabStrength()>.1){
            return false;
        }
        //test passed- record new x position
        xPos = newX;
        //System.out.println("rswipe1 passed");
        return true;
    }

    //almost exactly the same test as test1
    public boolean test2(Frame frame){
        Hand hand = frame.hands().get(0);
        float newX = hand.palmPosition().getX();
        if(newX < xPos +20.0){ //lowered the change in x threshold from test1
            return false;
        }
        //test hand iD.. is this necessary? i
        if(handID!=hand.id()){
            detectCount=0; //hand left frame, test for gesture back from test0()
            return false;
        }
        //test grab strength (will replace with finger extension if that proves to be more efficient
        if(hand.grabStrength()>.1){
            return false;
        }
        //System.out.println("rswipe2 passed");
        //final test passed, gesture detected
        return true;
    }


}
