package com.robyrodriguez.stackbuster;

import java.util.Date;

import com.robyrodriguez.stackbuster.transfer.StackClient;

/**
 * App launcher
 */
public class App {

    public static void main(String args[]) throws Exception {
        StackClient client = new StackClient();

        while (true) {
            int kill = Runtime.getRuntime().exec("sudo pkill tor").waitFor(),
                    start = Runtime.getRuntime().exec("sudo service tor start").waitFor();

            if (kill == 0 && start == 0) {
                boolean success = client.incrementCounter("https://stackoverflow.com/q/25649725/5173530");
                if (success) {
                    System.out.println("Made request at " + new Date());
                } else {
                    System.out.println("Error encountered");
                }
            }
        }
    }
}
