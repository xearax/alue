import weka.core.Instances;

public class preprocess {
	private String licens;
	private Instances instances;
	
	public preprocess() {
		
	}
	
	public preprocess( String text ) {
		
	}
	
	public int start() {
		return 1;
	}
	
	public Instances getInstances() {
		return instances;
	}
	
	private boolean convert() {
		return false;
	}
	
	private boolean doFilter() {
		return false;
	}
}