package com.jlranta.pholiotracker;

import com.jlranta.pholiotracker.api.AlphaVantageApi;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockApi;


public class Main {
    public static void main(String[] args) {
        StockApiHandler apiHandler = new StockApiHandler();
        LinkedHashMap<StockApi, ArrayList<String>> r = apiHandler.searchAll("Alphabet");
        
        StockApi vantage = new AlphaVantageApi();
        LinkedHashMap<String, Double> data = vantage.getData("GOOGL");

        System.out.println(data.toString());
    }
}
