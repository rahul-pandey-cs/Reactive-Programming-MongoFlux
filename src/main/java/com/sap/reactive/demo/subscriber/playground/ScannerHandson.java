package com.sap.reactive.demo.subscriber.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

public class ScannerHandson {

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

    /*
     * Helper method to convert to string and raise exception
     */
    public static Integer convertStringToNumber(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            throw new RuntimeException("Oops " + num + " is not a number");
        }
    }

    /*
     * Method to read from scanner and
     */
    public List<String> ReadFromScanner() {
        Scanner reader = new Scanner(System.in);
        String line;
        List<String> numbers = new ArrayList<String>();
        System.out.print("Enter comma separated numbers followed by <enter> and \" \" to exit: \n");

        while (reader.hasNextLine() && !(line = reader.nextLine()).equals(" ")) {
            List<String> inputs = Arrays.stream(line.split(",")).map(String::trim)
                    .collect(Collectors.toList());
            numbers.addAll(inputs);
        }
        reader.close();
        return numbers;
    }


    public void customSubscribeWithErrorHandling(List<String> numbers) {
        Flux<Integer> numberFeed = Flux.fromIterable(numbers)
                .log()
                .map(input -> convertStringToNumber(input));

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
                System.out.println("Exception happened :" + t.getMessage());
            }

            @Override
            public void onComplete() {
                // TODO Auto-generated method stub
                System.out.println("Completed");
            }

        });
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ScannerHandson s = new ScannerHandson();
        // read from scanner and pass it to customSubscribeWithErrorHandling method
        s.customSubscribeWithErrorHandling(s.ReadFromScanner());


    }

}
