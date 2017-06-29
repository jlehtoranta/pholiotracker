package com.jlranta.pholiotracker.portfolio;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockSearchResult;

import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Date;

/**
 * The Portfolio class represents and keeps track of the user's investment
 * portfolio.
 * @author Jarkko Lehtoranta
 */
public class Portfolio {
    private String name = "";
    private Double cash = 0.0;
    private StockApiHandler apiHandler;
    private TreeMap<String, Security> securities = new TreeMap<>();
    private LinkedHashMap<Date, Double> valueData = new LinkedHashMap<>();
    
    /**
     * Creates a new Portfolio.
     * @param name          The name of the portfolio as a string
     * @param apiHandler    The API handler that handles all the stock APIs
     */
    public Portfolio(String name, StockApiHandler apiHandler) {
        this.name = name;
        this.apiHandler = apiHandler;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public void setName(String n) {
        this.name = n;
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
    
    public LinkedHashMap<Date, Double> getValues() {
        return this.valueData;
    }
    
    /**
     * Updates the price history of the portfolio.
     */
    public void updateValues() {
        this.valueData.clear();
        for (Map.Entry<String, Security> sec : this.securities.entrySet()) {
            LinkedHashMap<Date, Double> prices = sec.getValue().getValues();
            for (SimpleTransaction st : sec.getValue().getBuyData()) {
                for (Map.Entry<Date, Double> price : prices.entrySet()) {
                    if (price.getKey().compareTo(st.getTime()) >= 0) {
                        Double value = st.getAmount() * price.getValue() + this.valueData.getOrDefault(price.getKey(), 0.0);
                        this.valueData.put(price.getKey(), value);
                    }
                }
            }
        }
    }
    
    /**
     * Pulls new price data for the securities and updates the price history
     * of the portfolio.
     */
    public void updateAllValues() {
        for (Map.Entry<String, Security> sec : this.securities.entrySet()) {
            sec.getValue().updateValues();
        }
        this.updateValues();
    }
    
    /**
     * Get a security from the portfolio.
     * @param s The description of the security
     * @return  The Security in question
     */
    public Security getSecurity(String s) {
        return this.securities.get(s);
    }
    
    /**
     * Buy more of a single security.
     * @param sr The stock search result
     * @param p  The buy price of a single security
     * @param a  The amount to buy
     * @param t  The time of the transaction
     * @return  <code>true</code>, if the transaction succeeds. Otherwise
     *          return <code>false</code>.
     */
    public boolean buySecurity(StockSearchResult sr, Double p, Integer a, Date t) {
        Security sec = this.getSecurity(sr.getDescription());
        
        if (this.cash < p * a) {
            return false;
        }
        
        if (sec != null) {
            if (sec.buy(p, a, t)) {
                this.cash -= p * a;
                this.updateValues();
                return true;
            }
        } else {
            sec = new Security(sr.getSymbol(), sr.getName(), sr.getExchange(), this.apiHandler.searchApi(sr.getSymbol()));
            if (sec.buy(p, a, t)) {
                this.cash -= p * a;
                this.securities.put(sec.getDescription(), sec);
                this.updateValues();
                return true;
            } 
        }
        return false;
    }
    
    /**
     * Sell x amount of a single security.
     * @param sec The security to sell
     * @param p   The sell price of a single security
     * @param a   The amount to sell
     * @param t   The sell time
     * @return  <code>true</code>, if the transaction succeeds. Otherwise
     *          return <code>false</code>.
     */
    public boolean sellSecurity(Security sec, Double p, Integer a, Date t) {
        if (sec != null) {
            if (sec.sell(a)) {
                if (sec.getAmount() == 0) {
                    this.securities.remove(sec.getDescription());
                }
                this.cash += p * a;
                this.updateValues();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the amount of profit/loss.
     * @return The amount of profit/loss
     */
    public Double getChange() {
        Double val = 0.0;
        
        for (Map.Entry<String, Security> sec : this.securities.entrySet()) {
            val += sec.getValue().getProfit();
        }
        return val;
    }
    
    /**
     * Gets the amount of profit/loss as a percentage value.
     * @return The amount of profit/loss as a percentage value
     */
    public Double getProfitLoss() {
        Double val = 1.0;
        Double mval = 1.0;
        
        for (Map.Entry<String, Security> sec : this.securities.entrySet()) {
            val += sec.getValue().getBuyPrice() * sec.getValue().getAmount();
            mval += sec.getValue().getCurrentPrice() * sec.getValue().getAmount();
        }
        return ((mval / val) - 1.0) * 100.0;
    }
    
    public Double getTotalValue() {
        return this.getCash() + this.getMarketValue();
    }
    
    /**
     * Gets the current market value of the portfolio.
     * @return The current market value of the portfolio
     */
    public Double getMarketValue() {
        Double val = 0.0;
        for (Map.Entry<String, Security> sec : this.securities.entrySet()) {
            val += sec.getValue().getCurrentPrice() * sec.getValue().getAmount();
        }
        return val;
    }
}
