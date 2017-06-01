package com.jlranta.pholiotracker.portfolio;

import java.util.LinkedHashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;

import com.jlranta.pholiotracker.api.StockApi;


public class Security {
    private String name;
    private StockApi api;
    private Integer amount;
    private Double meanBuyPrice;
    private ArrayDeque<SimpleEntry<Double, Integer>> buyData = new ArrayDeque<>();
    private LinkedHashMap<String, Double> valueData = new LinkedHashMap<>();
    
    public Security(String name, StockApi api) {
        this.amount = 0;
        this.meanBuyPrice = 0.0;
        this.name = name;
        this.api = api;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public ArrayDeque<SimpleEntry<Double, Integer>> getBuyData() {
        return this.buyData;
    }
    
    public void setBuyData(ArrayDeque<SimpleEntry<Double, Integer>> bd) {
        this.buyData = bd;
    }
    
    public LinkedHashMap<String, Double> getValues() {
        return this.valueData;
    }
    
    public boolean updateValues() {
        LinkedHashMap<String, Double> newData = this.api.getData(this.name);
        
        if (!newData.equals(this.valueData)) {
            this.valueData = newData;
            return true;
        }
        
        return false;
    }
    
    public void updateMeanBuyPrice() {
        Double price = 0.0;
        
        for (SimpleEntry<Double, Integer> t : this.buyData) {
            price += t.getKey() * t.getValue();
        }
        
        this.meanBuyPrice = price / this.amount.doubleValue();
    }
    
    public Double getBuyPrice() {
        return this.meanBuyPrice;
    }
    
    public void setBuyPrice(Double p) {
        this.meanBuyPrice = p;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer a) {
        this.amount = a;
    }
    
    public boolean buy(Integer a, Double p) {
        this.amount += a;
        this.buyData.add(new SimpleEntry<>(p, a));
        
        this.updateMeanBuyPrice();
        
        return true;
    }
    
    public boolean sell(Integer a) {
        if (this.amount >= a) {
            this.amount -= a;
            
            for (SimpleEntry<Double, Integer> t : this.buyData) {
                if (a >= t.getValue()) {
                    a -= t.getValue();
                    this.buyData.pop();
                    if (a == 0) {
                        break;
                    }
                } else {
                    t.setValue(t.getValue() - a);
                    break;
                }
            }
            
            this.updateMeanBuyPrice();
            
            return true;
        }
        
        return false;
    }
}
