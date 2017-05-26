package com.jlranta.pholiotracker.portfolio;

import java.util.TreeMap;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockApi;


public class Portfolio {
    private String name;
    private Double cash;
    private StockApiHandler apiHandler;
    private TreeMap<String, Security> securities = new TreeMap<>();
    private TreeMap<String, Double> valueData = new TreeMap<>();
    
    public Portfolio(String name, StockApiHandler apiHandler) {
        this.name = name;
        this.apiHandler = apiHandler;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public void setSecurities(TreeMap<String, Security> secs) {
        this.securities = secs;
    }
    
    public void setCash(Double c) {
        this.cash = c;
    }
    
    public Double getCash() {
        return this.cash;
    }
    
    public TreeMap<String, Security> getSecurities() {
        return this.securities;
    }
    
    public TreeMap<String, Double> getValues() {
        return this.valueData;
    }
    
    public Security getSecurity(String s) {
        return this.securities.get(s);
    }
    
    public boolean buySecurity(String s, Double p, Integer a) {
        Security sec = this.securities.get(s);
        
        if (sec != null) {
            if (sec.buy(a, p)) {
                this.cash -= p * a;
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean sellSecurity(String s, Double p, Integer a) {
        Security sec = this.securities.get(s);
        
        if (sec != null) {
            if (sec.sell(a)) {
                this.cash += p * a;
                
                return true;
            }
        }
        
        return false;
    }
}
