/*NOTES REGARDING BOOM GESTURE
* -is working accurately and efficiently
* -it's a very simple implementation, so be careful not to clench + unclench your fist when demo'ing other gestures
* -Need to include test for normal vector to make sure hand is facing downward in test0()
* -observing false detection of boom when performing a swipe.. include finger extended test?
*
* */

package com.company;

import com.leapmotion.leap.*;

/**
 * Created by matt.raporte on 11/25/2015.
 */
public class BoomGesture extends CustomGesture  {



    /*test0: tests the start condition of a gesture by checking grab strength > .9
    *   */
    public boolean test0(Frame frame){
        //System.out.println("hello from BoomGesture:test0() - Grabstrength = "+frame.hands().get(0).grabStrength());
        Hand hand = frame.hands().get(0);
        if(hand.palmNormal().getY()>-.7){
            return false;
        }

        if(hand.grabStrength()<.9){
            return false;
        }

        return true;
    }

    /*test1: this gesture only needs 2 tests so test1 simply returns true
    *        this demonstrates scalability of the architecture in terms of
    *        defining more complex (20+ test) gestures, alongside simple ones
    * */
    public boolean test1(Frame frame){
        //System.out.println("hello from BoomGesture:test1()"+frame.hands().get(0).grabStrength());
        return true;
    }

    /*test2: tests the final condition, that grab strength < .1
    * */
    public boolean test2(Frame frame){
        //System.out.println("hello from BoomGesture:test2()"+frame.hands().get(0).grabStrength());
        Hand hand = frame.hands().get(0);

        if(hand.grabStrength()>.1){
            return false;
        }

        if(hand.palmNormal().getY()>-.7){
            return false;
        }

        return true;
    }

}
