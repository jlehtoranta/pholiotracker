package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;
import com.jlranta.pholiotracker.portfolio.Security;

import java.util.LinkedHashMap;
import java.util.Date;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Jarkko Lehtoranta
 */
public class DataDrawer implements ActionListener {
    private LinkedHashMap<Date, Double> data;
    private Portfolio portfolio;
    private JComboBox assetBox;
    private ChartPanel chartPanel;
    private String source;
    
    public DataDrawer(Portfolio p, JComboBox assets, ChartPanel chartPanel) {
        this.portfolio = p;
        this.assetBox = assets;
        this.chartPanel = chartPanel;
        this.source = this.portfolio.toString();
        this.updateAssets();
        this.chartPanel.setChart(this.createChart(this.getData()));
    }
    
    public void updateAssets() {
        this.assetBox.removeAllItems();
        this.assetBox.addItem(this.portfolio.toString());
        for (Map.Entry<String, Security> sec : this.portfolio.getSecurities().entrySet()) {
            this.assetBox.addItem(sec.getValue().getDescription());
        }
    }
    
    public void setDataSource(String s) {
        this.source = s;
    }
    
    public TimeSeriesCollection getData() {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        TimeSeries s1 = new TimeSeries(this.source);
        
        if (this.source.equals(this.portfolio.toString())) {
            this.data = this.portfolio.getValues();
        } else if (this.portfolio.getSecurity(this.source) != null) {
            this.data = this.portfolio.getSecurity(this.source).getValues();
        }
        
        if (this.data != null) {
            for (Map.Entry<Date, Double> e : this.data.entrySet()) {
                s1.add(new Day(e.getKey()), e.getValue());
            }
        }
        
        dataset.addSeries(s1);

        return dataset;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.assetBox) {
            if (this.assetBox.getSelectedIndex() != -1) {
                this.setDataSource((String) this.assetBox.getSelectedItem());
            }
        }
        this.chartPanel.setChart(this.createChart(this.getData()));
    }
    
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Stock price", "Timestamp", "USD", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        return result;
    }
}
