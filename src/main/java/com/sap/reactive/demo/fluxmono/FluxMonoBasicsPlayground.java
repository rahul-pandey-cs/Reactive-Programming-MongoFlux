package com.sap.reactive.demo.fluxmono;

import com.sap.reactive.share.monitor.exercise.SharePrice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class FluxMonoBasicsPlayground {

    public static void main(String[] args) {
        tryFlux();
        tryMono();
        mergeTwoStreams();
    }


    private static void tryFlux() {
        //Use this space to explore Flux.
        System.out.println("-------------Start of Flux--------------");

        Flux<String> namesOfYourPresenter = Flux.just("Rahul", "Rohan", "Rohit")
                .filter(anyName -> !anyName.equals("Rohit"));

        namesOfYourPresenter.subscribe(name -> {
            System.out.println(name);
        });


        List<Integer> integerList = new ArrayList<>();
        integerList.add(2);
        integerList.add(3);
        integerList.add(4);
        integerList.add(-243234);
        integerList.add(9);

        Flux.fromIterable(integerList).takeUntil(inputNumber -> inputNumber < 0).subscribe(inputData -> System.out.println(inputData));
    }

    private static void tryMono() {
        //Use this space to explore Mono.
        System.out.println("-------------Start of Mono--------------");
        Mono.just("A single string").subscribe(System.out :: println);

        SharePrice sharePrice = new SharePrice();
        sharePrice.setHigh(10f);
        Mono.just(sharePrice).map(SharePrice::getHigh).map(price -> price*10).subscribe(System.out :: println);
    }

    private static void mergeTwoStreams() {
        //Use this space to merge two streams.
    }

}
