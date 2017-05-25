package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.TreeMap;


public interface StockApi {
    
    @Override
    public String toString();
    
    public ArrayList<String> search(String s);
    public TreeMap<String, Double> getData(String s);
}
