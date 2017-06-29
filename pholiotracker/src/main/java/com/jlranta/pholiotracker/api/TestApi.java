package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * This is a dummy API for testing purposes.
 * @author Jarkko Lehtoranta
 */
public class TestApi implements StockApi {

    /**
     * Get a description of the API.
     * @return The display name of the API
     */
    @Override
    public String toString() {
        return "";
    }
    
    /**
     * Search for a stock symbol or company name in the API.
     * @param s Stock symbol or company name to search
     * @return A list of stock symbols
     */
    @Override
    public ArrayList<String> search(String s) {
        return new ArrayList<>();
    }
    
    /**
     * Query a stock price history for a stock symbol.
     * @param s Stock symbol
     * @return A LinkedHashMap of times as keys and prices as values
     */
    @Override
    public LinkedHashMap<Date, Double> getData(String s) {
        return new LinkedHashMap<>();
    }
    
    /**
     * Query the latest price for a stock symbol.
     * @param s Stock symbol
     * @param exch Stock exchange name
     * @return The latest price as a double value
     */
    @Override
    public StockSearchResult getQuote(String s, String exch) {
        return new StockSearchResult("test", "test", "test", 10.0, new Date());
    }
}
