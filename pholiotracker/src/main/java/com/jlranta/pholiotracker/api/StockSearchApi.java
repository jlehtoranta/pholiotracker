package com.jlranta.pholiotracker.api;

import java.util.LinkedHashMap;

public class StockSearchApi {
    private final String apiName = "Yahoo Stock Symbol Search";
    private final String baseUrl = "http://d.yimg.com/aq/autoc?region=US&lang=en-US";
    private RestApiQuery restQuery;
    
    public StockSearchApi() {
        this.restQuery = new RestApiQuery(this.apiName);
    }
    
    public LinkedHashMap<String, String> search(String s) {
        LinkedHashMap<String, String> results = new LinkedHashMap<>();
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&query=" + s);
        
        return results;
    }
    
}
