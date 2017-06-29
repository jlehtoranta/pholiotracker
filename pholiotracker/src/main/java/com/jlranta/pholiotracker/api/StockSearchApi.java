package com.jlranta.pholiotracker.api;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * The StockSearchApi can be used for translating company names to stock
 * symbols that should be used in the stock API queries.
 * @author Jarkko Lehtoranta
 */
public class StockSearchApi {
    private final String apiName = "Yahoo Stock Symbol Search";
    private final String baseUrl = "http://d.yimg.com/aq/autoc?region=US&lang=en-US";
    private RestApiQuery restQuery;
    
    /**
     * Creates a new StockSearchApi instance.
     */
    public StockSearchApi() {
        this.restQuery = new RestApiQuery(this.apiName);
    }
    
    /**
     * Search for a company or a stock symbol.
     * @param s A part of a company name or a stock symbol
     * @return  A LinkedHashMap of company names as keys and stock symbols as
     *          values
     */
    public ArrayList<StockSearchResult> search(String s) {
        ArrayList<StockSearchResult> results = new ArrayList<>();
        
        String response = this.restQuery.apiRequest(this.baseUrl + "&query=" + s);
        
        JsonReader parser = new JsonReader(new StringReader(response));
        try {
            parser.beginObject();
            while (parser.hasNext()) {
                if (parser.nextName().equals("ResultSet")) {
                    parser.beginObject();
                    while (parser.hasNext()) {
                        if (parser.nextName().equals("Result")) {
                            parser.beginArray();
                            while (parser.hasNext()) {
                                this.readResults(parser, results);
                            }
                            parser.endArray();
                        } else {
                            parser.skipValue();
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
        
        return results;
    }
    
    private void readResults(JsonReader parser, ArrayList<StockSearchResult> results) throws IOException {
        String s = "";
        String n = "";
        String e = "";
        
        parser.beginObject();
        while (parser.hasNext()) {
            switch (parser.nextName()) {
                case "symbol":
                    s = parser.nextString();
                    break;
                case "name":
                    n = parser.nextString();
                    break;
                case "exchDisp":
                    e = parser.nextString();
                    break;
                default:
                    parser.skipValue();
                    break;
            }
        }
        parser.endObject();
        
        if (!(e.isEmpty() && s.isEmpty() && n.isEmpty())) {
            // Show only wall street for now...
            if (e.equals("NASDAQ") || e.equals("NYSE")) {
                results.add(new StockSearchResult(n, s, e));
            }
        }
    }
    
}
