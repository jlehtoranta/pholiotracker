package com.jlranta.pholiotracker.portfolio;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.TreeMap;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.AlphaVantageApi;


public class PortfolioTest {
    private StockApiHandler apiHandler = new StockApiHandler();
    
    @Test
    public void initialNameIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        
        assertTrue(p.toString().equals("test"));
    }
    
    @Test
    public void moreCashAfterSelling() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security sec = new Security("security", new AlphaVantageApi());
        sec.setAmount(10);
        securities.put("security", sec);
        p.setSecurities(securities);
        p.setCash(100.0);
        
        p.sellSecurity("security", 10.0, 5);
        
        assertTrue(p.getCash().equals(150.0));
    }
    
    @Test
    public void lessCashAfterBuying() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        securities.put("security", new Security("security", new AlphaVantageApi()));
        p.setSecurities(securities);
        p.setCash(100.0);
        
        p.buySecurity("security", 10.0, 5);
        
        assertTrue(p.getCash().equals(50.0));
    }
}
