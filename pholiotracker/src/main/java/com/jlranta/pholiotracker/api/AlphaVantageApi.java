package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.StringReader;
import com.google.gson.stream.JsonReader;


public class AlphaVantageApi implements StockApi {
    private final String apiName = "Alpha Vantage";
    private final String apiKey = "90A8";
    private final String baseUrl = "http://www.alphavantage.co/query?";
    private RestApiQuery restQuery;
    
    public AlphaVantageApi() {
        this.restQuery = new RestApiQuery(this.apiName);
    }
    
    @Override
    public String toString() {
        return this.apiName;
    }
    
    @Override
    public ArrayList<String> search(String s) {
        
        return new ArrayList<>();
    }
    
    @Override
    public LinkedHashMap<String, Double> getData(String s) {
        LinkedHashMap<String, Double> data = new LinkedHashMap<>();
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&apikey=" + this.apiKey + "&function=TIME_SERIES_INTRADAY&interval=1min&symbol=" + s);
        System.out.println(response);
        JsonReader parser = new JsonReader(new StringReader(response));
        try {
            parser.beginObject();
            while (parser.hasNext()) {
                String name = parser.nextName();
                if (name.contains("Time Series")) {
                    parser.beginObject();
                    while (parser.hasNext()) {
                        String time = parser.nextName();
                        Double price = 0.0;
                        parser.beginObject();
                        while (parser.hasNext()) {
                            if (parser.nextName().equals("4. close")) {
                                price = parser.nextDouble();
                            } else {
                                parser.skipValue();
                            }
                        }
                        parser.endObject();
                        data.put(time, price);
                    }
                    parser.endObject();
                } else {
                    parser.skipValue();
                }
            }
            parser.endObject();
        } catch (IOException e) {
            System.err.println(this.apiName + " JSON parsing failed" + e.toString());
        }
        
        return data;
    }
    
}
