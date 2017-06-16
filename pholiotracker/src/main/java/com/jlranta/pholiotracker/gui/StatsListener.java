package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.util.List;

/**
 *
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public class StatsListener implements ActionListener {
    private Portfolio portfolio;
    private List<JLabel> labels;
    private StatsModel smodel;
    
    public StatsListener(List<JLabel> l, StatsModel s, Portfolio p) {
        this.labels = l;
        this.smodel = s;
        this.portfolio = p;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (JLabel l : this.labels) {
            Double d = 0.0;
            switch(l.getName()) {
                case "TOTAL_VALUE": l.setText(this.portfolio.getTotalValue().toString()); break;
                case "MARKET_VALUE": l.setText(this.portfolio.getMarketValue().toString()); break;
                case "CASH": l.setText(this.portfolio.getCash().toString()); break;
                case "CHANGE":
                    d = portfolio.getChange();
                    l.setForeground(this.getForegroundColor(d));
                    l.setText(d.toString());
                    break;
                case "PROFIT_LOSS":
                    d = portfolio.getProfitLoss();
                    l.setForeground(this.getForegroundColor(d));
                    l.setText(d.toString() + " %");
                    break;
            }
        }
        
        this.smodel.updateData();
        this.smodel.fireTableDataChanged();
    }
    
    private Color getForegroundColor(Double d) {
        if (d > 0.0) {
            return Color.GREEN;
        } else if (d < 0.0) {
            return Color.RED;
        }
        
        return Color.BLACK;
    }
}
