package com.sap.reactive.share.monitor.exercise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This is the class that makes you rich!
 * 
 * The ShareMonitor tells you whether or not to invest in SAP shares, based on real data streamed
 * from stock price APIs.
 * 
 * Your task is to fill 
 * 
 * A) the empty methods here and
 * B) your pockets
 *
 */
public class SAPSharesMonitor {
  
  /**
   * Use this in the Flux exercise when you count which Share Prices match your criteria.
   */
  private int timeSeriesCount;
  
  
  /**
   * Use this in the Mono exercise, to decide whether or not to invest.
   */
  private boolean singeQuoteMatchesCriteria;
  
  
  /**
   * 
   * Decide whether to make an investment decision.
   * 
   * This depends on your analysis of two data streams - one containing the last 100 share prices at
   * 5 minute intervals, and the other containing a single summary price for the day.
   * 
   * If your analysis of both sets of data results in a favourable outcome, you decide to invest.
   * 
   * @return
   * @throws IOException
   */
  public boolean makeInvestmentDecision() throws IOException {
    boolean timeSeriesSaysIGetRich = analyzeTimeSeriesData();
    boolean singleQuoteSaysIGetRich = analyzeSingleQuoteData();
    
    return timeSeriesSaysIGetRich && singleQuoteSaysIGetRich;
  }  
  
  /**
   * Analyzes a stream of the latest 100 share prices at five minute intervals.
   * 
   * Criteria for choosing to invest - 
   * 
   *      1. An SAP share having a high point of more than $104 and a lowest point of more than $103 is a good sign.
   *      2. If this has happened at least 25% of the time (25 times), we can assume it is good to invest and stop listening to the stream.
   * 
   * Logging - 
   * 
   *      Print "Closing Price of $104... Looks promising!" for each SharePrice in the stream that matches this criteria.
   *      
   * @return a boolean indicating whether to invest in the shares.
   * @throws IOException Exception.
   */
  private boolean analyzeTimeSeriesData() throws IOException {
    Flux<SharePrice> sharePricesTimeSeries = getSharePricesForEveryFiveMinutes();
    
    // Your logic goes here. Go ahead and play around with this flux!
    sharePricesTimeSeries.filter( share -> share.getHigh() > 104).filter(share -> share.getLow() >103)
            .takeWhile((sharePrice -> timeSeriesCount <=25)).subscribe( sharePrice ->{
      timeSeriesCount++;});

    if(timeSeriesCount>25)
      return true;

    return false;
    };
  
  /**
   * Analyses the current share price.
   * 
   * For stocks, a good measure of liquidity is the average daily trading volume. In general, any
   * stock that trades at fewer than 10,000 shares a day is considered a low-volume stock.
   * Low-volume stocks are harder to buy or sell quickly and at the market price.
   * 
   * Let's consider a volume of 300000 as our benchmark for liquid stock. We invest if yesterday's
   * volume was greater than this.
   * 
   *   Logging -
   *      Print "300001 stocks were traded yesterday. Looks good!" if criteria is met.
   *      Or print "Only 200000 stocks were traded yesterday.. :(" if it's not.
   * 
   * @return a boolean indicating whether to invest in the shares.
   * @throws IOException Exception
   */
  private boolean analyzeSingleQuoteData() throws IOException {
    Mono<SharePrice> latestSharePrice = getLatestSharePrice();
    // Your logic goes here. Go ahead and play around with this mono!
    latestSharePrice.subscribe(sharePrice -> {
      Float shareVolume = sharePrice.getVolume();
      if (shareVolume > 300000) {
        System.out.println(shareVolume + "stocks were traded yesterday. Looks good!");
        singeQuoteMatchesCriteria = true;
      } else {
        System.out.println("Only" + shareVolume + "stocks were traded yesterday.. :(");
        singeQuoteMatchesCriteria = false;
      }
    });
    return singeQuoteMatchesCriteria;
  }
  
  /**
   * 
   * Gets SAP's last 100 share prices at five minute intervals.
   * 
   *
   * @return A flux containing {@link SharePrice} objects.
   * @throws IOException Exception
   */
  private Flux<SharePrice> getSharePricesForEveryFiveMinutes() throws IOException {
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    ObjectMapper mapper = new ObjectMapper();
    List<SharePrice> sharePrices = new ArrayList<>();
    ResponseEntity<String> response = restTemplate.getForEntity(
        "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=SAP&interval=5min&apikey=ZMAHH3FUGCWHABMF",
        String.class);
    JsonNode timeSeriesData = mapper.readTree(response.getBody()).get("Time Series (5min)");
    timeSeriesData.forEach(jsonNode -> {
      sharePrices.add(mapper.convertValue(jsonNode, SharePrice.class));
    });
    return Flux.fromStream(sharePrices.stream());
  }
  
  /**
   * 
   * Gets the latest SAP share price for the day. 
   * 
   * @return A mono containing {@link SharePrice} objects.
   * @throws IOException
   */
  private Mono<SharePrice> getLatestSharePrice() throws IOException {
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    ObjectMapper mapper = new ObjectMapper();
    ResponseEntity<String> response = restTemplate.getForEntity(
        "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=SAP&apikey=ZMAHH3FUGCWHABMF",
        String.class);
    JsonNode latestPriceInformation = mapper.readTree(response.getBody()).get("Global Quote");
    return Mono.just(mapper.convertValue(latestPriceInformation, SharePrice.class));
  }

  
}
