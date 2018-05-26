
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
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import java.util.Random;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Utils;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class WekaClassifiers {
    private static Evaluation eval;
    private static Classifier[] clas = new Classifier[50];
    static{
        clas = new Classifier[]{
            new J48(),
            new NaiveBayes(),
            new RandomTree(),
            new REPTree()
            //new RandomForest()
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
    
    public static Instances klasifikatorInit(Instances dataset, int[] n)throws Exception{
        
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
	for(int i=0;i<n.length;i++){
            clas[n[i]].buildClassifier(newData);
        }
        */
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
    
    public static String klasifikatorSplit(Instances newData, int[] n)throws Exception{
        String vratiText = "";
        int folds = 10;
        
        for(int i=0;i<n.length;i++){
            eval = new Evaluation(newData);
            eval.crossValidateModel(clas[n[i]], newData, folds, new Random(1));
            
            switch(n[i]){
                case 0:vratiText = vratiText.concat("J48\n");
                break;
                case 1:vratiText = vratiText.concat("Bayes\n");
                break;
                case 2:vratiText = vratiText.concat("RandomTree\n");
                break;
                case 3:vratiText = vratiText.concat("REPTree\n");
                break;
                case 4:vratiText = vratiText.concat("RandomForest\n");
                break;
                
                default:vratiText = vratiText.concat("Greška\n");
                break;               
           
            }
            double x = eval.truePositiveRate(1);
            double y = eval.trueNegativeRate(1);
            
            vratiText = vratiText.concat("Correct % = "+eval.pctCorrect()+"\nIncorrect % = "+eval.pctIncorrect()+"\n");
            vratiText = vratiText.concat("AUC = "+eval.areaUnderROC(1)+"\n");
            vratiText = vratiText.concat("Gm = " + Math.sqrt(x*y) +"\n");
        }
        return vratiText;
    }
    
    public static void ROC_graph () throws Exception{
        ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0;
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
        String plotName = vmc.getName(); 
        final javax.swing.JFrame jf = 
        new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
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
    
    public static double[][] box_plot(Instances data, int[] indices) throws Exception{
        int folds = 10;
        Random random = new Random(1); 
        double[][] GM = new double[indices.length][folds];
        
        for (int i = 0; i < indices.length; i++) { 
            for (int j=0; j<folds; j++){
                eval = new Evaluation(data); 
                Instances train = data.trainCV(folds, j, random); 
                eval.setPriors(train); 
                //Classifier copiedClassifier = Classifier.makeCopy(clas[1]); 
                //clas[1].buildClassifier(train); 
                Instances test = data.testCV(folds, j); 
                eval.evaluateModel(clas[indices[i]], test); 
                // output fold statistics 
                //System.out.println("\nFold " + (i+1) + ":\n" + eval.toSummaryString()); 
                double x = eval.truePositiveRate(1);
                double y = eval.trueNegativeRate(1);
                GM[i][j] = Math.sqrt(x*y);
            }
        } 
        /*
        for (int i = 0; i < indices.length; i++) { 
            for (int j=0; j<folds; j++){
                System.out.println(i+"."+j+"***"+GM[i][j]+" ");
            }
            System.out.println("\n");
        } 
        */
        return GM;
    }
}