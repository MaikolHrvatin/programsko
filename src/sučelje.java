import weka.core.Instances;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import org.jfree.ui.RefineryUtilities;

public class sučelje extends javax.swing.JFrame {

   
    private sučelje() {
        /*
        try {
            Klasifikator_analizator kl = new Klasifikator_analizator(0, "http://192.168.43.5:7779/ws/sucelje?wsdl");
        } catch (IOException ex) {
            Logger.getLogger(sučelje.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        initComponents();
    }
    private WekaData weka_data = new WekaData();
    private WekaClassifiers weka_c = new WekaClassifiers();
    private Instances data = null, new_data = null;
    private Instances[][] split = null;
    private int[] indices;
    private String path_data = "";
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        fileChooser = new javax.swing.JFileChooser();
        jButtonPodatci = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListKlasifikatori = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        b_ROC = new javax.swing.JButton();
        labelDatoteka = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        B_boxplot = new javax.swing.JButton();
        b_usporedi = new javax.swing.JButton();
        b_GRN = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        fileChooser.setDialogTitle("MY title");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Usporedba klasifikatora");

        jButtonPodatci.setText("Prikaži podatke");
        jButtonPodatci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPodatciActionPerformed(evt);
            }
        });

        startButton.setText("Odaberi podatke");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        jListKlasifikatori.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jListKlasifikatori);

        jLabel1.setText("Odabir klasifikatora");

        jLabel2.setText("//držati pritisnuto CTRL za odabir više klasifikatora");

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane1.setViewportView(jTextArea);

        jLabel3.setText("Prikaz podataka");

        b_ROC.setText("ROC krivulja");
        b_ROC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ROCActionPerformed(evt);
            }
        });

        jLabel4.setText("Odabrana datoteka:");

        B_boxplot.setText("BoxPlot");
        B_boxplot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_boxplotActionPerformed(evt);
            }
        });

        b_usporedi.setText("Usporedi klasifikatore");
        b_usporedi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usporediActionPerformed(evt);
            }
        });

        b_GRN.setText("Granična razina neujednačenosti");
        b_GRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_GRNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(labelDatoteka, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startButton)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonPodatci)
                                .addGap(18, 18, 18)
                                .addComponent(b_ROC)
                                .addGap(18, 18, 18)
                                .addComponent(B_boxplot))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_usporedi)
                                .addGap(18, 18, 18)
                                .addComponent(b_GRN)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(startButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(labelDatoteka, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPodatci)
                    .addComponent(b_ROC)
                    .addComponent(B_boxplot)
                    .addComponent(b_usporedi)
                    .addComponent(b_GRN))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPodatciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPodatciActionPerformed
       
        try {            
            /*weka_data.newData("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_1.csv"); //stvara novu datu sa elementom kojeg pogađamo
            if(weka_data.getData() != null){
               System.out.println("File loaded");
            }
            */
            //ODABIR KLASIFIKATORA
            indices = jListKlasifikatori.getSelectedIndices();          
            
            //PODJELA PODATAKA
            String ispis = weka_c.klasifikatorSplit(split, indices);
            //PRIKAZ PODATAKA
            jTextArea.setText(ispis);
  
        } catch (Exception ex) {
            
        }
    }//GEN-LAST:event_jButtonPodatciActionPerformed

    
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        int returnVal = fileChooser.showOpenDialog(this);
        
        //DEKLARIRANJE KLASIFIKATORA
        DefaultListModel modelKlasifikator = new DefaultListModel();
        modelKlasifikator.addElement("J48");
        modelKlasifikator.addElement("NaiveBayes");
        modelKlasifikator.addElement("RandomTree");
        modelKlasifikator.addElement("RandomForest");
        modelKlasifikator.addElement("PART");
        modelKlasifikator.addElement("Logistic");
        
        //NE RADE
        //modelKlasifikator.addElement("LMT");
        //modelKlasifikator.addElement("M5P");
        //modelKlasifikator.addElement("REPTree");
        //modelKlasifikator.addElement("OneR");
        //modelKlasifikator.addElement("ZeroR");
        //modelKlasifikator.addElement("KStar");
        //modelKlasifikator.addElement("LinearRegression");
        
        jListKlasifikatori.setModel(modelKlasifikator);
        //KRAJ DEKLARACIJE KLASIFIKATORA
        
        //
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //DOBIVAMO CSV FILE
            File file = fileChooser.getSelectedFile();
            path_data = file.getAbsolutePath();
            labelDatoteka.setText(file.getAbsolutePath());
            
            try {
                //MJENJAMO U ARFF FILE
                String arffPath = weka_c.changeCSV_to_ARFF(file.getAbsolutePath());
                        
                //LOADAMO DATASET IZ ARFF DATOTEKE    
                weka_data.loadFile(arffPath);
                
                //DODAJEMO ZADNJI ATRIBUT IMA LI BUG-OVA
                data = weka_data.newData(arffPath);
                
                //PODJELA DATA U 10 DIJELOVA
                new_data = weka_c.klasifikatorInit(data);
                split = weka_c.crossValidationSplit(new_data, 10);
                
            } catch (Exception ex) {
                Logger.getLogger(sučelje.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*
                NE BRISATI KOD SLUŽIT ĆE POSLIJE ZA DOBITI PATH DOKUMENTA ILI DR.
                try {
                textArea.read( new FileReader( file.getAbsolutePath() ), null );
                } catch (IOException ex) {
                System.out.println("problem accessing file"+file.getAbsolutePath());
                }
                */
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void b_ROCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ROCActionPerformed
        try {
            indices = jListKlasifikatori.getSelectedIndices();
            weka_c.ROC_graph(split, indices);
        } catch (Exception ex) {
            Logger.getLogger(sučelje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_b_ROCActionPerformed

    private void B_boxplotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_boxplotActionPerformed
           
        try {
            indices = jListKlasifikatori.getSelectedIndices();
            
            final BoxAndWhiskerDemo boxplot;
            boxplot = new BoxAndWhiskerDemo("Box-and-Whisker Chart", split, indices);
            boxplot.pack();
            RefineryUtilities.centerFrameOnScreen(boxplot);
            boxplot.setVisible(true);
            
        } catch (Exception ex) {
            Logger.getLogger(sučelje.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_B_boxplotActionPerformed

    private void b_usporediActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_usporediActionPerformed
        // TODO add your handling code here:
        try {
            indices = jListKlasifikatori.getSelectedIndices();
            
            final TestClassification friedman = new TestClassification();
            String ispis = friedman.strFriedman(weka_c.box_plot(split, indices));
            
            jTextArea.setText(ispis);
            
        } catch (Exception ex) {
            Logger.getLogger(sučelje.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_b_usporediActionPerformed

    private void b_GRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_GRNActionPerformed
        // TODO add your handling code here:
        indices = jListKlasifikatori.getSelectedIndices();
        
        String ispis = "";//weka_c.GRN(path_data, indices);
        jTextArea.setText(ispis);
    }//GEN-LAST:event_b_GRNActionPerformed

    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sučelje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sučelje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sučelje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sučelje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sučelje().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_boxplot;
    private javax.swing.JButton b_GRN;
    private javax.swing.JButton b_ROC;
    private javax.swing.JButton b_usporedi;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButtonPodatci;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jListKlasifikatori;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JLabel labelDatoteka;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
