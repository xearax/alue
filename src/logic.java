public class logic {
	private String verdict[];
	private String path;
	
	public logic( String path ) {
		this.path = path;
	}

	public int start() {
		extractor extract = new extractor( path );
		if ( extract.start() != 0 ) {
			misc.log( "Error: <error-message>" );
			return 1;
		}
		preprocess pprocess = new preprocess( extract.getLicense() );
		pprocess.start();
		
		classifier cfier = new classifier( pprocess.getInstances() );
		cfier.start();
		//cfier.getVerdict();
		
		return 0;
	}
	
	private boolean isFileOK() {
		return false;
	}
	
	private void traverse( String path ) {
		
	}
	
	public String getVerdict() {
		return "";
	}
}