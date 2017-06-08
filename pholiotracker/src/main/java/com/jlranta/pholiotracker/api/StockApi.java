package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public interface StockApi {
    
    @Override
    String toString();
    
    ArrayList<String> search(String s);
    LinkedHashMap<String, Double> getData(String s);
}
