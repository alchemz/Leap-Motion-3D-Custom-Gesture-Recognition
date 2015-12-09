package com.company;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

import javax.swing.*;
import java.io.UnsupportedEncodingException;

/**
 * Created by matt.raporte on 11/30/2015.
 */
public class ActionQueue {

    private static ActionQueue actionQueue = null;

    private Queue queue;

    private ActionQueue() {
        try {
            queue = new Queue("DIRECT=OS:.\\private$\\SeniorProject");
        } catch (MessageQueueException e) {
            System.out.println("ERROR WITH QUEUE!");
            e.printStackTrace();
        }
    }

    public static ActionQueue get() {
        if (actionQueue == null) {
            actionQueue = new ActionQueue();
        }
        return actionQueue;
    };

    public void sendCommand(String command) {
        try {
            String date = String.format("%d", System.currentTimeMillis());
            Message msg = new Message(date, command, "L:None");
            queue.send(msg);
        } catch (MessageQueueException | UnsupportedEncodingException e) {
            System.out.println("ERROR WITH QUEUE!");
            e.printStackTrace();
        }
    }
}
