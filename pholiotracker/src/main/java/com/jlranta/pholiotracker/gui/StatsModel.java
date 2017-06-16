package com.jlranta.pholiotracker.gui;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public class StatsModel extends AbstractTableModel {
    private String[] columnNames = {"Name", "Price", "Acquisition price", "Profit/Loss", "%", "Amount", "Value"};
    private Object[][] data = {{"Alphabet Inc", "967.93", "651.20", "633.46", "48.6 %", "2", "1935.86"},
                                {"Apple Inc", "145.16", "92.60", "525.60", "56.8 %", "10", "1451.60"},
                                {"Coca-Cola Co", "45.25", "42.00", "65.00", "7.7 %", "20", "905.00"},
                                {"Harley-Davidson Inc", "54.89", "59.23", "-65.10", "-7.3 %", "15", "823.35"},
                                {"Pfizer Inc", "32.81", "34.66", "-55.50", "-5.3 %", "30", "984.30"}};
    
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
        return this.data.length;
    }
    
    @Override
    public Object getValueAt(int r, int c) {
        return data[r][c];
    }
    
    public void updateData() {

    }
    
}
