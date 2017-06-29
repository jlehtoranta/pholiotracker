package com.jlranta.pholiotracker.api;

import java.util.Date;

/**
 * Describes a single search result.
 * @author Jarkko Lehtoranta
 */
public class StockSearchResult {
    private String name;
    private String symbol;
    private String exchange;
    private Double price;
    private Date time;
    
    /**
     * Creates a new StockSearchResult.
     * @param n The name of the stock
     * @param s The stock symbol
     * @param e The stock exchange, where the stock is listed
     */
    public StockSearchResult(String n, String s, String e) {
        this.name = n;
        this.symbol = s;
        this.exchange = e;
    }
    
    /**
     * Creates a new StockSearchResult.
     * @param n The name of the stock
     * @param s The stock symbol
     * @param e The stock exchange, where the stock is listed
     * @param p The most recent price quote for the stock
     * @param t The time of the most recent price quote
     */
    public StockSearchResult(String n, String s, String e, Double p, Date t) {
        this.name = n;
        this.symbol = s;
        this.exchange = e;
        this.price = p;
        this.time = t;
    }
    
    public String getDescription() {
        return this.getName() + " (" + this.getSymbol() + ":" + this.getExchange() + ")";
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSymbol() {
        return this.symbol;
    }
    
    public String getExchange() {
        return this.exchange;
    }
    
    public Double getPrice() {
        return this.price;
    }
    
    public Date getTime() {
        return this.time;
    }
    
    @Override
    public String toString() {
        return this.name + " (" + this.symbol + ":" + this.exchange + ")";
    }
    
}
