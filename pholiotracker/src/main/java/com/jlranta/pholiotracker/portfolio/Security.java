package com.jlranta.pholiotracker.portfolio;

import com.jlranta.pholiotracker.api.StockApi;

import java.util.LinkedHashMap;
import java.util.ArrayDeque;
import java.util.Date;

/**
 * This class represents a single security. The object tracks the user's buy
 * and sell history for the security. It also keeps track of the historical
 * price information for the security.
 * @author Jarkko Lehtoranta
 */
public class Security {
    private String name;
    private String symbol;
    private String exchange;
    private StockApi api;
    private Integer amount;
    private Double meanBuyPrice;
    private Double currentPrice;
    private ArrayDeque<SimpleTransaction> buyData = new ArrayDeque<>();
    private LinkedHashMap<Date, Double> valueData = new LinkedHashMap<>();
    
    /**
     * Creates a new security.
     * @param name     The name of the security as a string
     * @param symbol   The stock symbol of the security as a string
     * @param exchange The stock exchange name as a string
     * @param api      The stock API, where the security gets its price data
     */
    public Security(String symbol, String name, String exchange, StockApi api) {
        this.amount = 0;
        this.meanBuyPrice = 0.0;
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.api = api;
        this.updateValues();
    }
    
    @Override
    public String toString() {
        return this.getName() + " (" + this.getSymbol() + ":" + this.getExchange() + "), " + this.getAmount() + " pcs";
    }
    
    public String getDescription() {
        return this.getName() + " (" + this.getSymbol() + ":" + this.getExchange() + ")";
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSymbol() {
        return this.symbol;
    }
    
    public String getExchange() {
        return this.exchange;
    }
    
    public Double getProfit() {
        return (this.currentPrice - this.meanBuyPrice) * this.amount;
    }
    
    public Double getProfitPercentage() {
        return ((this.currentPrice / this.meanBuyPrice) - 1.0) * 100.0;
    }
    
    public ArrayDeque<SimpleTransaction> getBuyData() {
        return this.buyData;
    }
    
    public void setBuyData(ArrayDeque<SimpleTransaction> bd) {
        this.buyData = bd;
    }
    
    public LinkedHashMap<Date, Double> getValues() {
        return this.valueData;
    }
    
    /**
     * Updates the price data.
     * @return  <code>true</code>, if the update succeeds. Otherwise return
     *          <code>false</code>.
     */
    public boolean updateValues() {
        LinkedHashMap<Date, Double> newData = this.api.getData(this.symbol);
        
        this.updateCurrentPrice();
        
        if (!newData.equals(this.valueData)) {
            this.valueData = newData;
            return true;
        }
        
        return false;
    }
    
    /**
     * Updates the current price from the API.
     */
    public void updateCurrentPrice() {
        this.currentPrice = this.api.getQuote(this.symbol, this.exchange).getPrice();
    }
    
    /**
     * Updates the mean buy price for the security.
     */
    public void updateMeanBuyPrice() {
        Double price = 0.0;
        
        for (SimpleTransaction t : this.buyData) {
            price += t.getPrice() * t.getAmount();
        }
        
        this.meanBuyPrice = price / this.amount.doubleValue();
    }
    
    public Double getBuyPrice() {
        return this.meanBuyPrice;
    }
    
    public void setBuyPrice(Double p) {
        this.meanBuyPrice = p;
    }
    
    public Double getCurrentPrice() {
        return this.currentPrice;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer a) {
        this.amount = a;
    }
    
    /**
     * Buy more securities.
     * @param p The buy price of a single security
     * @param a The amount to buy as an integer
     * @param t The time of the transaction
     * @return  <code>true</code>, if the transaction succeeds.
     *          Otherwise return <code>false</code>.
     */
    public boolean buy(Double p, Integer a, Date t) {
        this.amount += a;
        this.buyData.add(new SimpleTransaction(p, a, t));
        
        this.updateMeanBuyPrice();
        
        return true;
    }
    
    /**
     * Sell securities.
     * @param a The amount to sell as an integer
     * @return  <code>true</code>, if the transaction succeeds.
     *          Otherwise return <code>false</code>.
     */
    public boolean sell(Integer a) {
        if (this.amount >= a) {
            this.amount -= a;
            
            for (SimpleTransaction t : this.buyData) {
                if (a >= t.getAmount()) {
                    a -= t.getAmount();
                    this.buyData.pop();
                    if (a == 0) {
                        break;
                    }
                } else {
                    t.setAmount(t.getAmount() - a);
                    break;
                }
            }
            
            this.updateMeanBuyPrice();
            
            return true;
        }
        
        return false;
    }
}
