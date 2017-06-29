package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.portfolio.Security;
import com.jlranta.pholiotracker.portfolio.Portfolio;

import java.text.SimpleDateFormat;
import java.util.EventListener;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Jarkko Lehtoranta
 */
public class SellListener implements ActionListener, ListSelectionListener {
    private StockApiHandler apiHandler;
    private Portfolio portfolio;
    private SellModel smodel;
    private JTextField amount;
    private JTextField price;
    private JTextField time;
    private JButton sellButton;
    private Security curStock;
    private JTextField cashVar;
    
    public SellListener(StockApiHandler apiHandler, Portfolio portfolio, SellModel smodel, JTextField amount, JTextField price, JTextField time, JButton bbutton, JTextField cashVar) {
        this.apiHandler = apiHandler;
        this.portfolio = portfolio;
        this.smodel = smodel;
        this.amount = amount;
        this.price = price;
        this.time = time;
        this.sellButton = bbutton;
        this.cashVar = cashVar;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!e.getSource().equals(this.sellButton)) {
            this.smodel.updateData();
            return;
        }
        
        Integer a;
        Double p;
        Date t;
        
        try {
            a = Integer.parseInt(this.amount.getText());
            if (a < 1) {
                throw new Exception();
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Please, enter an amount that is bigger than 0.", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            p = Double.parseDouble(this.price.getText());
            if (p < 0) {
                throw new Exception();
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Please, enter a price that is bigger than or equal to 0.", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm, dd.MM.yyyy");
            t = df.parse(this.time.getText());
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Please, enter a valid timestamp like \"23:59, 31.12.2016\".", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!this.portfolio.sellSecurity(this.curStock, p, a, t)) {
            JOptionPane.showMessageDialog(null, "Couldn't sell the assets. Do you own enough securities?", "Buying failed", JOptionPane.ERROR_MESSAGE);
        } else {
            this.cashVar.setText(this.portfolio.getCash().toString());
            this.smodel.updateData();
            JOptionPane.showMessageDialog(null, "Assets sold!", "Selling successful", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        
        if (list.getSelectedIndex() == -1) {
            this.amount.setText("Amount");
            this.price.setText("Price");
            this.time.setText("Time");
            this.sellButton.setEnabled(false);
            
            return;
        }
        
        Security res = (Security) list.getModel().getElementAt(list.getSelectedIndex());
        this.curStock = res;
        this.amount.setText(res.getAmount().toString());
        this.price.setText(res.getCurrentPrice().toString());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm, dd.MM.yyyy");
        this.time.setText(df.format(new Date()));
        
        this.sellButton.setEnabled(true);
    }
}
