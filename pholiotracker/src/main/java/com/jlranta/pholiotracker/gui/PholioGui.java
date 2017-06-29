package com.jlranta.pholiotracker.gui;

import com.jlranta.pholiotracker.portfolio.Portfolio;
import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockSearchResult;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Jarkko Lehtoranta
 */
public class PholioGui extends javax.swing.JFrame {
    private Portfolio portfolio;
    private StockApiHandler apiHandler;
    private Timer statsTimer;
    private StatsListener statsListener;
    private Timer graphTimer;
    private DataDrawer dataDrawer;

    /**
     * Creates new form MainGui
     * @param p Portfolio
     * @param h StockApiHandler
     */
    public PholioGui(Portfolio p, StockApiHandler h) {
        this.portfolio = p;
        this.apiHandler = h;
        
        initComponents();
        
        this.jTabbedPane1.addTab("Graph", null, this.makeGraphPanel());
        this.jTabbedPane1.addTab("Stats", null, this.makeStatsPanel());
        this.jTabbedPane1.addTab("Manage", null, this.makeManagePanel());
        this.jTabbedPane1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jTabbedPane1Changed(e);
            }
        });
        
        this.graphTimer.start();
    }
    
    private void jTabbedPane1Changed(ChangeEvent e) {
        JTabbedPane j = (JTabbedPane) e.getSource();
        this.graphTimer.stop();
        this.statsTimer.stop();
        switch(j.getTitleAt(j.getSelectedIndex())) {
            case "Graph": this.dataDrawer.updateAssets(); this.graphTimer.restart(); break;
            case "Stats": this.statsTimer.restart(); break;
            case "Manage": break;
        }
    }
    
    private JComponent makeGraphPanel() {
        JPanel graph = new JPanel(false);
        JLabel asset = new JLabel("Choose asset:");
        JComboBox assetBox = new JComboBox();
        
        ChartPanel chartPanel = new ChartPanel(null);
        this.dataDrawer = new DataDrawer(this.portfolio, assetBox, chartPanel);
        assetBox.addActionListener(this.dataDrawer);
        
        graph.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(20,30,10,10);
        graph.add(asset, c);
        
        c.gridx = 1;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.insets = new Insets(20,5,10,30);
        c.fill = GridBagConstraints.HORIZONTAL;
        graph.add(assetBox, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.anchor = GridBagConstraints.PAGE_END;
        graph.add(chartPanel, c);

        this.graphTimer = new Timer(3600000, this.dataDrawer);
        this.graphTimer.setInitialDelay(0);
        
        return graph;
    }
    
    private JComponent makeStatsPanel() {
        JPanel stats = new JPanel(false);
        JLabel value = new JLabel("Total value:");
        JLabel valueVar = new JLabel("0.0");
        valueVar.setName("TOTAL_VALUE");
        value.setFont(value.getFont().deriveFont(Font.BOLD));
        valueVar.setFont(valueVar.getFont().deriveFont(Font.BOLD));
        JLabel mvalue = new JLabel("Total market value:");
        JLabel mvalueVar = new JLabel("0.0");
        mvalueVar.setName("MARKET_VALUE");
        JLabel cash = new JLabel("Cash:");
        JLabel cashVar = new JLabel("0.0");
        cashVar.setName("CASH");
        JLabel change = new JLabel("Change:");
        JLabel changeVar = new JLabel("0.0 %");
        changeVar.setName("CHANGE");
        JLabel prolo = new JLabel("Unrealised profit/loss:");
        JLabel proloVar = new JLabel("0.0");
        proloVar.setName("PROFIT_LOSS");

        JTable assets = new JTable(new StatsModel(this.apiHandler, this.portfolio)) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.BLACK);
                c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                if (column == 0) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                int modelRow = this.convertRowIndexToModel(row);
                if (column == 3 || column == 4) {
                    String status = (String) getModel().getValueAt(modelRow, 3);
                    if (Double.valueOf(status) > 0.0) {
                        c.setForeground(Color.GREEN);
                    } else if (Double.valueOf(status) < 0.0) {
                        c.setForeground(Color.RED);
                    }
                }
                
                return c;
            }
        };
        assets.setAutoCreateRowSorter(true);
        assets.setFillsViewportHeight(true);
        assets.setPreferredScrollableViewportSize(new Dimension(320, 240));
        assets.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(assets);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        stats.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        Insets lin = new Insets(10, 10, 0, 0);
        Insets rin = new Insets(10, 5, 0, 0);
        c.insets = lin;
        stats.add(value, c);
        c.gridx = 1;
        c.insets = rin;
        stats.add(valueVar, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.insets = lin;
        stats.add(cash, c);
        c.gridx = 1;
        c.insets = rin;
        stats.add(cashVar, c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.insets = lin;
        stats.add(mvalue, c);
        c.gridx = 1;
        c.insets = rin;
        stats.add(mvalueVar, c);
        c.gridx = 2;
        c.insets = new Insets(10, 20, 0, 0);
        stats.add(change, c);
        c.gridx = 3;
        c.insets = rin;
        stats.add(changeVar, c);
        c.gridx = 4;
        c.insets = new Insets(10, 20, 0, 0);
        stats.add(prolo, c);
        c.gridx = 5;
        c.insets = rin;
        stats.add(proloVar, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 6;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.anchor = GridBagConstraints.PAGE_END;
        stats.add(scrollPane, c);
        
        List<JLabel> jlist = Arrays.asList(valueVar, cashVar, mvalueVar, changeVar, proloVar);
        this.statsListener = new StatsListener(jlist, (StatsModel) assets.getModel(), this.portfolio);
        this.statsTimer = new Timer(30000, this.statsListener);
        this.statsTimer.setInitialDelay(0);
        
        return stats;
    }
        
    
    private JComponent makeManagePanel() {
        JPanel manage = new JPanel(false);
        JLabel folio = new JLabel("Portfolio:");
        JTextField folioVar = new JTextField(this.portfolio.toString());
        folioVar.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (folioVar.getText().isEmpty()) {
                    folioVar.setText(portfolio.toString());
                    return;
                }
                portfolio.setName(folioVar.getText());
            }
            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        
        JLabel cash = new JLabel("Cash:");
        JTextField cashVar = new JTextField(this.portfolio.getCash().toString());
        cashVar.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent evt) {
                if (cashVar.getText().isEmpty()) {
                    cashVar.setText(portfolio.getCash().toString());
                    return;
                }
                try {
                    Double d = Double.parseDouble(cashVar.getText());
                    if (d >= 0.0) {
                        portfolio.setCash(d);
                        cashVar.setText(portfolio.getCash().toString());
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    cashVar.setText(portfolio.getCash().toString());
                    JOptionPane.showMessageDialog(null, "Please, enter a numeric value that is bigger or equal to 0.", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        
        
        JLabel buy = new JLabel("Buy Assets:");
        JTextField buySearch = new JTextField("Search...");
        DefaultListModel searchModel = new DefaultListModel();
        buySearch.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent evt) {
                if (buySearch.getText().isEmpty()) {
                    buySearch.setText("Search...");
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if (buySearch.getText().equals("Search...")) {
                    buySearch.setText("");
                }
            }
        });
        buySearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            
            public void handle() {
                if (buySearch.getText().isEmpty()) {
                    return;
                } else {
                    ArrayList<StockSearchResult> results = apiHandler.searchStock(buySearch.getText());
                    searchModel.clear();
                    for (StockSearchResult result : results) {
                        searchModel.addElement(result);
                    }
                }
            }
        });
        
        JList buyList = new JList(searchModel);
        buyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTextField buyAmount = new JTextField("Amount");
        buyAmount.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent evt) {
                if (buyAmount.getText().isEmpty()) {
                    buyAmount.setText("Amount");
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if (buyAmount.getText().equals("Amount")) {
                    buyAmount.setText("");
                }
            }
        });
        JTextField buyPrice = new JTextField("Price");
        JTextField buyTime = new JTextField("Time");
        JButton buyButton = new JButton("Buy");
        buyButton.setEnabled(false);
        SellModel sellModel = new SellModel(this.portfolio);
        BuyListener buyListener = new BuyListener(this.apiHandler, this.portfolio, sellModel, buyAmount, buyPrice, buyTime, buyButton, cashVar);
        buyList.addListSelectionListener(buyListener);
        buyButton.addActionListener(buyListener);
        
        JLabel sell = new JLabel("Sell Assets:");
        JTextField sellAmount = new JTextField("Amount");
        JTextField sellPrice = new JTextField("Price");
        JTextField sellTime = new JTextField("Time");
        JButton sellButton = new JButton("Sell");
        sellButton.setEnabled(false);
        JList sellList = new JList(sellModel);
        sellList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SellListener sellListener = new SellListener(this.apiHandler, this.portfolio, sellModel, sellAmount, sellPrice, sellTime, sellButton, cashVar);
        sellList.addListSelectionListener(sellListener);
        sellButton.addActionListener(sellListener);
        
        manage.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        Insets lin = new Insets(10, 10, 0, 0);
        Insets rin = new Insets(10, 5, 0, 0);
        c.insets = lin;
        manage.add(folio, c);
        c.gridx = 1;
        c.gridwidth = 3;
        c.insets = rin;
        manage.add(folioVar, c);
        
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        c.insets = lin;
        manage.add(cash, c);
        c.gridx = 1;
        c.gridwidth = 3;
        c.insets = rin;
        manage.add(cashVar, c);
        
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        c.insets = new Insets(30, 10, 0, 0);
        manage.add(buy, c);
        
        c.gridx = 4;
        c.insets = new Insets(30, 50, 0, 0);
        manage.add(sell, c);
        
        c.gridy = 2;
        c.gridx = 1;
        c.gridwidth = 3;
        c.insets = new Insets(30, 5, 0, 0);
        manage.add(buySearch, c);
        
        c.gridy = 3;
        c.gridx = 4;
        c.gridwidth = 4;
        c.insets = new Insets(10, 50, 0, 0);
        c.weightx = 1.0;
        c.weighty = 1.0;
        manage.add(sellList, c);
        
        c.gridx = 0;
        c.gridwidth = 4;
        c.insets = lin;
        c.weightx = 1.0;
        c.weighty = 1.0;
        manage.add(buyList, c);
        
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridwidth = 1;
        c.gridy = 4;
        c.gridx = 4;
        c.insets = new Insets(10, 50, 0, 0);
        c.weightx = 0.5;
        manage.add(sellAmount, c);
        c.gridx = 5;
        c.insets = rin;
        c.weightx = 1.0;
        manage.add(sellPrice, c);
        c.gridx = 6;
        c.weightx = 2.0;
        manage.add(sellTime, c);
        c.gridx = 7;
        c.weightx = 0.0;
        manage.add(sellButton, c);
        
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.insets = lin;
        c.weightx = 0.5;
        manage.add(buyAmount, c);
        c.gridx = 1;
        c.insets = rin;
        c.weightx = 1.0;
        manage.add(buyPrice, c);
        c.gridx = 2;
        c.weightx = 2.0;
        manage.add(buyTime, c);
        c.gridx = 3;
        c.weightx = 0.0;
        manage.add(buyButton, c);
        
        return manage;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (evt.getSource() == this.jMenuItem1) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
