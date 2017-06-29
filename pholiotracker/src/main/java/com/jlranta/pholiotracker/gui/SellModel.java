package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;
import com.jlranta.pholiotracker.portfolio.Security;

import javax.swing.DefaultListModel;
import java.util.Map;

/**
 *
 * @author Jarkko Lehtoranta
 */
public class SellModel extends DefaultListModel {
    private Portfolio portfolio;
    
    public SellModel(Portfolio p) {
        this.portfolio = p;
        this.updateData();
    }
    
    public void updateData() {
        this.clear();
        for (Map.Entry<String, Security> sec : this.portfolio.getSecurities().entrySet()) {
            this.addElement(sec.getValue());
        }
    }
}
