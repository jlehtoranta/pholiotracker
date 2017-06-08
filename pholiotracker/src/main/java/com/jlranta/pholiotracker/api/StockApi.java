package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * StockApi is an interface that describes how the support for various stock
 * APIs should be implemented. This makes it easier to add support for new APIs
 * to the application.
 * 
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public interface StockApi {
    
    /**
     * Get a description of the API
     * @return The display name of the API
     */
    @Override
    String toString();
    
    /**
     * Search for a stock symbol or company name in the API
     * @param s Stock symbol or company name to search
     * @return A list of stock symbols
     */
    ArrayList<String> search(String s);
    
    /**
     * Query a stock price history for a stock symbol
     * @param s Stock symbol
     * @return A LinkedHashMap of times as keys and prices as values
     */
    LinkedHashMap<String, Double> getData(String s);
}
