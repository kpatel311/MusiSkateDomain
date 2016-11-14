import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NaiveBayesClassifier extends RuntimeException{

    private static final String MODEL_PATH = ".";
    private static Classifier model = null;
    private Instances trainingSet;

    //Loads the saved model.
    private void loadModel() throws Exception{
        model = (Classifier) SerializationHelper.read("./NB_Original.model");
    }

    //Takes in 2d array of data; internal arrays are acc, gyro, and mag
    public NaiveBayesClassifier(double[][] acc,
    		double[][] gyro,
    		double[][] mag,
    		String label) throws Exception{

    	this(acc, gyro, mag);
        this.loadModel();
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr, label);
        this.trainingSet = extractor.getInstance();

    }
    
    public NaiveBayesClassifier(double[][] acc,
    		double[][] gyro, double[][] mag) throws Exception{

        this.loadModel();
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        data_arr.add(mag);
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
    
    public void addNewInstanceWithoutLabel(double[][] acc, double[][] gyro, double[][] mag) {
        ArrayList<double[][]> data_arr = new ArrayList<double[][]>();
        data_arr.add(acc);
        data_arr.add(gyro);
        data_arr.add(mag);
        FeatureExtractor extractor = new FeatureExtractor(data_arr);
        this.trainingSet = extractor.getInstance();   	
    }

    //Train on the single instance
    public void train() throws Exception{
    	((NaiveBayesUpdateable) this.model).updateClassifier(this.trainingSet.firstInstance());
    }

    //Classifies current instance
    public String classifyInstance() throws Exception{
    	double[] fdistribution = this.model.distributionForInstance(this.trainingSet.firstInstance());
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
    	 weka.core.SerializationHelper.write("./updated_nb.model", this.model);
    }

}