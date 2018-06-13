
import com.proginz.projekt.web_servis.Klasifikator_analizator;
import com.proginz.projekt.web_servis.Tocka;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instances;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.Logistic;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class WekaClassifiers {
    private static Evaluation eval;
    private static final int folds = 10;
    private static Classifier[] clas = new Classifier[50];
    static{
        clas = new Classifier[]{
            new J48(),
            new NaiveBayes(),
            new RandomTree(),
            new RandomForest(),
            new PART(),
            new Logistic()
        };
    }
    
    private static String[] clasName = new String[50];
    static{
        clasName = new String[]{
            "J48",
            "NaiveBayes",
            "RandomTree",
            "RandomForest",
            "PART",
            "Logistic"
        };
    }
    
    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;
        
        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }
        
        return inputReader;
    }
    
    // metoda za build, treniranje i testiranje klasifikatora
    public static Evaluation simpleClassify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation validation = new Evaluation(trainingSet);
        
        model.buildClassifier(trainingSet);
        validation.evaluateModel(model, testingSet);
        
        return validation;
    }
    
    public static double calculateAccuracy(FastVector predictions) {
        double correct = 0;
        
        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }
        
        return 100 * correct / predictions.size();
    }
    
    // podjela podataka u skupine i njihova podjela na dio za treniranje i testiranje
    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];
        
        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }
    
    public static String changeCSV_to_ARFF(String path) throws IOException{
        //AKO ZAVRŠAVA NA ARFF IZAĐI
        if(path.endsWith(".arff")){
            return path;
        }
            
        //OVO JE PRETVARANJE CSV U ARFF
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances data = loader.getDataSet();//get instances object
        
        //promjeni ime stinga u arff
        String newPath = path.replace(".csv", ".arff");
        
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);//set the dataset we want to convert
        //and save as ARFF
        saver.setFile(new File(newPath));
        saver.writeBatch();
        
        return newPath;
    }
    
    public static Instances klasifikatorInit(Instances dataset)throws Exception{
        
        //KOD ZA PRETVARANJE U NOMINALNE VRIJEDNOSTI
        NumericToNominal convert= new NumericToNominal();
        
        String[] options = new String[2];
        options[0]="-R";
        options[1]="50"; //MORA BITI BROJ 50, NE 49 !!!
        convert.setOptions(options);
        
        convert.setInputFormat(dataset);
        
        Instances newData=Filter.useFilter(dataset, convert);
        newData.setClassIndex(newData.numAttributes()-1);
        
        /*
        //TEST ZA KONVERZIJU U NOMINALNE VRIJEDNOSTI
        System.out.println("Before");
        for(int i=0; i<3; i=i+1)
        {
            System.out.println("Nominal? "+dataset.attribute(i+47).isNominal());
        }

        System.out.println("After");
        for(int i=0; i<3; i=i+1)
        {
            System.out.println("Nominal? "+newData.attribute(i+47).isNominal());
        }
        */
        return newData;
    }
    
    public static String klasifikatorSplit(Instances[][] splitData, int[] indices)throws Exception{
        String vratiText = "";
        
        for(int i=0;i<indices.length;i++){
            
            vratiText = vratiText.concat(clasName[indices[i]]+"\n");
            
            float correct = 0;
            float incorrect = 0;
            double roc = 0;
            double gm = 0;
            
            for(int k=0; k<10; k++){
                eval = simpleClassify(clas[indices[i]], splitData[0][k], splitData[1][k]);
                
                correct += eval.pctCorrect();
                incorrect += eval.pctIncorrect();
                roc += eval.areaUnderROC(1);
                
                double x = eval.truePositiveRate(1);
                double y = eval.trueNegativeRate(1);
                gm += Math.sqrt(x*y);
                
            }
            
            vratiText = vratiText.concat("Correct % = "+ (correct/folds) +"\nIncorrect % = "+ (incorrect/folds) +"\n");
            vratiText = vratiText.concat("AUC = "+ (roc/folds) +"\n");
            vratiText = vratiText.concat("Gm = " + (gm/folds) +"\n\n");
        }
        return vratiText;
    }
    
    public static void ROC_graph (Instances[][] splitData, int[] indices) throws Exception{
        for(int i=0; i<indices.length; i++){
            ThresholdCurve tc = new ThresholdCurve();
            int classIndex = 0;
            eval = simpleClassify(clas[indices[i]], splitData[0][0], splitData[1][0]);
            Instances result = tc.getCurve(eval.predictions(), classIndex);

            // plot curve
            ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
            vmc.setROCString("(Area under ROC = " + Utils.doubleToString(tc.getROCArea(result), 4) + ")");
            vmc.setName(result.relationName());
            PlotData2D tempd = new PlotData2D(result);
            tempd.setPlotName(result.relationName());
            tempd.addInstanceNumberAttribute();
            // specify which points are connected
            boolean[] cp = new boolean[result.numInstances()];
            for (int n = 1; n < cp.length; n++){
              cp[n] = true;
            }
            tempd.setConnectPoints(cp);
            // add plot
            vmc.addPlot(tempd);

            // prikaz grafa
            final javax.swing.JFrame jf = 
            new javax.swing.JFrame("Weka Classifier Visualize: "+clasName[indices[i]]);
            jf.setSize(500,400);
            jf.getContentPane().setLayout(new BorderLayout());
            jf.getContentPane().add(vmc, BorderLayout.CENTER);
            jf.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
                }
            });
            jf.setVisible(true);
        }
    }
    
    public static double[][] box_plot(Instances[][] splitData, int[] indices) throws Exception{
        double[][] GM = new double[indices.length][folds];
        
        for (int i = 0; i < indices.length; i++) { 
            for (int k=0; k<folds; k++){
                eval = simpleClassify(clas[indices[i]], splitData[0][k], splitData[1][k]);

                double x = eval.truePositiveRate(1);
                double y = eval.trueNegativeRate(1);
                GM[i][k] = Math.sqrt(x*y);
            }
        } 

        return GM;
    }
    
    public String GRN(String path, int[] indices) throws Exception{
        
        String output = "";
        Klasifikator_analizator sucelje = null;
        
	for(int i=0; i<indices.length; i++){	
            // Inicijalizacija objekta koji će se koristiti za komunikaciju s web servisom
            sucelje = new Klasifikator_analizator(path, "http://25.71.49.67:7779/ws/sucelje?wsdl");
            sucelje.obaviSve(clasName[indices[i]]);
            Tocka tocka = sucelje.vratiTockuGranicneRazineNeujednacenosti();
            
            output = output.concat(clasName[indices[i]]+":\n");
            output = output.concat("(x, y): (" + tocka.X() + ", " + tocka.Y() + ")\n\n");
        }
        return output;

        /*
        String output = "";
        Klasifikator_analizator sucelje = new Klasifikator_analizator(1, "http://25.71.49.67:7779/ws/sucelje?wsdl");
        sucelje.obaviSve("NaiveBayes");
        ArrayList<Tocka> arr = sucelje.vratiTocke();
        for (int i=0; i<arr.size(); i++) {
            System.out.println("("+arr.get(i).X()+", "+arr.get(i).Y()+")");
        }
        double[] coeff = sucelje.vratiRegresiju();
        System.out.println("coef="+Arrays.toString(coeff));
        System.out.println(sucelje.granicnaRazinaNeujednacenosti(coeff));
        Tocka t = sucelje.vratiTockuGranicneRazineNeujednacenosti();
        System.out.println(t.X() + ", " + t.Y());
        return output;
        */
    }
}