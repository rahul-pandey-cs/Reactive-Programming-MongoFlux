package com.sap.reactive.demo.subscriber.playground;

import java.util.Arrays;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

public class CustomSubscriberBackPressure {

  /*
   * Helper method to check if Prime
   */
  public static boolean isPrime(int n) {
    if (n <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

  /*
   * Helper method to return odd and even
   */
  public static String returnOddEven(int n) {
    if (n % 2 == 0) {
      return "Even";
    }
    return "Odd";
  }

  /**
   * Custom subscriber with back pressure. Here we can see all the channels, data (onNext) , Error
   * and complete channel User can implement their own implementations over this
   */
  public void customSubscribeWithBackPressure() {

    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    Flux<Integer> numberFeed = Flux.fromIterable(numbers)
        .log();

    numberFeed.subscribe(new Subscriber<Integer>() {

      Subscription s;
      int countLimit = 0;
      int totalLimit = 0;

      @Override
      public void onSubscribe(Subscription s) {
        // TODO Auto-generated method stub
        this.s = s;
        s.request(2);
      }

      @Override
      public void onNext(Integer t) {
        // TODO Auto-generated method stub

        this.countLimit++;
        this.totalLimit ++;

        if (isPrime(t)) {
          System.out.println("I am prime " + t);
        }

        if(this.totalLimit ==6){
          this.s.cancel();
        }

        if(this.countLimit ==2){
          this.countLimit =0;
          this.s.request(2);
        }
       
      }

      @Override
      public void onError(Throwable t) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onComplete() {
        // TODO Auto-generated method stub

      }

    });

  }


  /*
   * Main method with invocations
   */
  public static void main(String[] args) {

    CustomSubscriberBackPressure s = new CustomSubscriberBackPressure();
    s.customSubscribeWithBackPressure();



  }


}
