package com.jlranta.pholiotracker.api;

import java.util.LinkedHashMap;

/**
 * The StockSearchApi can be used for translating company names to stock
 * symbols that should be used in the stock API queries.
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public class StockSearchApi {
    private final String apiName = "Yahoo Stock Symbol Search";
    private final String baseUrl = "http://d.yimg.com/aq/autoc?region=US&lang=en-US";
    private RestApiQuery restQuery;
    
    public StockSearchApi() {
        this.restQuery = new RestApiQuery(this.apiName);
    }
    
    /**
     * Search for a company or a stock symbol
     * @param s A part of a company name or a stock symbol
     * @return  A LinkedHashMap of company names as keys and stock symbols as
     *          values
     */
    public LinkedHashMap<String, String> search(String s) {
        LinkedHashMap<String, String> results = new LinkedHashMap<>();
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&query=" + s);
        
        return results;
    }
    
}
