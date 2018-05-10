
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
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import java.util.Random;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;

public class WekaTest {
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
    
    public static void main(String[] args) throws Exception {
        //OVO JE PRETVARANJE CSV U ARFF
    // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_1.csv"));
    Instances data = loader.getDataSet();//get instances object

    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);//set the dataset we want to convert
    //and save as ARFF
    saver.setFile(new File("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_1.arff"));
    saver.writeBatch();


	DataSource source = new DataSource("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_1.arff");
	Instances dataset = source.getDataSet();
	//set class index to the last attribute
	dataset.setClassIndex(dataset.numAttributes()-1);
				
        NumericToNominal convert= new NumericToNominal();
        
	J48 tree = new J48();
                
        convert.setInputFormat(dataset);
        Instances newData=Filter.useFilter(dataset, convert);
	
	tree.buildClassifier(newData);
        System.out.println(tree.getCapabilities().toString());
        System.out.println(tree.graph());
        
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(newData);
        System.out.println(nb.getCapabilities().toString ());

        
        
	Evaluation eval = new Evaluation(newData);
	Random rand = new Random(1);
	int folds = 10;
		
		//Notice we build the classifier with the training dataset
        //we initialize evaluation with the training dataset and then
        //evaluate using the test dataset

	DataSource source1 = new DataSource("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_1.arff");
	Instances testDataset = source1.getDataSet();
	testDataset.setClassIndex(testDataset.numAttributes()-1);
        
        convert.setInputFormat(testDataset);
        Instances newData1=Filter.useFilter(testDataset, convert);
	
	
	eval.crossValidateModel(tree, newData1, folds, rand);
	System.out.println(eval.toSummaryString("Evaluation results:\n", false));
		
		System.out.println("Correct % = "+eval.pctCorrect());
		System.out.println("Incorrect % = "+eval.pctIncorrect());
		System.out.println("AUC = "+eval.areaUnderROC(1));
		System.out.println("kappa = "+eval.kappa());
		System.out.println("MAE = "+eval.meanAbsoluteError());
		System.out.println("RMSE = "+eval.rootMeanSquaredError());
		System.out.println("RAE = "+eval.relativeAbsoluteError());
		System.out.println("RRSE = "+eval.rootRelativeSquaredError());
		System.out.println("Precision = "+eval.precision(1));
		System.out.println("Recall = "+eval.recall(1));
		System.out.println("fMeasure = "+eval.fMeasure(1));
		System.out.println("Error Rate = "+eval.errorRate());
		System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
	        

    
        
        
        // I've commented the code as best I can, at the moment.
        // Comments are denoted by "//" at the beginning of the line.
        /*
        BufferedReader datafile = readDataFile("iris.arff"); //učitava podatke
        
        Instances data = new Instances(datafile); //sprema podatke u data
        data.setClassIndex(data.numAttributes() - 1); //postavlja klasu koju predviđamo (inače zadnja)
        
        // Choose a type of validation split
        Instances[][] split = crossValidationSplit(data, 10); //dijeli data u 10 skupina
        
        // Separate split into training and testing arrays
        Instances[] trainingSplits = split[0]; //data za treniranje
        Instances[] testingSplits  = split[1]; //data za testiranje
        
        // Choose a set of classifiers
        Classifier[] models = {     new J48(),
                                    new PART(),
                                    new DecisionTable(),
                                    new OneR(),
                                    new LMT(),
                                    new DecisionStump() 
        };
        
        
        // Run for each classifier model
        for(int j = 0; j < models.length; j++) {

            // Collect every group of predictions for current model in a FastVector
            FastVector predictions = new FastVector();
            
            // For each training-testing split pair, train and test the classifier
            for(int i = 0; i < trainingSplits.length; i++) {
                Evaluation validation = simpleClassify(models[j], trainingSplits[i], testingSplits[i]);
                predictions.appendElements(validation.predictions());
                
                // Uncomment to see the summary for each training-testing pair.
                //System.out.println(models[j].toString()); //rad klasifikatora >> j48 stvara stabla
            }
            
            // Calculate overall accuracy of current classifier on all splits
            double accuracy = calculateAccuracy(predictions);
            
            // Print current classifier's name and accuracy in a complicated, but nice-looking way.
            System.out.println(models[j].getClass().getSimpleName() + ": " + String.format("%.2f%%", accuracy) + "\n=====================");
        }
     */
        //proba nesto = new proba();
      /*  try {
            nesto.loadFile("C:\\Users\\Maikol\\Desktop\\PROGRAMSKO DATASETS\\JDT_R2_0.csv");
            if(nesto.getData() != null){
               System.out.println("File loaded");
            }
        } catch (Exception ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        */
           
    }
}