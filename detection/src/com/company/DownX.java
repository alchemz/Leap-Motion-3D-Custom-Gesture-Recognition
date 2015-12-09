/*NOTES REGARDING UPX
* -reverse of UpX, notes regarding both will be in the UpX file
* */

package com.company;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;

/**
 * Created by matt.raporte on 11/30/2015.
 */
public class DownX extends CustomGesture{

    int downBy;
    float y_pos;


    public boolean test0(Frame frame){
        Hand hand = frame.hands().get(0);

        if(hand.palmVelocity().getY()>0){
            return false;
        }

        downBy= hand.fingers().extended().count();

        if((downBy == 0)||(downBy==5)){
            return false;
        }

        y_pos = hand.palmPosition().getY();

        return true;
    }

    public boolean test1(Frame frame){
        Hand hand = frame.hands().get(0);
        float new_y_pos = hand.palmPosition().getY();

        if (y_pos-20.0 < new_y_pos){ //inverse of UpX's (y_pos+20.0 > new_y_pos) condition
            return false;
        }

        if(hand.fingers().extended().count()!=downBy){
            return false;
        }

        y_pos= new_y_pos;

        return true;
    }


    public boolean test2(Frame frame){
        Hand hand = frame.hands().get(0);
        float new_y_pos = hand.palmPosition().getY();

        if (y_pos-10.0 < new_y_pos){ //inverse of UpX's (y_pos+10.0 > new_y_pos) condition
            return false;
        }

        if(hand.fingers().extended().count()!=downBy){
            return false;
        }

        return true;
    }


}
