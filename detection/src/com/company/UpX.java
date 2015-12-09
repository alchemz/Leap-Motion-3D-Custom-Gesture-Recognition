/*NOTES REGARDING UPX
* -needs testing
* -check hand id frame to frame to make sure it doesnt leave frame?
*
*
* */

package com.company;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;

/**
 * Created by matt.raporte on 11/29/2015.
 */

public class UpX extends CustomGesture {

    int upBy;
    float y_pos;

    /*test0 - get number of fingers and use y-velocity to distinguish UpX vs DownX*/
    public boolean test0(Frame frame){
        Hand hand = frame.hands().get(0);

        //test grab strength to distinguish from boom? how does number of fingers affect grab strength?

        //first test to make sure y velocity is positive to distinguish from DownX (common case)
        if(hand.palmVelocity().getY()<0){
            return false;
        }

        //get the number of fingers extended and save it
        upBy= hand.fingers().extended().count();
        //no point in FWD-0'ing.. also no point FWD-1'ing but we'll allow it
        if((upBy == 0)||(upBy==5)){
            return false;
        }

        //passed test0, store palm y position (number of fingers is already stored)
        y_pos = hand.palmPosition().getY();

        //System.out.println("UpX: "+upBy);
        //cooldown(1000);

        return true;
    }

    public boolean test1(Frame frame){
        Hand hand = frame.hands().get(0);
        float new_y_pos = hand.palmPosition().getY();

        //first test new y position vs old one to ensure hand has moved up by at least 20 mm - test the 20mm and adjust if its too much
        if (y_pos+20.0 > new_y_pos){
            return false;
        }

        //next test the number of fingers is still the same
        if(hand.fingers().extended().count()!=upBy){
            return false;
        }

        //store new y_pos before passing test
        y_pos= new_y_pos;

        return true;
    }


    /*essentially the same as test1*/
    public boolean test2(Frame frame){
        Hand hand = frame.hands().get(0);
        float new_y_pos = hand.palmPosition().getY();

        if (y_pos+10.0 > new_y_pos){
            return false;
        }

        if(hand.fingers().extended().count()!=upBy){
            return false;
        }

        return true;
    }

}
