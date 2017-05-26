package com.jlranta.pholiotracker.portfolio;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;

import com.jlranta.pholiotracker.api.StockApi;
import com.jlranta.pholiotracker.api.AlphaVantageApi;

public class SecurityTest {
    private StockApi vantageApi = new AlphaVantageApi();
    
    @Test
    public void initialValuesAreCorrect() {
        Security s = new Security("test", this.vantageApi);
        
        assertTrue(s.toString().equals("test"));
        assertTrue(s.getAmount().equals(0));
        assertTrue(s.getBuyPrice().equals(0.0));
    }
    
    @Test
    public void amountIncreasesWhenBuying() {
        Security s = new Security("test", this.vantageApi);
        
        s.buy(5, 10.0);
        
        assertTrue(s.getAmount().equals(5));
    }
    
    @Test
    public void buyDataIsCorrectlyUpdatedWhenBuying() {
        Security s = new Security("test", this.vantageApi);
        ArrayDeque<SimpleEntry<Double, Integer>> bd = new ArrayDeque<>();
        bd.add(new SimpleEntry(100.0, 10));
        bd.add(new SimpleEntry(150.0, 40));
        s.setBuyData(bd);
        s.setAmount(50);
        
        s.buy(20, 50.0);
        
        bd = s.getBuyData();
        
        assertTrue(bd.size() == 3);
        assertTrue(bd.getLast().getValue().equals(20));
        assertTrue(bd.getLast().getKey().equals(50.0));
    }
    
    @Test
    public void meanPriceIsCorrectlyUpdatedWhenBuying() {
        Security s = new Security("test", this.vantageApi);
        ArrayDeque<SimpleEntry<Double, Integer>> bd = new ArrayDeque<>();
        bd.add(new SimpleEntry(100.0, 10));
        bd.add(new SimpleEntry(150.0, 40));
        s.setBuyData(bd);
        s.setAmount(50);
        s.setBuyPrice((100.0 * 10 + 150.0 * 40) / 50);
        
        s.buy(20, 50.0);
        
        assertTrue(s.getBuyPrice().equals((100.0 * 10 + 150.0 * 40 + 50.0 * 20) / 70));
    }
    
    @Test
    public void amountDecreasesWhenSelling() {
        Security s = new Security("test", this.vantageApi);
        s.setAmount(10);
        
        s.sell(5);
        
        assertTrue(s.getAmount().equals(5));
    }
    
    @Test
    public void buyDataIsCorrectlyUpdatedWhenSelling() {
        Security s = new Security("test", this.vantageApi);
        ArrayDeque<SimpleEntry<Double, Integer>> bd = new ArrayDeque<>();
        bd.add(new SimpleEntry(100.0, 10));
        bd.add(new SimpleEntry(150.0, 40));
        s.setBuyData(bd);
        s.setAmount(50);
        
        s.sell(20);
        
        bd = s.getBuyData();
        
        assertTrue(bd.size() == 1);
        assertTrue(bd.getFirst().getValue().equals(30));
    }
    
    @Test
    public void meanPriceIsCorrectlyUpdatedWhenSelling() {
        Security s = new Security("test", this.vantageApi);
        ArrayDeque<SimpleEntry<Double, Integer>> bd = new ArrayDeque<>();
        bd.add(new SimpleEntry(100.0, 10));
        bd.add(new SimpleEntry(150.0, 40));
        s.setBuyData(bd);
        s.setAmount(50);
        s.setBuyPrice((100.0 * 10 + 150.0 * 40) / 50);
        
        s.sell(20);
        
        assertTrue(s.getBuyPrice().equals(150.0));
    }
    
}
