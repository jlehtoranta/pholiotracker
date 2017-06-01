package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public interface StockApi {
    
    @Override
    public String toString();
    
    public ArrayList<String> search(String s);
    public LinkedHashMap<String, Double> getData(String s);
}
