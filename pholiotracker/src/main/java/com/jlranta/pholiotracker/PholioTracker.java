package com.jlranta.pholiotracker;

import com.jlranta.pholiotracker.gui.PholioGui;
import com.jlranta.pholiotracker.portfolio.Portfolio;
import com.jlranta.pholiotracker.api.AlphaVantageApi;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.jlranta.pholiotracker.api.StockApiHandler;
import com.jlranta.pholiotracker.api.StockApi;


public class PholioTracker {
    
     /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        StockApiHandler apiHandler = new StockApiHandler();
        LinkedHashMap<StockApi, ArrayList<String>> r = apiHandler.searchAll("Alphabet");
        
        StockApi vantage = new AlphaVantageApi();
        LinkedHashMap<String, Double> data = vantage.getData("GOOGL");

        System.out.println(data.toString());
        
        Portfolio portfolio = new Portfolio("Sijoitukset", apiHandler);
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PholioGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PholioGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PholioGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PholioGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PholioGui(portfolio).setVisible(true);
            }
        });
    }
}
