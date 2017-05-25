package com.jlranta.pholiotracker;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockApi;

import java.util.ArrayList;
import java.util.TreeMap;


public class Main {
    public static void main(String[] args) {
        StockApiHandler apiHandler = new StockApiHandler();
        TreeMap<StockApi, ArrayList<String>> r = apiHandler.searchAll("Alphabet");
    }
}
