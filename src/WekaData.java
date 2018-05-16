
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddValues;
import weka.filters.unsupervised.attribute.Remove;

public class WekaData
{
    private static int fileCount = 0;
    private String fileName;
    Remove remove;

    private ConverterUtils.DataSource source;
    private ConverterUtils.DataSource testsource;
    private Instances data;
    private Instances testdata;

    public WekaData()
    {
        initializeOptionArray();
    }

    private void initializeOptionArray()
    {
        
        int[] indicesOfColumnsToUse = new int[] {49};
        remove = new Remove();
        remove.setAttributeIndicesArray(indicesOfColumnsToUse);
        remove.setInvertSelection(true);
    }

    public void loadFile(String filepath) throws Exception
    {
        source = new ConverterUtils.DataSource(filepath);

        data = source.getDataSet();
        remove.setInputFormat(data);
        data = Filter.useFilter(data, remove);
        data.setClassIndex(data.numAttributes() - 1);

        fileCount++;
    }
    
    public double getLast(String path, int row) throws Exception
    {        
        //testsource = new ConverterUtils.DataSource(path);
        //testdata = testsource.getDataSet();
        //System.out.println(testdata.instance(row).value(testdata.numAttributes()-1)+" ");
        
        return(testdata.instance(row).value(testdata.numAttributes()-1));
    }
    
    public Instances newData (String path) throws Exception
    {
        testsource = new ConverterUtils.DataSource(path);
        testdata = testsource.getDataSet();
        
        Attribute newA = new Attribute("Bug");
        Instances newData= new Instances(testdata);
  
        newData.insertAttributeAt(newA, newData.numAttributes());
        

        for(int i=0; i<newData.numInstances(); i++){
            //System.out.println("test" + i);
            if(getLast(path, i)>0) //ako ima bug-ova postavi 1
            {
                newData.instance(i).setValue(newData.numAttributes()-1, 1);
            }
            else
            {
                newData.instance(i).setValue(newData.numAttributes()-1, 0);
            }
        }

        
        //TEST ZA ISPIS PODATAKA
        /*
        for(int i=0; i<10 ; i++){
            System.out.print(newData.instance(i).toString()+"\n");
        }
        System.out.print(newData.instance(0).toString());
        */
        
        return newData;
    }

    public int getFileCount()
    {
        return this.fileCount;
    }

    // Returns the loaded dataset
    public Instances getData()
    {
        return this.data;
    }

    public ConverterUtils.DataSource getDataSource()
    {
        return this.source;
    }
    // Removes the file from memory -> hopefully garbage collector understands this as a cue to delete the files from memory.
    public void removeFileInstance()
    {
        this.source = null;
        this.data = null;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    public void decreaseFileCount()
    {
        if(fileCount > 0)
        {
            fileCount--;
        }
    }
}