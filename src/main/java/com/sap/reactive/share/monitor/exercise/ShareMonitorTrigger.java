package com.sap.reactive.share.monitor.exercise;

import java.io.IOException;

public class ShareMonitorTrigger {
  
  
  /**
   * 
   * Run this class as a Java Application to trigger your share analysis.
   * @param args you know.
   * @throws IOException An exception you don't have to worry about.
   */
  public static void main(String[] args) throws IOException {
    SAPSharesMonitor shareTracker = new SAPSharesMonitor();
    boolean toInvest = shareTracker.makeInvestmentDecision();
    if (toInvest) {
      System.out.println("Let's go buy some shares!");
    } else {
      System.out.println("Now's not the time.");
    }
  }
}
