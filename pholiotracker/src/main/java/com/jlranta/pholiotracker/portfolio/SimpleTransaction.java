package com.jlranta.pholiotracker.portfolio;

import java.util.Date;

/**
 * Describes a single stock transaction.
 * @author Jarkko Lehtoranta
 */
public class SimpleTransaction {
    private Double price;
    private Integer amount;
    private Date time;
    
    /**
     * Creates a new SimpleTransaction.
     * @param p The buy price
     * @param a The buy amount
     * @param t The buy time
     */
    public SimpleTransaction(Double p, Integer a, Date t) {
        this.price = p;
        this.amount = a;
        this.time = t;
    }
    
    public Double getPrice() {
        return this.price;
    }
    
    public Integer getAmount() {
        return this.amount;
    }
    
    public void setAmount(Integer a) {
        this.amount = a;
    }
    
    public Date getTime() {
        return this.time;
    }
}
