/*NOTES REGARDING CLAP GESTURE
* -is working accurately and efficiently
* -one thing we can't improve is the delay in Leap recognizing a second hand
*     ..so when testing hover your hands over the controller for a second before clapping
*
* */


package com.company;

import java.lang.Math;
import com.leapmotion.leap.*;


/**
 * Created by matt.raporte on 11/17/2015.
 */

/*Subclass of CustomGesture, defines progression of tests used to detect a gesture where the user claps 2 hands*/
public class ClapGesture extends CustomGesture {

    public int frameReset = 200; //ovverrides CustomGesture's frameReset because it's largely unneeded in ClapGesture

    long startTime=0; //used for testing method execution speed
    long endTime=0;

    //These frame datapoints were scrapped, sacrificing detection accuracy for speed
    //int last_lh_id, last_rh_id; //last left hand and right hand id numbers
    //float last_lh_ppx, last_rh_ppx; //last left hand and fight hand palm positions x coordinates
    float handDistance; //the distance between 2 palms
    int id1, id2; //hand id's
    Hand firstHand, secondHand;

    /*test0: tests the start condition of a gesture by testing a series of disqualifying conditions
    *   fail conditions: (tested in order of fastest to check to slowest)
    *       X) UPDATE: no need to test this, it's tested in the onFrame() method
    *       2) sum of both hands grab strength exceeds .5
    *       3) the x vector coordinates for the 2 hands aren't within .4 radians of each other
    *
    *   */
    public boolean test0(Frame frame){
        //startTime = System.nanoTime(); //used in testing each gesture's sub-test speeds
        HandList hands = frame.hands();

        /*
        //1 - now redundant because ClapGesture is only being tested iff there are 2 hands in the handList
        if(hands.count()!=2){
            //System.out.println("test0(): failed hand number test");
            return false;
        }
        */

        //2 - decided to stop using hands.leftmost() and right most, instead calculate elements thru combined values
        firstHand = hands.get(0);
        secondHand = hands.get(1);

        if (firstHand.grabStrength() + secondHand.grabStrength() > .5) {
            //System.out.println("test0(): grab strength test");
            return false;
        }

        //3

        //has to be created to avoid API calls since we don't know which hand is which
        float pitchDif = Math.abs( firstHand.direction().pitch()) - Math.abs(secondHand.direction().pitch());

        if ((pitchDif<-.4)||pitchDif > .4 ){
            //System.out.println("test0(): failed pitch test");
            return false;
        }


        /*frame passed all test0() conditions*/


        /*store relevant frame info from current frame for use in test1*/
        handDistance = firstHand.palmPosition().distanceTo(secondHand.palmPosition());
        id1 = firstHand.id();
        id2 = secondHand.id();

        //last_lh_id = firstHand.id();
        //last_rh_id = secondHand.id();
        //last_lh_ppx = left.palmPosition().getX();
        //last_rh_ppx = right.palmPosition().getX();


        /*used during testing*/
        //endTime = System.nanoTime();
        //System.out.println("test0 passed!\tframe id: "+ frame.id()+ "\ttime: " +(endTime-startTime)+" nanoseconds");

        return true;
    }
    /*test1: tests a midpoint condition of a gesture by testing a series of disqualifying conditions
    *   fail conditions: (tested in order of fastest to check to slowest)
    *   X) UPDATE: no need to test this, it's tested in onFrame()
    *   2) hands have left the frame since test0 was passed and been assigned different ids
    *   3) distance between hands > last frames distance
    *   */
    public boolean test1(Frame frame){
        //startTime = System.nanoTime();
        HandList hands = frame.hands();
    /*
        //1
        if(hands.count()!=2){
            //System.out.println("test1(): failed hand number test");
            return false;
        }
    */
        //2
        firstHand = hands.get(0);
        secondHand = hands.get(1);
        int id1_current = firstHand.id();
        int id2_current = secondHand.id();
        if((id1_current!= id1)||(id2_current!=id2)) {
            //System.out.println("test1(): failed id test");
            return false;
        }
        //3
        float newHandDistance = firstHand.palmPosition().distanceTo(secondHand.palmPosition());
        if(newHandDistance>handDistance) {
            handDistance = newHandDistance;
            return false;
        }


        /*frame passed all test1() conditions*/


        /*store relevant frame info from current frame for use in test1*/
        handDistance = newHandDistance;

        /*used during testing*/
        //endTime = System.nanoTime();
        //System.out.println("test1 passed!\tframe id: "+ frame.id()+ "\ttime: " +(endTime-startTime)+" nanoseconds");


        return true;
    }

    /*test2: tests the final condition of a gesture by testing a series of disqualifying conditions
    *   fail conditions: (tested in order of fastest to check to slowest)
    *       1) Either hand has left the FOV
    *       2) new hand distance > old hand distance
    *       3) hand distance > 60
    *   */
    public boolean test2(Frame frame){
        HandList hands = frame.hands();
        //1
        firstHand = hands.get(0);
        secondHand = hands.get(1);
        int id1_current = firstHand.id();
        int id2_current = secondHand.id();
        if((id1_current!= id1)||(id2_current!=id2)) {
            //System.out.println("test1(): failed id test");
            return false;
        }
        //2
        float newHandDistance = firstHand.palmPosition().distanceTo(secondHand.palmPosition());
        if(newHandDistance>handDistance) {
            handDistance = newHandDistance;
            return false;
        }
        //3
        if (newHandDistance>60){
            handDistance = newHandDistance;
            return false;
        }


        /*frame passed all test2() conditions*/

        return true;
    }

}
