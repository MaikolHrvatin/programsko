import java.awt.Font;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.Log;
import org.jfree.util.LogContext;
import weka.core.Instances;

/**
 * Demonstration of a box-and-whisker chart using a {@link CategoryPlot}.
 *
 * @author David Browning
 */
public class BoxAndWhiskerDemo extends ApplicationFrame {

    /** Access to logging facilities. */
    private static final LogContext LOGGER = Log.createContext(BoxAndWhiskerDemo.class);
    public WekaClassifiers weka_c = new WekaClassifiers();
    private static String[] clas = new String[50];
    static{
        clas = new String[]{
            "J48",
            "NaiveBayes",
            "RandomTree",
            "RandomForest",
            "PART",
            "Logistic"
        };
    }

    //metoda koja spriječava zatvaranje čitavog programa zatvaranjem box plota
    public void windowClosing(final WindowEvent evt){
        if(evt.getWindow() == this){
            dispose();
        }
    }
    
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public BoxAndWhiskerDemo(final String title, Instances[][] splitData, int[] indices) throws Exception {

        super(title);
        
        final BoxAndWhiskerCategoryDataset dataset = createSampleDataset(splitData, indices);

        final CategoryAxis xAxis = new CategoryAxis("Klasifikator");
        final NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        renderer.setMeanVisible(false); //SAKRIVA MEAN TOČKU
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
            "Box-and-Whiskers",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(900, 540));
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private BoxAndWhiskerCategoryDataset createSampleDataset(Instances[][] splitData, int[] indices) throws Exception {
        
        final int seriesCount = 1; //BROJ BOXPLOTA PO CATEGORIJI/KLASIFIKATORU
        final int categoryCount = indices.length; //BROJ KLASIFIKATORA
        final int entityCount = 10; //BROJ ELEMENATA ZA BOXPLOT
        
        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        
        double[][] GM = weka_c.box_plot(splitData, indices);
        
        for (int i = 0; i < seriesCount; i++) {
            for (int j = 0; j < categoryCount; j++) {
                final List list = new ArrayList();
                // add some values...
                for (int k = 0; k < entityCount; k++) {
                    final double value = GM[j][k];
                    list.add(value);
                }
                LOGGER.debug("Adding series " + i);
                LOGGER.debug(list.toString());
                dataset.add(list, "Box plot ", clas[indices[j]].toString());
            }
            
        }

        return dataset;
    }

}