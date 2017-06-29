package com.jlranta.pholiotracker.portfolio;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.TreeMap;
import java.util.Date;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.TestApi;
import com.jlranta.pholiotracker.api.StockSearchResult;


public class PortfolioTest {
    private StockApiHandler apiHandler = new StockApiHandler();
    
    @Test
    public void initialNameIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        
        assertTrue(p.toString().equals("test"));
    }
    
    @Test
    public void setNameWorks() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        p.setName("duck");
        
        assertTrue(p.toString().equals("duck"));
    }
    
    @Test
    public void moreCashAfterSelling() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security sec = new Security("security", "", "", new TestApi());
        sec.setAmount(10);
        securities.put("security", sec);
        p.setSecurities(securities);
        p.setCash(100.0);
        
        p.sellSecurity(sec, 10.0, 5, new Date());
        
        assertTrue(p.getCash().equals(150.0));
    }
    
    @Test
    public void lessCashAfterBuying() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        securities.put("security", new Security("security", "", "", new TestApi()));
        p.setSecurities(securities);
        p.setCash(100.0);
        
        p.buySecurity(new StockSearchResult("security", "sec", ""), 10.0, 5, new Date());
        
        assertTrue(p.getCash().equals(50.0));
    }
    
    @Test
    public void buyingFailsWithTooLittleCash() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        securities.put("security", new Security("security", "", "", new TestApi()));
        p.setSecurities(securities);
        
        assertTrue(p.buySecurity(new StockSearchResult("security", "sec", ""), 10.0, 5, new Date()) == false);
    }
    
    @Test
    public void buyMoreOfAnExistingSecurity() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security sec = new Security("sec", "security", "stock", new TestApi());
        sec.setAmount(5);
        securities.put(sec.getDescription(), sec);
        p.setSecurities(securities);
        p.setCash(1000.0);
        p.buySecurity(new StockSearchResult("security", "sec", "stock"), 10.0, 5, new Date());

        assertTrue(sec.getAmount().equals(10));
    }
    
    @Test
    public void sellingFailsWithNoSuchSecurity() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        p.setSecurities(securities);
        
        assertTrue(p.sellSecurity(new Security("security", "", "", new TestApi()), 10.0, 5, new Date()) == false);
    }
    
    @Test
    public void profitIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security secA = new Security("security", "", "", new TestApi());
        Security secB = new Security("security2", "", "", new TestApi());
        secA.setBuyPrice(5.0);
        secA.setAmount(10);
        secB.setBuyPrice(20.0);
        secB.setAmount(20);
        securities.put("security", secA);
        securities.put("security2", secB);
        p.setSecurities(securities);
        p.setCash(100.0);
        
        assertTrue(p.getChange().equals(-150.0));
    }
    
    @Test
    public void profitPercentageIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security secA = new Security("security", "", "", new TestApi());
        Security secB = new Security("security2", "", "", new TestApi());
        secA.setBuyPrice(40.0);
        secA.setAmount(40);
        secB.setBuyPrice(20.0);
        secB.setAmount(20);
        securities.put("security", secA);
        securities.put("security2", secB);
        p.setSecurities(securities);
        p.setCash(100.0);

        assertTrue(p.getProfitLoss().equals(-69.96501749125437));
    }
    
    @Test
    public void totalValueIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security secA = new Security("security", "", "", new TestApi());
        Security secB = new Security("security2", "", "", new TestApi());
        secA.setBuyPrice(5.0);
        secA.setAmount(10);
        secB.setBuyPrice(20.0);
        secB.setAmount(20);
        securities.put("security", secA);
        securities.put("security2", secB);
        p.setSecurities(securities);
        p.setCash(100.0);

        assertTrue(p.getTotalValue().equals(400.0));
    }
    
    @Test
    public void marketValueIsCorrect() {
        Portfolio p = new Portfolio("test", this.apiHandler);
        TreeMap<String, Security> securities = new TreeMap<>();
        Security secA = new Security("security", "", "", new TestApi());
        Security secB = new Security("security2", "", "", new TestApi());
        secA.setBuyPrice(5.0);
        secA.setAmount(10);
        secB.setBuyPrice(20.0);
        secB.setAmount(20);
        securities.put("security", secA);
        securities.put("security2", secB);
        p.setSecurities(securities);
        p.setCash(100.0);

        assertTrue(p.getMarketValue().equals(300.0));
    }
}
