package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Initializes and handles all the APIs that implement the StockApi interface
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public class StockApiHandler {
    private ArrayList<StockApi> stockApis = new ArrayList<>();
    
    public StockApiHandler() {
        this.initializeApis();
    }
    
    /**
     * 
     * @param s A company name or a stock symbol
     * @return  A LinkedHashMap of StockApis as keys and a list of found
     *          stock symbols as strings
     */
    public LinkedHashMap<StockApi, ArrayList<String>> searchAll(String s) {
        LinkedHashMap<StockApi, ArrayList<String>> foundFromApis = new LinkedHashMap<>();
        
        for (StockApi api : this.stockApis) {
            ArrayList<String> found = api.search(s);
            if (!found.isEmpty()) {
                foundFromApis.put(api, found);
            }
        }
        
        return foundFromApis;
    }
    
    public ArrayList<StockApi> getApis() {
        return this.stockApis;
    }
    
    private void initializeApis() {
        StockApi vantage = new AlphaVantageApi();
        this.stockApis.add(vantage);
    }
}
