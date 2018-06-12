import javanpst.tests.multiple.friedmanTest.*;
import javanpst.data.structures.dataTable.DataTable;
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
        String output = "";

        if(GM.length == 2){
            WilcoxonRankSumTest test = new WilcoxonRankSumTest(data);

            //Run Wilcoxon test
            test.doTest();

            output = output.concat("Wilcoxon ranked sum P_value: " +test.getAsymptoticDoublePValue());
            if(test.getAsymptoticDoublePValue() < 0.05){
                output = output.concat("\n\nPostoji znatna razlika među odabrana dva klasifikatora");
            }else{
                output = output.concat("\n\nNe postoji znatna razlika među odabranim klasifikatorima");
            }
            return output;
        }
        
        //Create tests
        FriedmanTest friedman = new FriedmanTest(data);

        //Run Friedman test
        friedman.doTest();

        output = output.concat("Friedman P_value: " +friedman.getPValue());
        if(friedman.getPValue() < 0.05){
            output = output.concat("\n\nPostoji znatna razlika među bar dva od odabranih klasifikatora");
        }else{
            output = output.concat("\n\nNe postoji znatna razlika među odabranim klasifikatorima");
        }
        return output;
    }//end-method

}//end-class
