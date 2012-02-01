import weka.classifiers.Classifier;
import weka.core.Instances;

public class classifier {
	private Classifier cfier;
	private Instances instances;
	private double verdict;
	
	public classifier( String inClassifierPath, Instances inInstances ) {
		instances = inInstances;
		
		try {
			cfier = (Classifier) weka.core.SerializationHelper.read( inClassifierPath );
		}
		catch (Exception e) {
			misc.log( "Error: failed when reading classifier stored in \'" + inClassifierPath + "\'. Aborting ..." );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public int start() {
		int ret = 0;
		
		try {
			verdict = cfier.classifyInstance( instances.firstInstance() );
		} catch (Exception e) {
			misc.log( "Error: while classifying." );
			ret = 1;
		}
		
		return ret;
	}
	
	public String getVerdict() {
		return Double.toString( verdict );
	}
	
}