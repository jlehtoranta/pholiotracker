package com.jlranta.pholiotracker.portfolio;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayDeque;
import java.util.Date;

import com.jlranta.pholiotracker.api.StockApi;
import com.jlranta.pholiotracker.api.TestApi;

public class SecurityTest {
    private StockApi testApi = new TestApi();
    
    @Test
    public void initialValuesAreCorrect() {
        Security s = new Security("test", "name", "exch", this.testApi);
        
        assertTrue(s.getName().equals("name"));
        assertTrue(s.getSymbol().equals("test"));
        assertTrue(s.getExchange().equals("exch"));
        assertTrue(s.getAmount().equals(0));
        assertTrue(s.getBuyPrice().equals(0.0));
    }
    
    @Test
    public void amountIncreasesWhenBuying() {
        Security s = new Security("test", "", "", this.testApi);
        
        s.buy(10.0, 5, new Date());
        
        assertTrue(s.getAmount().equals(5));
    }
    
    @Test
    public void buyDataIsCorrectlyUpdatedWhenBuying() {
        Security s = new Security("test", "", "", this.testApi);
        ArrayDeque<SimpleTransaction> bd = new ArrayDeque<>();
        bd.add(new SimpleTransaction(100.0, 10, new Date()));
        bd.add(new SimpleTransaction(150.0, 40, new Date()));
        s.setBuyData(bd);
        s.setAmount(50);
        
        s.buy(50.0, 20, new Date());
        
        bd = s.getBuyData();
        
        assertTrue(bd.size() == 3);
        assertTrue(bd.getLast().getAmount().equals(20));
        assertTrue(bd.getLast().getPrice().equals(50.0));
    }
    
    @Test
    public void meanPriceIsCorrectlyUpdatedWhenBuying() {
        Security s = new Security("test", "", "", this.testApi);
        ArrayDeque<SimpleTransaction> bd = new ArrayDeque<>();
        bd.add(new SimpleTransaction(100.0, 10, new Date()));
        bd.add(new SimpleTransaction(150.0, 40, new Date()));
        s.setBuyData(bd);
        s.setAmount(50);
        s.setBuyPrice((100.0 * 10 + 150.0 * 40) / 50);
        
        s.buy(50.0, 20, new Date());
        
        assertTrue(s.getBuyPrice().equals((100.0 * 10 + 150.0 * 40 + 50.0 * 20) / 70));
    }
    
    @Test
    public void amountDecreasesWhenSelling() {
        Security s = new Security("test", "", "", this.testApi);
        s.setAmount(10);
        
        s.sell(5);
        
        assertTrue(s.getAmount().equals(5));
    }
    
    @Test
    public void buyDataIsCorrectlyUpdatedWhenSelling() {
        Security s = new Security("test", "", "", this.testApi);
        ArrayDeque<SimpleTransaction> bd = new ArrayDeque<>();
        bd.add(new SimpleTransaction(100.0, 10, new Date()));
        bd.add(new SimpleTransaction(150.0, 40, new Date()));
        s.setBuyData(bd);
        s.setAmount(50);
        
        s.sell(20);
        
        bd = s.getBuyData();
        
        assertTrue(bd.size() == 1);
        assertTrue(bd.getFirst().getAmount().equals(30));
    }
    
    @Test
    public void meanPriceIsCorrectlyUpdatedWhenSelling() {
        Security s = new Security("test", "", "", this.testApi);
        ArrayDeque<SimpleTransaction> bd = new ArrayDeque<>();
        bd.add(new SimpleTransaction(100.0, 10, new Date()));
        bd.add(new SimpleTransaction(150.0, 40, new Date()));
        s.setBuyData(bd);
        s.setAmount(50);
        s.setBuyPrice((100.0 * 10 + 150.0 * 40) / 50);
        
        s.sell(20);
        
        assertTrue(s.getBuyPrice().equals(150.0));
    }
    
}
