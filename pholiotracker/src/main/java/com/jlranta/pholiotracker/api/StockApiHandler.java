package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StockApiHandler {
    private ArrayList<StockApi> stockApis = new ArrayList<>();
    
    public StockApiHandler() {
        this.initializeApis();
    }
    
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
