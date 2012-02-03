import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;	
import weka.core.Instances;
import weka.core.SerializationHelper;

public class classifier {
	private Classifier cfier;
	private Instances instances;
	private double verdict;
	private String classifierDescription;
	
	public classifier( String inClassifierPath, Instances inInstances ) {
		instances = inInstances;
		
		try {
			cfier = (Classifier) weka.core.SerializationHelper.read( inClassifierPath );
			
			try {
				String tmp = cfier.toString();
				if ( tmp.length() > 40 ) {
					classifierDescription = tmp.substring(0,40) + "...";
				}
			} catch (Exception e) {}
		}
		catch (Exception e) {
			misc.log( "Error: failed when reading classifier stored in \'" + inClassifierPath + "\'. Aborting ..." );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public int start() {
		int ret = 0;
        instances.setClassIndex(instances.numAttributes() - 1);
		try {
            verdict = cfier.classifyInstance( instances.firstInstance() );
		} catch (Exception e) {
			misc.log( "Error: while classifying."+e.toString() );
			ret = 1;
		}
		
		return ret;
	}
	
	public String getVerdict() {
		return Double.toString( verdict );
	}
    
    public String getVerdictClass(){
        return instances.classAttribute().value((int) verdict);
    }
    
    public String getInfo() {
    	return classifierDescription;
    }
	
}