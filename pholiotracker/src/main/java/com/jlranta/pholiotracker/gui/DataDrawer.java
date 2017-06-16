package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author Jarkko Lehtoranta <devel@jlranta.com>
 */
public class DataDrawer implements ActionListener {
    private DynamicTimeSeriesCollection data;
    private Portfolio portfolio;
    
    public DataDrawer(Portfolio p) {
        this.portfolio = p;
    }
    
    public void setDataSource(String s) {
        if (s.equals(this.portfolio.toString())) {
            //this.data = this.portfolio.getValues();
        } else {
            //this.data = this.portfolio.getSecurity(s).getValues();
        }
    }
    
    public TimeSeriesCollection getData() {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);
        
        final TimeSeries s1 = new TimeSeries("Portfolio", Day.class);
        s1.add(new Day(1, 6, 2017), 5888.2);
        s1.add(new Day(2, 6, 2017), 5902.1);
        s1.add(new Day(3, 6, 2017), 6000.55);
        s1.add(new Day(4, 6, 2017), 6030.11);
        s1.add(new Day(5, 6, 2017), 6002.15);
        s1.add(new Day(6, 6, 2017), 6004.20);
        s1.add(new Day(7, 6, 2017), 6040.18);
        s1.add(new Day(8, 6, 2017), 6080.88);
        s1.add(new Day(9, 6, 2017), 6075.43);
        s1.add(new Day(10, 6, 2017), 6098.77);
        s1.add(new Day(11, 6, 2017), 6024.99);
        s1.add(new Day(12, 6, 2017), 6036.11);
        s1.add(new Day(13, 6, 2017), 6066.9);
        s1.add(new Day(14, 6, 2017), 6089.32);
        s1.add(new Day(15, 6, 2017), 6090.7);
        s1.add(new Day(16, 6, 2017), 6100.11);
        
        dataset.addSeries(s1);

        return dataset;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
