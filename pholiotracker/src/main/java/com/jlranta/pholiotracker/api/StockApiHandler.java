package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.TreeMap;

public class StockApiHandler {
    private ArrayList<StockApi> stockApis = new ArrayList<>();
    
    public StockApiHandler() {
        this.initializeApis();
    }
    
    public TreeMap<StockApi, ArrayList<String>> searchAll(String s) {
        TreeMap<StockApi, ArrayList<String>> foundFromApis = new TreeMap<>();
        
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
