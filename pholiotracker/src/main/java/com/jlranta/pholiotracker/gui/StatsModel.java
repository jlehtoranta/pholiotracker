package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;
import com.jlranta.pholiotracker.portfolio.Security;
import com.jlranta.pholiotracker.api.StockApiHandler;

import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jarkko Lehtoranta
 */
public class StatsModel extends AbstractTableModel {
    private Portfolio portfolio;
    private StockApiHandler apiHandler;
    private String[] columnNames = {"Name", "Price", "Acquisition price", "Profit/Loss", "%", "Amount", "Value"};
    private ArrayList<ArrayList<String>> data = new ArrayList<>();
    
    public StatsModel(StockApiHandler a, Portfolio p) {
        this.apiHandler = a;
        this.portfolio = p;
    }
    
    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }
    
    @Override
    public String getColumnName(int c) {
        return this.columnNames[c];
    }
    
    @Override
    public int getRowCount() {
        return this.data.size();
    }
    
    @Override
    public Object getValueAt(int r, int c) {
        return this.data.get(r).get(c);
    }
    
    public void updateData() {
        TreeMap<String, Security> secs = this.portfolio.getSecurities();
        this.data.clear();
        
        for (Map.Entry<String, Security> sec : secs.entrySet()) {
            sec.getValue().updateCurrentPrice();
            ArrayList<String> l = new ArrayList<>();
            l.add(sec.getKey());
            l.add(sec.getValue().getCurrentPrice().toString());
            l.add(String.format("%.2f", sec.getValue().getBuyPrice()));
            l.add(String.format("%.2f", sec.getValue().getProfit()));
            l.add(String.format("%.1f", sec.getValue().getProfitPercentage()));
            l.add(sec.getValue().getAmount().toString());
            l.add(String.format("%.2f", sec.getValue().getAmount() * sec.getValue().getBuyPrice()));
            this.data.add(l);
        }
        
    }
    
}
