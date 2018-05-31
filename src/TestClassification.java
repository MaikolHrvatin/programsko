import javanpst.tests.multiple.friedmanTest.*;
import javanpst.tests.multiple.pageTest.*;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.location.normalScoresTest.NormalScoresTest;
import javanpst.tests.location.wilcoxonRankSumTest.WilcoxonRankSumTest;

public class TestClassification{

    public static String strFriedman(double[][] GM){

        if(GM.length < 2){
            return "Select more clasificators";
        }
        
        //transpose matrix
        double[][] temp = new double[GM[0].length][GM.length];
        for (int i = 0; i < GM.length; i++)
            for (int j = 0; j < GM[0].length; j++)
                temp[j][i] = GM[i][j];
        //Data is formatted
        DataTable data = new DataTable(temp);

        if(GM.length == 2){
            WilcoxonRankSumTest test = new WilcoxonRankSumTest(data);

            //Run Wilcoxon test
            test.doTest();

            return "Wilcoxon ranked sum P_value: "+test.getAsymptoticDoublePValue();
        }
        
        //Create tests
        FriedmanTest friedman = new FriedmanTest(data);

        //Run Friedman test
        friedman.doTest();

        //Print results of Friedman test
        //System.out.println("Results of Friedman test:\n"+friedman.printReport());
        return "Friedman P_value: "+friedman.getPValue();
    }//end-method

}//end-class
