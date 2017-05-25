package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.TreeMap;


public class AlphaVantageApi implements StockApi {
    String apiName = "Alpha Vantage";
    String apiKey = "";
    
    @Override
    public String toString() {
        return this.apiName;
    }
    
    @Override
    public ArrayList<String> search(String s) {
        
        return new ArrayList<>();
    }
    
    @Override
    public TreeMap<String, Double> getData(String s) {
        
        return new TreeMap<>();
    }
}
