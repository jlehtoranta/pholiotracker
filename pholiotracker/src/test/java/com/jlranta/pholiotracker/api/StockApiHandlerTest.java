package com.jlranta.pholiotracker.api;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class StockApiHandlerTest {
    private StockApiHandler apiHandler = new StockApiHandler();
    
    @Test
    public void searchingForAStockWorks() {
        ArrayList<StockSearchResult> results = this.apiHandler.searchStock("apple");
        
        boolean found = false;
        
        for (StockSearchResult r : results) {
            if (r.getName().contains("Apple")) {
                found = true;
            }
        }
        
        assertTrue(found);
    }
    
    @Test
    public void allApisAreAddedToTheApiList() {
        assertTrue(this.apiHandler.getApis().size() == 1);
    }
    
    @Test
    public void getCurrentPriceForAStockWorks() {
        assertTrue(this.apiHandler.getCurrentPrice("AAPL", "NASDAQ").getPrice() > 0.0);
    }
    
    @Test
    public void getCurrentPriceForAnEmptyStockWorks() {
        assertTrue(this.apiHandler.getCurrentPrice("", "").getPrice() == 0.0);
    }
}
