package com.sap.reactive.demo.subscriber.playground;

import java.util.Arrays;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class CustomSubscriber {

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
     * Default subscriber. Here we are looking at the data channel
     */
    /*
     * public void defaultSubscribe() {
     *
     * List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8);
     *
     * Flux<Integer> numberFeed = Flux.fromIterable(numbers) .log() .map(num -> num*2);
     *
     * numberFeed.subscribe(System.out::println);
     *
     * }
     */


    /**
     * Custom subscriber. Here we can see all the channels, data (onNext) , Error and complete channel
     * User can implement their own implementations over this
     */
    public void customSubscribeOnNext() {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        Flux<Integer> numberFeed = Flux.fromIterable(numbers)
                .log();

        // Implement your own subscriber
        numberFeed.subscribe(new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                // TODO Auto-generated method stub
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer t) {
                // TODO Auto-generated method stub
                if (isPrime(t)) {
                    System.out.println("I am prime " + t);
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

        CustomSubscriber s = new CustomSubscriber();
        s.customSubscribeOnNext();


    }


}
