package com.jlranta.pholiotracker.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.StringReader;
import com.google.gson.stream.JsonReader;

/**
 * This class implements the Alpha Vantage stock API. Have a look at the
 * in depth documentation of the methods in the StockApi interface.
 * @author Jarkko Lehtoranta
 */
public class AlphaVantageApi implements StockApi {
    private final String apiName = "Alpha Vantage";
    private final String apiKey = "90A8";
    private final String baseUrl = "http://www.alphavantage.co/query?";
    private RestApiQuery restQuery;
    
    /**
     * Creates a new API instance.
     */
    public AlphaVantageApi() {
        this.restQuery = new RestApiQuery(this.apiName);
    }
    
    @Override
    public String toString() {
        return this.apiName;
    }
    
    @Override
    public ArrayList<String> search(String s) {
        ArrayList<String> r = new ArrayList<>();
        
        r.add("");
        
        return r;
    }
    
    @Override
    public StockSearchResult getQuote(String s, String exch) {
        Double price = 0.0;
        Date time = null;
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&apikey=" + this.apiKey + "&function=GLOBAL_QUOTE&symbol=" + exch + ":" + s);
        if (response.isEmpty()) {
            return null;
        }
        JsonReader parser = new JsonReader(new StringReader(response));
        DateFormat df = new SimpleDateFormat("MMM dd, hh:mma z yyyy");
        try {
            parser.beginObject();
            while (parser.hasNext()) {
                if (parser.nextName().equals("Realtime Global Securities Quote")) {
                    parser.beginObject();
                    while (parser.hasNext()) {
                        switch (parser.nextName()) {
                            case "03. Latest Price":
                                price = parser.nextDouble();
                                break;
                            case "11. Last Updated":
                                try {
                                    time = df.parse(parser.nextString() + " " + Calendar.getInstance().get(Calendar.YEAR));
                                } catch (ParseException e) {
                                    System.err.println(this.apiName + " JSON date parsing failed" + e.toString());
                                    parser.skipValue();
                                }
                                break;
                            default:
                                parser.skipValue();
                                break;
                        }
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
        
        return new StockSearchResult("", s, exch, price, time);
    }
    
    @Override
    public LinkedHashMap<Date, Double> getData(String s) {
        LinkedHashMap<Date, Double> data = new LinkedHashMap<>();
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&apikey=" + this.apiKey + "&function=TIME_SERIES_DAILY&symbol=" + s);
        if (response.isEmpty()) {
            return data;
        }
        JsonReader parser = new JsonReader(new StringReader(response));
        try {
            parser.beginObject();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            while (parser.hasNext()) {
                String name = parser.nextName();
                if (name.contains("Meta Data")) {
                    parser.beginObject();
                    while (parser.hasNext()) {
                        if (parser.nextName().equals("6. Time Zone")) {
                            df.setTimeZone(TimeZone.getTimeZone(parser.nextString()));
                        } else {
                            parser.skipValue();
                        }
                    }
                    parser.endObject();
                } else if (name.contains("Time Series")) {
                    parser.beginObject();
                    while (parser.hasNext()) {
                        Date time;
                        try {
                            time = df.parse(parser.nextName());
                        } catch (ParseException e) {
                            System.err.println(this.apiName + " JSON date parsing failed" + e.toString());
                            parser.skipValue();
                            continue;
                        }
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
