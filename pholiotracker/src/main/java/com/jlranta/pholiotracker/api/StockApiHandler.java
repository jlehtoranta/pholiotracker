package com.jlranta.pholiotracker.api;

import java.util.ArrayList;

/**
 * Initializes and handles all the APIs that implement the StockApi interface.
 * @author Jarkko Lehtoranta
 */
public class StockApiHandler {
    private ArrayList<StockApi> stockApis = new ArrayList<>();
    private StockSearchApi stockSearch;
    
    /**
     * Creates a new StockApiHandler instance.
     */
    public StockApiHandler() {
        this.initializeApis();
    }
    
    /**
     * Search for a stock.
     * @param s A part of a stock name or a stock symbol
     * @return  A list of StockSearchResults
     */
    public ArrayList<StockSearchResult> searchStock(String s) {
        return this.stockSearch.search(s);
    }
    
    /**
     * Get the most recent price quote for a stock.
     * @param s    A stock symbol
     * @param exch A stock exchange, where the symbol is listed
     * @return     The most recent price as a StockSearchResult
     */
    public StockSearchResult getCurrentPrice(String s, String exch) {
        StockSearchResult res = null;
        
        for (StockApi api : this.stockApis) {
            if (api.toString().equals("Alpha Vantage")) {
                res = api.getQuote(s, exch);
            }
        }
        
        return res;
    }
    
    /**
     * Search for a stock API, which provides data for a stock symbol.
     * @param s A company name or a stock symbol
     * @return  A LinkedHashMap of StockApis as keys and a list of found
     *          stock symbols as strings
     */
    public StockApi searchApi(String s) {

        for (StockApi api : this.stockApis) {
            ArrayList<String> found = api.search(s);
            if (!found.isEmpty()) {
                return api;
            }
        }
        
        return null;
    }
    
    public ArrayList<StockApi> getApis() {
        return this.stockApis;
    }
    
    private void initializeApis() {
        this.stockSearch = new StockSearchApi();
        
        StockApi vantage = new AlphaVantageApi();
        this.stockApis.add(vantage);
    }
}
