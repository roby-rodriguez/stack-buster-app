package com.robyrodriguez.stackbuster;

import com.robyrodriguez.stackbuster.transfer.StackClient;

/**
 * App launcher
 */
public class App {

    public static void main(String args[]) throws Exception {
        StackClient client = new StackClient();
        boolean status = client.incrementCounter("https://stackoverflow.com/q/25649725/5173530");
        System.out.println("Incremented: " + status);
    }
}
