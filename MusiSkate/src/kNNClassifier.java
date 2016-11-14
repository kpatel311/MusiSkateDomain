import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class kNNClassifier extends RuntimeException{

    private static final String MODEL_PATH = ".";
    private static Classifier model = null;
    private Instances trainingSet;

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
    
    //Retrains the model once.
    private void loadModel() throws Exception{
    	if (model == null) {
    		BufferedReader datafile = readDataFile("/Users/DrSosa/CS4605/Tricks_Algorithm/MusiSkate/src/dataset.arff");
    		
    		Instances data = new Instances(datafile);
    		data.setClassIndex(data.numAttributes() - 1);
    		Classifier ibk = new IBk(5);
    		
    		ibk.buildClassifier(data);
    		
    		model = (Classifier)ibk;
    	}
        //model = (Classifier) SerializationHelper.read("./kNN_after_collection_1.model");
    }

    //Takes in 2d array of data; internal arrays are acc, gyro, and mag
    public kNNClassifier(double[][] acc,
    		double[][] gyro,
    		//double[][] mag,
    		String label) throws Exception{

    	this(acc, gyro);
        this.loadModel();
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        //data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr, label);
        this.trainingSet = extractor.getInstance();

    }
    
    public kNNClassifier(double[][] acc,
    		double[][] gyro) throws Exception{

        this.loadModel();
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        //data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr);
        this.trainingSet = extractor.getInstance();
  
    }

    //Try it out with another instance
    public void addNewInstance(double[][] acc, double[][] gyro, double[][] mag, String label) {
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr, label);
        this.trainingSet = extractor.getInstance();
    }
    
    public void addNewInstanceWithoutLabel(double[][] acc, double[][] gyro) {
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        //data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr);
        this.trainingSet = extractor.getInstance();   	
    }

    //Train on the single instance
    public void train() throws Exception{
    	((IBk) this.model).updateClassifier(this.trainingSet.firstInstance());
    }

    //Classifies current instance
    public String classifyInstance() throws Exception{
    	double[] fdistribution = this.model.distributionForInstance(this.trainingSet.firstInstance());
    	for (double item: fdistribution) {
    		System.out.println(item);
    	}
    	int maxIndex = 0;
    	double max = 0;
    	for (int i=0; i<fdistribution.length; i++) {
    		if (fdistribution[i] > max) {
    			max = fdistribution[i];
    			maxIndex = i;
    		}
    	}
    	switch (maxIndex) {
    		case 0: return "ollie";
    		case 1: return "nollie";
    		case 2: return "kickflip";
    		case 3: return "No trick";
    		case 4: return "heel";
    		case 5: return "pop";
    		case 6: return "360.0";
    		default: return "What the fuck?!";
    	}
    }

    public void saveModel() throws Exception{
    	 weka.core.SerializationHelper.write("./updated_knn.model", this.model);
    }

}