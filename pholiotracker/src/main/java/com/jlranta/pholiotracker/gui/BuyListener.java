package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockSearchResult;
import com.jlranta.pholiotracker.portfolio.Portfolio;

import java.text.SimpleDateFormat;
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
public class BuyListener implements ActionListener, ListSelectionListener {
    private StockApiHandler apiHandler;
    private Portfolio portfolio;
    private JTextField amount;
    private JTextField price;
    private JTextField time;
    private JButton buyButton;
    private StockSearchResult curStock;
    private JTextField cashVar;
    private SellModel sellModel;
    
    public BuyListener(StockApiHandler apiHandler, Portfolio portfolio, SellModel sellModel, JTextField amount, JTextField price, JTextField time, JButton bbutton, JTextField cashVar) {
        this.apiHandler = apiHandler;
        this.portfolio = portfolio;
        this.sellModel = sellModel;
        this.amount = amount;
        this.price = price;
        this.time = time;
        this.buyButton = bbutton;
        this.cashVar = cashVar;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
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
        
        if (!this.portfolio.buySecurity(this.curStock, p, a, t)) {
            JOptionPane.showMessageDialog(null, "Couldn't buy the assets. Do you have enough cash on your account?", "Buying failed", JOptionPane.ERROR_MESSAGE);
        } else {
            this.cashVar.setText(this.portfolio.getCash().toString());
            JOptionPane.showMessageDialog(null, "Assets bought!", "Buying successful", JOptionPane.INFORMATION_MESSAGE);
            this.sellModel.updateData();
        }
        
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        
        if (list.getSelectedIndex() == -1) {
            this.amount.setText("Amount");
            this.price.setText("Price");
            this.time.setText("Time");
            this.buyButton.setEnabled(false);
            
            return;
        }
        
        StockSearchResult res = (StockSearchResult) list.getModel().getElementAt(list.getSelectedIndex());
        this.curStock = res;
        res = this.apiHandler.getCurrentPrice(res.getSymbol(), res.getExchange());
        this.price.setText(res.getPrice().toString());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm, dd.MM.yyyy");
        this.time.setText(df.format(res.getTime()));
        
        this.buyButton.setEnabled(true);
    }
}
