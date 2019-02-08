package com.sap.reactive.share.monitor.exercise;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The (frankly) crazy data coming from the real Shares API is formatted into this wonderful,
 * easy-to-understand format.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharePrice {
  
  @JsonAlias({"1. open", "02. open"})
  private float open;
  
  @JsonAlias({"2. high", "03. high"})
  private float high;
  
  @JsonAlias({"3. low", "04. low"})
  private float low;
  
  @JsonProperty("4. close")
  private float close;
  
  @JsonAlias({"5. volume", "06. volume"})
  private float volume;

  public float getOpen() {
    return open;
  }

  public float getHigh() {
    return high;
  }

  public float getLow() {
    return low;
  }

  public float getClose() {
    return close;
  }

  public float getVolume() {
    return volume;
  }

  public void setOpen(float open) {
    this.open = open;
  }

  public void setHigh(float high) {
    this.high = high;
  }

  public void setLow(float low) {
    this.low = low;
  }

  public void setClose(float close) {
    this.close = close;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }
}
